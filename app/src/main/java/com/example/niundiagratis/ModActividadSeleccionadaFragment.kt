package com.example.niundiagratis

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.niundiagratis.DatabaseActive.databaseAct
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModActividadSeleccionadaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class ModActividadSeleccionadaFragment : Fragment(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var fechaInicio: Date
    private lateinit var fechaFinal: Date
    lateinit var binding: FragmentModActividadSeleccionadaBinding
    private lateinit var dao: ActividadesRealizadasDao
    private lateinit var entidad: ActividadesRealizadas
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext())
        daot = database.fTiposActividadesDao()
        ViewModelSimple(daot)
    }
    private lateinit var daot: TiposActividadesDao
    private lateinit var navController: NavController
    private lateinit var tipoActOk: String
    private lateinit var tipoActividad: TiposActividades
    private lateinit var fechaIni: LocalDate
    private lateinit var fechaFin: LocalDate
    private var difDias: Int? = 0
    private lateinit var actividadNueva: ActividadesRealizadas
    private lateinit var spinnerItems: MutableList<Int>
    private lateinit var nombresTiposActividades: List<String>
    private lateinit var camposPU: Map<String, String>
    private lateinit var camposPUVal: Map<String, Int?>
    private var campoConPu: Map.Entry<String, String>? = null
    private var totalDias1: Int = 0
    private var totalDias2: Int = 0
    private var totalDias3: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentModActividadSeleccionadaBinding.inflate(inflater, container, false)
        val view = binding.root

        //Declaramos el bundle
        val bundle = this.arguments

        //Obtenemos valores del bundle
        val id = bundle!!.getInt("id")


        //Obtenemos instancia de la base de datos
        //database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        daot = databaseAct!!.fTiposActividadesDao()
        navController = findNavController()

        /* Obtenemos los datos del registro con la id del bundle en otro hilo, pero esperando a que
        termine de obtener los datos para continuar */
        runBlocking {
            println(id)
            entidad = withContext(Dispatchers.IO) {
                //Obtenemos instancia del Dao
                dao = databaseAct!!.fActividadesRealizadasDao()
                dao.getActividadById(id)!!
            }
            println(entidad.fechaInActOk.toString())
            println(entidad.fechaFiActOk.toString())
            //Asignamos los valores a los campos
            //Obtenemos las fechas, las formateamoss y las asignamos a los textos de los botones correspondientes
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaFormateada = formato.format(entidad.fechaInActOk)
            binding.btnFechaIni08.text = fechaFormateada

            val fechaFormateada1 = formato.format(entidad.fechaFiActOk)
            binding.btnFechaFin08.text = fechaFormateada1

            binding.editTextNombre08.setText(entidad.nombreActOk)
            /* Inicializamos valores de campos de fechas para los botones, pues pese a tener el
            texto escrito las variables solo son inicializadas en el onclick, si no se produce el
            evento las variables no tienen valor */
            fechaInicio = entidad.fechaInActOk
            fechaFinal = entidad.fechaFiActOk

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        spinConfig()

        binding.btnFechaIni08.setOnClickListener {
                showDatePickerDialog(requireContext()) { fechaSelec ->
                    fechaInicio = fechaSelec
                    binding.btnFechaIni08.text = getString(R.string.inicio,formatearFecha(fechaInicio))
                }

        }

        binding.btnFechaFin08.setOnClickListener {
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaFinal = fechaSelec
                binding.btnFechaFin08.text = getString(R.string.fin, formatearFecha(fechaFinal))
            }

        }
        binding.btnMod08.setOnClickListener {
            calculoBtn(1)
        }
        binding.btnDel08.setOnClickListener {
            calculoBtn(2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    private fun spinConfig(){
//----------Obtenemos valores e inicializamos el spinner en un hilo secundario-----------------------
        lifecycleScope.launch {
            //Obtenemos la lista de los tipos de dias
            val tipoActividadDB = withContext(Dispatchers.IO) {
                viewModelT.obtenerTiposActividades()
            }
            //Obtenemos listado de valores para el spinner
            spinnerItems = resources.getIntArray(R.array.spinner_max_items).toMutableList()
            //Iteramos sobre la lista y obtenemos los nombres de los tipos de días
            nombresTiposActividades = tipoActividadDB.map { it.nombreTipoAct }


            //Creamos un ArrayAdapter con la lista de nombres
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nombresTiposActividades
            )
            //Configuramos el adapter y lo asignamos
            val adapterII = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                spinnerItems
            )

            //Configuramos el ArrayAdapter para el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //Especificamos el layout a usar cuando se muestra la lista
            adapterII.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            //Asignamos el adapter
            binding.spinnerTipo08.adapter = adapter
            binding.spinnerMo08.adapter = adapterII

            val spinSelec = adapter.getPosition(entidad.tipoActOk)
            println(spinSelec)
            println(entidad.tipoActOk)

            //Establecemos la selección en el Spinner
            binding.spinnerTipo08.setSelection(spinSelec)

            //Agregamos los valores al adaptador
            binding.spinnerTipo08.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo08.selectedItem as String
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo08.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                        // Encuentra la entidad TiposActividades que corresponde al ítem seleccionado
                        val entidadSeleccionada = tipoActividadDB.find { it.nombreTipoAct == itemSel }
                        if (entidadSeleccionada != null) {
                            if (entidadSeleccionada.tipoDiasGenerados1 == "PU" || entidadSeleccionada.tipoDiasGenerados2 == "PU"|| entidadSeleccionada.tipoDiasGenerados3 =="PU") {
                                binding.spinnerMo08.visibility = View.VISIBLE
                                binding.txtvwDiasPu08.visibility = View.VISIBLE
                            }else{
                                binding.spinnerMo08.visibility = View.INVISIBLE
                                binding.txtvwDiasPu08.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //No se ha realizado ninguna seleccion
                }
            }

    //---------------------------------------Segundo Spinner----------------------------------------
        //--------------------------Inicio de spinners de Int---------------------------------------

            //Obtenemos un mapa de los campos de tipodiasActok para comprobar si contienen el "PU"
            camposPU = mapOf(
                "tipoDiasActOk1" to entidad.tipoDiasActOk1,
                "tipoDiasActOk2" to entidad.tipoDiasActOk2,
                "tipoDiasActOk3" to entidad.tipoDiasActOk3
            )
            /*
            Obtenemos un mapa de los valores de deiasGenOk, en caso de que algun tipodiaactok
            sea "PU" se usara su valor
            */
            camposPUVal = mapOf(
                "tipoDiasActOk1" to entidad.diasGenActOk1,
                "tipoDiasActOk2" to entidad.diasGenActOk2,
                "tipoDiasActOk3"  to entidad.diasGenActOk3
            )
            /*
            Obtenemos la posicion del campo con tipodiasactOk en caso de ser "PU" y en caso
            afirmativo entramos en el if
            */
            campoConPu = camposPU.entries.find { it.value == "PU" }
            if (campoConPu != null){
                //Dado que hay valor que mostrar, mostramos el spinner
                //Configuramos los spinner
                //Obtenemos listado de valores para el spinner
                spinnerItems = resources.getIntArray(R.array.spinner_max_items).toMutableList()
                binding.spinnerMo08.visibility = View.VISIBLE
                //Configuramos el adapter y lo asignamos
                val adapterI = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerItems
                )
                //Especificamos el layout a usar cuando se muestra la lista
                adapterI.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                //Aplicamos el adaptador al spinner
                binding.spinnerMo08.adapter = adapterI

                //Asignamos el valor int de diasGen de la posicion cuyo nombre de campo es "PU"
                val valorDiasGenPU = camposPUVal[campoConPu!!.key]

                //Obtenemos la posicion del adaptador
                val spinSelecmo = adapterI.getPosition(valorDiasGenPU)

                //Añadimos valor por defecto 0 en caso de nuevo
                // spinnerItems.add(0, 0)

                //Asignamos la posicion al spinner en caso de modificar
                binding.spinnerMo08.setSelection(spinSelecmo)
            }
//
//-------------------------Fin hilo secundario------------------------------------------------------
        }
    }

    private fun calculoBtn(opcion: Int){
        //-------------------------------------Boton calcular-----------------------------------------------

            println("calcular pulsado")
            lifecycleScope.launch(Dispatchers.IO) {
                tipoActOk = binding.spinnerTipo08.selectedItem.toString()
                tipoActividad = daot.getTipoActividadByNombre(tipoActOk)!!
                //Creamos las variables para la resta de fechas, modificando el formato para obetener una medida de dias
                fechaIni = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                fechaFin = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                //Calculamos la diferencia en días
                difDias = java.time.temporal.ChronoUnit.DAYS.between(fechaIni, fechaFin).toInt()

//-----------------Calculamos los dias generados en base a los requisitos---------------------------
                //Creamos una variable para el control del when que controla el valor de la variable
                // adecuada en caso de ser PU
                val campoConPu = when {
                    entidad.tipoDiasActOk1 == "PU" -> "tipoDiasActOk1"
                    entidad.tipoDiasActOk2 == "PU" -> "tipoDiasActOk2"
                    entidad.tipoDiasActOk3 == "PU" -> "tipoDiasActOk3"
                    else -> null
                }
                //Asignamos el valor a la respectiva variable de la entidad en caso de ser PU
               if (campoConPu != null) {
                   when (campoConPu){
                       "tipoDiasActOk1" -> if (entidad.diasGenActOk1 != null) totalDias1 = binding.spinnerMo08.selectedItem as Int
                       "tipoDiasActOk2" -> if (entidad.diasGenActOk2 != null) totalDias2 = binding.spinnerMo08.selectedItem as Int
                       "tipoDiasActOk3" -> if (entidad.diasGenActOk3 != null) totalDias3 = binding.spinnerMo08.selectedItem as Int
                   }

               }
                //En caso de no ser PU y existir datos el calculo de dias debe realizarse normalmente
               if (campoConPu != "tipoDiasActOk1" && tipoActividad.requisitosDiasAct1 != null) {
                   totalDias1 = difDias!! / tipoActividad.requisitosDiasAct1!!
               }
               if (campoConPu != "tipoDiasActOk2" && tipoActividad.requisitosDiasAct2 != null) {
                   totalDias2 = difDias!! / tipoActividad.requisitosDiasAct2!!
               }
               if (campoConPu != "tipoDiasActOk3" && tipoActividad.requisitosDiasAct3 != null) {
                   totalDias3 = difDias!! / tipoActividad.requisitosDiasAct3!!
               }
                //Asignamos los valores a una variable del tipo adecuado para guardar los datos
                actividadNueva = ActividadesRealizadas(
                    id = entidad.id,
                    nombreActOk = binding.editTextNombre08.text.toString(),
                    tipoActOk = tipoActOk,
                    tipoDiasActOk1 = tipoActividad.tipoDiasGenerados1.toString(),
                    tipoDiasActOk2 = tipoActividad.tipoDiasGenerados2.toString(),
                    tipoDiasActOk3 = tipoActividad.tipoDiasGenerados3.toString(),
                    diasGenActOk1 = totalDias1,
                    diasGenActOk2 = totalDias2,
                    diasGenActOk3 = totalDias3,
                    fechaInActOk = fechaInicio,
                    fechaFiActOk = fechaFinal,
                    esGuardiaOk = tipoActividad.esGuardia
                )
                /*
                -------------------------Creamos el cuadro de confirmacion------------------------------------------
                Estamos en un hilo secundario, pero el cuadro de dialogo solo se ejecuta en el hilo principal, no
                obstante es necesario que el cauadro aparezca despues de la asignacion de valores, por lo que deb
                ser llamado en el hilo secundario para asegurar que tiene los datos cargados para ejecutarse en el
                hilo principal
                */
                /*withContext(Dispatchers.Main) {
                    val construct = AlertDialog.Builder(context)
                    construct.setTitle("Confirmar datos")
                    construct.setMessage(
                        "¿Estas seguro de que quieres guardar estos datos?:\n\n" +
                                "Nombre de la actividad: ${actividadNueva?.nombreActOk}\n" +
                                "Tipo de Actividad: $tipoActOk\n" +
                                "Fecha de Inicio: $fechaIni\n" +
                                "Fecha de Finalización: $fechaFin"
                    )
                    //Controlamos la reaccion de pulsar aceptar
                    construct.setPositiveButton("Aceptar") { dialog, wich ->
                        if (actividadNueva != null) {
                            println("datos asignados")
//------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                            lifecycleScope.launch(Dispatchers.IO) {
                                dao.update(actividadNueva)
                                BBDDHandler.actualizarDiasGenerados(actividadNueva, databaseAct!!, 2)
                                BBDDHandler.actualizarComputoGlobal(databaseAct!!)
                            }
//------------------------------------Fin hilo secundario-------------------------------------------
                            println("datos guardados?")
                        }
                        //------Cargamos el fragment home al guardar los datos en la base de datos----------
                        navController.navigate(R.id.nav_home)
                    }
                    construct.setNegativeButton("Cancelar", null)
                    construct.show()
                }*/
                if (opcion == 1){
                    cuadroConf()
                }else{
                    cuadroDel()
                }

            }
//-----------------------------------Fin hilo secundario--------------------------------------------

//-----------------------------------Fin boton calcular---------------------------------------------
    }
    //A partir de aqui hay que hacer todas las variables de btncalcular globales a la clase
    // para acceder a ellas desde todas sus funciones y copiar modificando para delete
    private suspend fun cuadroConf(){
        withContext(Dispatchers.Main) {
            val construct = AlertDialog.Builder(context)
            construct.setTitle("Confirmar datos")
            construct.setMessage(
                "¿Estas seguro de que quieres guardar estos datos?:\n\n" +
                        "Nombre de la actividad: ${actividadNueva.nombreActOk}\n" +
                        "Tipo de Actividad: $tipoActOk\n" +
                        "Fecha de Inicio: $fechaIni\n" +
                        "Fecha de Finalización: $fechaFin"
            )
            //Controlamos la reaccion de pulsar aceptar
            construct.setPositiveButton("Aceptar") { _, _ ->
                println("datos asignados")
//------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                lifecycleScope.launch(Dispatchers.IO) {
                    dao.update(actividadNueva)
                    BBDDHandler.actualizarDiasGenerados(actividadNueva, databaseAct!!, 2)
                    BBDDHandler.actualizarComputoGlobal(databaseAct!!)
                }
//------------------------------------Fin hilo secundario-------------------------------------------
                println("datos guardados?")
                //------Cargamos el fragment home al guardar los datos en la base de datos----------
                navController.navigate(R.id.nav_home)
            }
            construct.setNegativeButton("Cancelar", null)
            construct.show()
        }
    }
    private suspend fun cuadroDel(){
        withContext(Dispatchers.Main) {
            val construct = AlertDialog.Builder(context)
            construct.setTitle("Confirmar datos")
            construct.setMessage(
                "¿Estas seguro de que quieres eliminar este registro?:\n\n" +
                        "Nombre de la actividad: ${actividadNueva.nombreActOk}\n" +
                        "Tipo de Actividad: $tipoActOk\n" +
                        "Fecha de Inicio: $fechaIni\n" +
                        "Fecha de Finalización: $fechaFin"
            )
            //Controlamos la reaccion de pulsar aceptar
            construct.setPositiveButton("Aceptar") { _, _ ->
                println("datos asignados")
//------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteById(actividadNueva.id)
                    BBDDHandler.actualizarDiasGenerados(actividadNueva, databaseAct!!, 3)
                    BBDDHandler.actualizarComputoGlobal(databaseAct!!)

                }
//------------------------------------Fin hilo secundario-------------------------------------------
                println("datos guardados?")
                //------Cargamos el fragment home al guardar los datos en la base de datos----------
                navController.navigate(R.id.nav_home)
            }
            construct.setNegativeButton("Cancelar", null)
            construct.show()
        }
    }
}