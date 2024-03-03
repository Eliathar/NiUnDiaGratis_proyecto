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
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentAddPermisoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.util.Date

// TODO: Rename parameter arguments, choose names that match

class AddPermisoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentAddPermisoBinding
    private lateinit var fechaInicio: Date
    private lateinit var fechaFinal: Date
    private lateinit var entidad: DiasDisfrutados
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        daoT = database.fTiposDiasDao()
        ViewModelSimple(daoT)
    }
    private lateinit var daoT: TiposDiasDao
    private lateinit var navController: NavController
    private lateinit var database: NiUnDiaGratisBBDD
    private lateinit var dao: DiasDisfrutadosDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddPermisoBinding.inflate(inflater, container, false)
        val view = binding.root
        /*//Obtenemos el nombre de la base de datos
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }*/

        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        daoT = database.fTiposDiasDao()
        dao = database.fDiasDisfrutadosDao()
        navController = findNavController()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        spinConfig()
//--------------------------------Botones-----------------------------------------------------------
        binding.btnFechaIniPerm09.setOnClickListener(){
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaInicio = fechaSelec
                binding.btnFechaIniPerm09.text = "Inicio: ${formatearFecha(fechaInicio)}"
            }

        }
        binding.btnFechaFinPerm09.setOnClickListener(){
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaFinal = fechaSelec
                binding.btnFechaFinPerm09.text = "Inicio: ${formatearFecha(fechaFinal)}"
            }

        }
        binding.buttonGuardar.setOnClickListener(){
            btnCalcular()
        }

        //Agregamos los valores al adaptador
        binding.spinnerTipo09.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemSel = binding.spinnerTipo09.selectedItem as String
                if (itemSel == "Ninguna selección"){
                    //Acciones si no hay seleccion
                    binding.spinnerTipo09.setSelection(0)
                }else {
                    //Acciones si hay seleccion
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
                //No se ha realizado ninguna seleccion
            }
        }

    }
    private fun spinConfig(){
//----------Obtenemos valores e inicializamos el spinner en un hilo secundario-----------------------
        lifecycleScope.launch {
            //Obtenemos la lista de los tipos de dias
            val tipoDiaDB = withContext(Dispatchers.IO) {
                viewModelT.obtenerTiposDiasList()
            }
            //Iteramos sobre la lista y obtenemos los nombres de los tipos de días
            val nombresTiposActividades = tipoDiaDB?.map { it.nombreTipoDia } ?: emptyList()

            //Creamos un ArrayAdapter con la lista de nombres
            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nombresTiposActividades
            )

            //Configuramos el ArrayAdapter para el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            //Asignamos el adapter
            binding.spinnerTipo09.adapter = adapter
            //Solo necesario en fragments modificar
            /*val spinSelec = adapter.getPosition(entidad.tipoDiaDis)
            println(spinSelec)
            println(entidad.tipoDiaDis)

            //Establecemos la selección en el Spinner
            binding.spinnerTipo09.setSelection(spinSelec)*/

            //Agregamos los valores al adaptador
            binding.spinnerTipo09.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo09.selectedItem as String
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo09.setSelection(0)
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
            //Obtenemos el tipo de dia disfrutado del spinner
            val tipoDiaDisfrutado = binding.spinnerTipo09.selectedItem.toString()
            println("A guardar datos2 $tipoDiaDisfrutado")
            val tipoDia = daoT.getTipoDiaById(tipoDiaDisfrutado)

            //Creamos las variables para la resta de fechas, modificando el formato para obetener una medida de dias
            val fechaIni = fechaInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            println("A guardar datos4")
            val fechaFin = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            println("A guardar datos5")
            //Calculamos la diferencia en días
            val difDias = java.time.temporal.ChronoUnit.DAYS.between(fechaIni, fechaFin).toInt()
            println("A guardar datos6")

            //Asignamos los valores a una variable del tipo adecuado para guardar los datos
            println("A guardar datos")
            //Bucle para escribir un registro por dia disfrutado
            var permisoNuevo: DiasDisfrutados? = null
            val listaPermisos = mutableListOf<DiasDisfrutados>()
            for (i in 0L..difDias) {
            //Obtenemos el valor para el registro con cambio a formato Date pues esta en LocalDate
            val fechaCon = Date.from(fechaIni.atStartOfDay(ZoneId.systemDefault()).toInstant())
            println("A guardar datos7")
                permisoNuevo = tipoDia.let { it1 ->
                    DiasDisfrutados(
                        id = 0,
                        tipoDiaDis = tipoDiaDisfrutado,
                        fechaCon = fechaCon
                    )

                }
                listaPermisos.add(permisoNuevo)
            println(permisoNuevo)
            println("A guardar datos8")
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
                println("A guardar datos9")
                construct.setTitle("Confirmar datos")
                println("A guardar datos10")
                construct.setMessage(
                    "¿Estas seguro de que quieres guardar estos datos?:\n\n" +
                            "Tipo de dia: $tipoDiaDisfrutado\n" +
                            "Fecha de Inicio: $fechaIni\n" +
                            "Fecha de Finalización: $fechaFin"
                )
                println("A guardar datos11")
                //Controlamos la reaccion de pulsar aceptar

                construct.setPositiveButton("Aceptar") { dialog, wich ->
                    runBlocking {
                        if (permisoNuevo != null) {
                            println("datos asignados")
    //------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                            lifecycleScope.launch(Dispatchers.IO) {
                                println("A guardar datos guardando")
                                dao.insertAll(listaPermisos)
                                BBDDHandler.actualizarComputoGlobal(database)

                                println("A guardar datos terminado")

                            }
    //------------------------------------Fin hilo secundario-------------------------------------------
                            println("datos guardados?")
                        }
                        //------Cargamos el fragment home al guardar los datos en la base de datos----------
                        navController.navigate(R.id.nav_home)
                    }

                }
                construct.setNegativeButton("Cancelar", null)
                construct.show()
            }
        }
//-----------------------------------Fin hilo secundario--------------------------------------------

//-----------------------------------Fin boton calcular---------------------------------------------
    }
}