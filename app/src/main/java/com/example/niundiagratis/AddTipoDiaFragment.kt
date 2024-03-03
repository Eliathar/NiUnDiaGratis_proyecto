package com.example.niundiagratis

import android.R
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
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.TiposDias
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentAddTipoDiaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AddTipoDiaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentAddTipoDiaBinding
    private lateinit var entidad: TiposDias

    //Valores para el listado del spinner-----------------------------------------------------------
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        daoT = database.fComputoGlobalDao()
        ViewModelSimple(daoT)
    }
    private lateinit var daoT: ComputoGlobalDao
    //----------------------------------------------------------------------------------------------
    private lateinit var navController: NavController
    private lateinit var database: NiUnDiaGratisBBDD
    private lateinit var dao: TiposDiasDao
    private lateinit var spinnerItems: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddTipoDiaBinding.inflate(inflater, container, false)
        val view = binding.root

        /*//Obtenemos el nombre de la base de datos
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }*/
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        daoT = database.fComputoGlobalDao()
        dao = database.fTiposDiasDao()
        navController = findNavController()

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        spinConfig()
//--------------------------------Botones-----------------------------------------------------------

        binding.btnGuardar13.setOnClickListener(){
            btnCalcular()
        }
    }
    private fun spinConfig(){
//----------Obtenemos valores e inicializamos el spinner en un hilo secundario----------------------
        lifecycleScope.launch {
//------------------En caso de ser spinner de Strings-----------------------------------------------
            /*//Obtenemos la lista de los tipos de dias
            val tipoDiasDB = withContext(Dispatchers.IO) {
                viewModelT.obtenerTiposDias()
            }
            //Creamos una lista para poder añadir la opcion por defecto al spinner
            val tipoDiasDBSpin = mutableListOf("Seleccione una opción")

            //Iteramos sobre la lista y obtenemos los nombres de los tipos de días
            val nombresTiposDias = tipoDiasDB.map { it.nombreTipoDia }

            //Añadimos todos los valores de la base de datos a la lista con el valor por defecto
            tipoDiasDBSpin.addAll(nombresTiposDias)
            //Creamos un ArrayAdapter con la lista de nombres
            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                tipoDiasDBSpin
            )
            //Configuramos el ArrayAdapter para el Spinner
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            //Asignamos el adapter
            binding.spinnerMax13.adapter = adapter*/
//--------------------------Fin spinner Strings-----------------------------------------------------

//--------------------------Inicio de spinners de Int-----------------------------------------------
            //Configuramos los spinner
            //Obtenemos listado de valores para el spinner
            spinnerItems = resources.getIntArray(com.example.niundiagratis.R.array.spinner_max_items).toMutableList()
            //Añadimos valor por defecto 0
            spinnerItems.add(0, 0)
            //Configuramos el adapter y lo asignamos
            val adapter = ArrayAdapter<Int>(requireContext(), R.layout.simple_spinner_item, spinnerItems)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerMax13.adapter = adapter
//-----------------------------Fin configuracion spinner de items Int-------------------------------



//------------------------------Solo necesario en fragments modificar-------------------------------
            /*val spinSelec = adapter.getPosition(entidad.tipoDiaDis)
            println(spinSelec)
            println(entidad.tipoDiaDis)

            //Establecemos la selección en el Spinner
            binding.spinnerTipo09.setSelection(spinSelec)*/
//-------------------------------Fin parte dedicada a fragments modificar---------------------------

            //Agregamos los valores al adaptador
            //max Anual
            binding.spinnerMax13.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerMax13.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerMax13.setSelection(0)
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
            //Obtenemos el tipo de valor del spinner
//-------------------------------Spinner valores BBDD-----------------------------------------------
            /*val tipoDia1 = binding.spinnerTipo1Dia12.selectedItem.toString()
            val tipoDia2= binding.spinnerTipo2Dia12.selectedItem.toString()
            val tipoDia3= binding.spinnerTipo3Dia12.selectedItem.toString()*/

//-------------------------------Spinner valores predefinidos en lista------------------------------
            val maxAnual= spinnerItems[binding.spinnerMax13.selectedItemPosition]
            /*val propDias2= spinnerItems[binding.spinner2Proporcion12.selectedItemPosition]
            val propDias3= spinnerItems[binding.spinner3Proporcion12.selectedItemPosition]*/
            /*println("A guardar datos2 $tipoDia1")
            println("A guardar datos2 $tipoDia2")
            println("A guardar datos2 $tipoDia3")*/
            println("A guardar datos2 $maxAnual")
            /*println("A guardar datos2 $propDias2")
            println("A guardar datos2 $propDias3")*/

//---Obtencion de valores de BBDD en funcion de valor de spinner solo para datos obtenidos de BBDD---
            /*val tipoDia11 = daoT.getTipoDiaById(tipoDia1)
            val tipoDia21 = daoT.getTipoDiaById(tipoDia2)
            val tipoDia31 = daoT.getTipoDiaById(tipoDia3)*/

//--------------------------Varible tipo para pasar los datos a la BBDD-----------------------------
            println("A guardar datos7")
            val tipoDiaNuevo = maxAnual?.let { it1 ->
                TiposDias(
                    nombreTipoDia = binding.editTextNombre13.text.toString(),
                    maxDias = maxAnual
                )
            }
            println(tipoDiaNuevo)
            println("A guardar datos8")
            //}
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
                            "Nombre tipo: ${tipoDiaNuevo?.nombreTipoDia}\n" +
                            "Max anual: ${tipoDiaNuevo?.maxDias}\n"
                )
                println("A guardar datos11")
                //Controlamos la reaccion de pulsar aceptar

                construct.setPositiveButton("Aceptar") { dialog, wich ->
                    runBlocking {
                        if (tipoDiaNuevo != null) {
                            println("datos asignados")
                            //------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                            lifecycleScope.launch(Dispatchers.IO) {
                                println("A guardar datos guardando")
                                dao.insert(tipoDiaNuevo)
                                println("A guardar datos terminado")
                                val computoModificar = daoT.getComputoGlobalByTipo(tipoDiaNuevo.nombreTipoDia)
                                val computoGlobalNuevo = computoModificar?.let { it ->
                                    ComputoGlobal(
                                        id = computoModificar.id,
                                        tipoDiaGlobal = tipoDiaNuevo.nombreTipoDia,
                                        maxGlobal = dao.getTipoDiaById(tipoDiaNuevo.nombreTipoDia)!!.maxDias ,
                                        genGlobal = it.genGlobal,
                                        conGlobal = it.conGlobal,
                                        saldoGlobal = it.saldoGlobal
                                    )
                                }
                                daoT.update(computoGlobalNuevo!!)

                            }
                            //------------------------------------Fin hilo secundario-------------------------------------------
                            println("datos guardados?")
                        }
                        //------Cargamos el fragment home al guardar los datos en la base de datos----------
                        navController.navigate(com.example.niundiagratis.R.id.nav_home)
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