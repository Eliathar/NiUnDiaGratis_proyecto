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
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModPermisoSeleccionadoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match

class ModPermisoSeleccionadoFragment : Fragment() {
    private val job = Job()
    private lateinit var binding: FragmentModPermisoSeleccionadoBinding
    private lateinit var fechaInicio: Date
    private lateinit var dao: DiasDisfrutadosDao
    private lateinit var nombreBD: String
    private lateinit var entidad: DiasDisfrutados
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        daot = database.fTiposDiasDao()
        ViewModelSimple(daot)
    }
    private lateinit var daot: TiposDiasDao
    private lateinit var navController: NavController
    private var id: Int = 0
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
        binding = FragmentModPermisoSeleccionadoBinding.inflate(inflater, container, false)
        val view = binding.root

        //Declaramos el bundle
        val bundle = this.arguments

        //Obtenemos valores del bundle
        id = bundle!!.getInt("id")
        println("esta linea 1 la id es $id")
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }


        //Obtenemos instancia de la base de datos
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        //daot = database.fTiposActividadesDao()
        navController = findNavController()

        /* Obtenemos los datos del registro con la id del bundle en otro hilo, pero esperando a que
        termine de obtener los datos para continuar */
        runBlocking {
            println("esta linea la id es $id")
            entidad = withContext(Dispatchers.IO) {
                //Obtenemos instancia del Dao
                dao = database.fDiasDisfrutadosDao()
                dao.getDiasdisfrutadosById(id)!!
            }
            println(entidad.fechaCon.toString())
            println(entidad.tipoDiaDis)
            //Asignamos los valores a los campos
            //Obtenemos las fechas, las formateamoss y las asignamos a los textos de los botones correspondientes
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaFormateada = formato.format(entidad.fechaCon)
            binding.btnFechaIniPerm11.text = fechaFormateada
            /* Inicializamos valores de campos de fechas para los botones, pues pese a tener el
            texto escrito las variables solo son inicializadas en el onclick, si no se produce el
            evento las variables no tienen valor */
            fechaInicio = entidad.fechaCon

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinConfig()

        binding.btnFechaIniPerm11.setOnClickListener(){
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaInicio = fechaSelec
                binding.btnFechaIniPerm11.text = "Inicio: ${formatearFecha(fechaInicio)}"
            }

        }
        binding.buttonGuardar11.setOnClickListener() {
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
            val tipoDiaDB = withContext(Dispatchers.IO) {
                viewModelT.obtenerTiposDiasList()
            }
            //Iteramos sobre la lista y obtenemos los nombres de los tipos de días
            val nombresTiposActividades = tipoDiaDB.map { it.nombreTipoDia }

            //Creamos un ArrayAdapter con la lista de nombres
            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nombresTiposActividades
            )

            //Configuramos el ArrayAdapter para el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            //Asignamos el adapter
            binding.spinnerTipo11.adapter = adapter

            val spinSelec = adapter.getPosition(entidad.tipoDiaDis)
            println(spinSelec)
            println(entidad.tipoDiaDis)

            //Establecemos la selección en el Spinner
            binding.spinnerTipo11.setSelection(spinSelec)

            //Agregamos los valores al adaptador
            binding.spinnerTipo11.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo11.selectedItem as String
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo11.setSelection(0)
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
            val tipoDia = binding.spinnerTipo11.selectedItem.toString()
            println("A guardar datos2 $tipoDia")
//Creamos las variables para la resta de fechas, modificando el formato para obetener una medida de dias
            val fechaIni = fechaInicio
            println("A guardar datos4")

            //Asignamos los valores a una variable del tipo adecuado para guardar los datos
            println("A guardar datos")
            val permisoNuevo = id?.let { it1 ->
                DiasDisfrutados(
                    id = entidad.id,
                    tipoDiaDis = tipoDia,
                    fechaCon = fechaIni
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
                            "Tipo de permiso: $tipoDia\n" +
                            "Fecha de Inicio: $fechaIni\n"
                )
                //Controlamos la reaccion de pulsar aceptar
                construct.setPositiveButton("Aceptar") { dialog, wich ->
                    if (permisoNuevo != null) {
                        println("datos asignados")
//------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                        lifecycleScope.launch(Dispatchers.IO) {
                            dao.update(permisoNuevo)
                            BBDDHandler.actualizarComputoGlobal(database)
                        }
//------------------------------------Fin hilo secundario-------------------------------------------
                        println("datos guardados?")
                    }
//------------Cargamos el fragment home al guardar los datos en la base de datos--------------------
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