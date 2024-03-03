package com.example.niundiagratis


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.niundiagratis.data.adapter.SimpleAdapter
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentAddActividadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.BBDDHandler.actualizarComputoGlobal
import com.example.niundiagratis.data.db.BBDDHandler.actualizarDiasGenerados
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.data.db.TiposDias
import java.util.concurrent.TimeUnit
import java.sql.Time
import java.time.LocalDate
import java.time.ZoneId

// TODO: Rename parameter arguments, choose names that match
interface OnMenuItemSelectedListener {
    fun onMenuItemSelected(item: MenuItem)
}
class AddActividadFragment : Fragment(), OnMenuItemSelectedListener {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentAddActividadBinding
    private var mainActivity: MainActivity? = null
    private lateinit var fechaInicio: Date
    private lateinit var fechaFinal: Date
    private val viewModel: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        dao = database.fActividadesRealizadasDao()
        ViewModelSimple(dao)
    }
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        daot = database.fTiposActividadesDao()
        ViewModelSimple(daot)
    }
    private lateinit var dao: ActividadesRealizadasDao
    private lateinit var daot: TiposActividadesDao
    private lateinit var database: NiUnDiaGratisBBDD
    private lateinit var tipoActividadDB: List<TiposActividades>
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        //Obtenemos fecha del calendario

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddActividadBinding.inflate(inflater, container, false)
        val view = binding.root

        /*//Obtenemos el nombre de la base de datos
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }*/
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        dao = database.fActividadesRealizadasDao()
        daot = database.fTiposActividadesDao()
        navController = findNavController()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinConfig()


//-------------------------Fin hilo secundario------------------------------------------------------
        //Boton fecha inicio
        binding.btnFechaIni06.setOnClickListener() {
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaInicio = fechaSelec
                binding.btnFechaIni06.text = "Inicio: ${formatearFecha(fechaInicio)}"
            }
        }
        //Boton fecha fin
        binding.btnFechaFin06.setOnClickListener() {
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaFinal = fechaSelec
                binding.btnFechaFin06.text = "Inicio: ${formatearFecha(fechaFinal)}"
            }
        }
//-------------------------------------Boton calcular-----------------------------------------------
        binding.buttonCalcular06.setOnClickListener() {
            println("calcular pulsado")
            btnCalcular()

//-----------------------------------Fin hilo secundario--------------------------------------------
        }
//-----------------------------------Fin boton calcular---------------------------------------------
    }
//-----------------------------------Fin onViewCreated----------------------------------------------

    override fun onMenuItemSelected(item: MenuItem) {
        mainActivity?.onNavigationItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
            println("attached mainActivity $mainActivity")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }
    private fun spinConfig() {
        //----------Obtenemos valores para el spinner e inicializamos en un hilo secundario-----------------
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
            binding.spinnerTipoActividad06.adapter = adapter


            //Agregamos los valores al adaptador
            binding.spinnerTipoActividad06.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipoActividad06.selectedItem as String
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipoActividad06.setSelection(0)
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
    }
    private fun btnCalcular(){
        lifecycleScope.launch(Dispatchers.IO) {
            println("A guardar datos1")
            //Obtenemos seleccion del spinner
            val tipoActOk = binding.spinnerTipoActividad06.selectedItem.toString()
            println("A guardar datos2 $tipoActOk")
            //Obtenemos el tipo de actividad de esa seleccion
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
                    id = 0,
                    nombreActOk = binding.editTextNombre06.text.toString(),
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
                            dao.insert(actividadNueva)
                            println(" la cuenta es ${actividadNueva.diasGenActOk2} + ${actividadNueva.tipoDiasActOk2}")
                            actualizarDiasGenerados(actividadNueva, database, 1)
                            actualizarComputoGlobal(database)
                            println("el nombre es $database.gett")
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
    }
}



