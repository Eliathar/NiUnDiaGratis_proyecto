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
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModActividadSeleccionadaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class ModActividadSeleccionadaFragment : Fragment(), CoroutineScope {
    // TODO: Rename and change types of parameters
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var fechaInicio: Date
    private lateinit var fechaFinal: Date
    lateinit var binding: FragmentModActividadSeleccionadaBinding
    private lateinit var dao: ActividadesRealizadasDao
    private lateinit var nombreBD: String
    private lateinit var entidad: ActividadesRealizadas
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        daot = database.fTiposActividadesDao()
        ViewModelSimple(daot)
    }
    private lateinit var daot: TiposActividadesDao
    private lateinit var navController: NavController
    private lateinit var database: NiUnDiaGratisBBDD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentModActividadSeleccionadaBinding.inflate(inflater, container, false)
        val view = binding.root

        //Declaramos el bundle
        val bundle = this.arguments

        //Obtenemos valores del bundle
        val id = bundle!!.getInt("id")
        nombreBD = bundle.getString("nombreBD")!!


        //Obtenemos instancia de la base de datos
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        daot = database.fTiposActividadesDao()
        navController = findNavController()

        /* Obtenemos los datos del registro con la id del bundle en otro hilo, pero esperando a que
        termine de obtener los datos para continuar */
        runBlocking {
            println(id)
            entidad = withContext(Dispatchers.IO) {
                //Obtenemos instancia del Dao
                dao = database.fActividadesRealizadasDao()
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

        binding.btnFechaIni08.setOnClickListener() {
                showDatePickerDialog(requireContext()) { fechaSelec ->
                    fechaInicio = fechaSelec
                    binding.btnFechaIni08.text = "Inicio: ${formatearFecha(fechaInicio)}"
                }

        }

        binding.btnFechaFin08.setOnClickListener() {
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaFinal = fechaSelec
                binding.btnFechaFin08.text = "Inicio: ${formatearFecha(fechaFinal)}"
            }

        }
        binding.btnMod08.setOnClickListener() {
            btnCalcular()
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
            //Iteramos sobre la lista y obtenemos los nombres de los tipos de días
            val nombresTiposActividades = tipoActividadDB.map { it.nombreTipoAct }

            //Creamos un ArrayAdapter con la lista de nombres
            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nombresTiposActividades
            )

            //Configuramos el ArrayAdapter para el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            //Asignamos el adapter
            binding.spinnerTipo08.adapter = adapter

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
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
        }
//-------------------------Fin hilo secundario------------------------------------------------------
    }

    private fun btnCalcular(){
        //-------------------------------------Boton calcular-----------------------------------------------

            println("calcular pulsado")
            lifecycleScope.launch(Dispatchers.IO) {
                println("A guardar datos1")
                val tipoActOk = binding.spinnerTipo08.selectedItem.toString()
                println("A guardar datos2 $tipoActOk")
                val tipoActividad = daot.getTipoActividadByNombre(tipoActOk)
                println("A guardar datos3 $tipoActividad")
                //Creamos las variables para la resta de fechas, modificando el formato para obetener una medida de dias
                val fechaIni = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                println("A guardar datos4")
                val fechaFin = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                println("A guardar datos5")
                //Calculamos la diferencia en días
                val difDias = java.time.temporal.ChronoUnit.DAYS.between(fechaIni, fechaFin).toInt()
                println("A guardar datos6")
//-----------------Calculamos los dias generados en base a los requisitos---------------------------

                val totalDias1 = if (difDias != null && tipoActividad?.requisitosDiasAct1 !=
                    null
                ) {
                    difDias / tipoActividad.requisitosDiasAct1
                } else null
                println("A guardar datos7")
                val totalDias2 = if (difDias != null && tipoActividad?.requisitosDiasAct2 !=
                    null
                ) {
                    difDias / tipoActividad.requisitosDiasAct2
                } else null
                println("A guardar datos8")
                val totalDias3 = if (difDias != null && tipoActividad?.requisitosDiasAct3 !=
                    null
                ) {
                    difDias / tipoActividad.requisitosDiasAct3
                } else null
                //Asignamos los valores a una variable del tipo adecuado para guardar los datos
                println("A guardar datos")
                val actividadNueva = tipoActividad?.let { it1 ->
                    ActividadesRealizadas(
                        id = entidad.id,
                        nombreActOk = binding.editTextNombre08.text.toString(),
                        tipoActOk = tipoActOk,
                        tipoDiasActOk1 = tipoActividad?.tipoDiasGenerados1.toString(),
                        tipoDiasActOk2 = tipoActividad?.tipoDiasGenerados2.toString(),
                        tipoDiasActOk3 = tipoActividad?.tipoDiasGenerados3.toString(),
                        diasGenActOk1 = totalDias1,
                        diasGenActOk2 = totalDias2,
                        diasGenActOk3 = totalDias3,
                        fechaInActOk = fechaInicio,
                        fechaFiActOk = fechaFinal,
                        esGuardiaOk = tipoActividad.esGuardia
                    )
                }
                /*
                -------------------------Creamos el cuadro de confirmacion------------------------------------------
                Estamos en un hilo secundario, pero el cuadro de dialogo solo se ejecuta en el hilo principal, no
                obstante es necesario que el cauadro aparezca despues de la asignacion de valores, por lo que deb
                ser llamado en el hilo secundario para asegurar que tiene los datos cargados para ejecutarse en el
                hilo principal
                */
                withContext(Dispatchers.Main) {
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
                                BBDDHandler.actualizarDiasGenerados(actividadNueva, database, 2)
                                BBDDHandler.actualizarComputoGlobal(database)
                            }
//------------------------------------Fin hilo secundario-------------------------------------------
                            println("datos guardados?")
                        }
                        //------Cargamos el fragment home al guardar los datos en la base de datos----------
                        navController.navigate(R.id.nav_home)
                    }
                    construct.setNegativeButton("Cancelar", null)
                    construct.show()
                }
            }
//-----------------------------------Fin hilo secundario--------------------------------------------

//-----------------------------------Fin boton calcular---------------------------------------------
    }
}