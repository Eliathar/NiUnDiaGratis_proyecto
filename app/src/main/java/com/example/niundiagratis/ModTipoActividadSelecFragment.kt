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
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModTipoActividadSelecBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match

//TODO: inicializar los valores de los campos segun la actividad pasada
//TODO: pasar datos al mensaje
class ModTipoActividadSelecFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentModTipoActividadSelecBinding
    private lateinit var nombreBD: String
    private lateinit var entidad: TiposActividades

    //Valores para el listado del spinner tipos de dia----------------------------------------------
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        daoT = database.fTiposDiasDao()
        ViewModelSimple(daoT)
    }
    private lateinit var daoT: TiposDiasDao
    //----------------------------------------------------------------------------------------------
    private lateinit var navController: NavController
    private lateinit var database: NiUnDiaGratisBBDD
    private lateinit var dao: TiposActividadesDao
    private lateinit var spinnerItems: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentModTipoActividadSelecBinding.inflate(inflater, container, false)
        val view = binding.root
        //Declaramos el bundle
        val bundle = this.arguments

        //Obtenemos valores del bundle
        val id = bundle!!.getString("id").toString()
        nombreBD = bundle.getString("nombreBD")!!


        //Obtenemos instancia de la base de datos
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        daoT = database.fTiposDiasDao()
        navController = findNavController()

        /* Obtenemos los datos del registro con la id del bundle en otro hilo, pero esperando a que
        termine de obtener los datos para continuar */
        runBlocking {
            println(id)
            entidad = withContext(Dispatchers.IO) {
                //Obtenemos instancia del Dao
                dao = database.fTiposActividadesDao()
                dao.getTipoActividadByNombre(id)!!
            }


            binding.editTextNombreTipoAct15.setText(entidad.nombreTipoAct)
            binding.checkBoxGuardia15.isChecked = entidad.esGuardia


        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        spinConfig()
//--------------------------------Botones-----------------------------------------------------------

        binding.buttonCalcular15.setOnClickListener(){
            btnCalcular()
        }
    }
    private fun spinConfig(){
//----------Obtenemos valores e inicializamos el spinner en un hilo secundario----------------------
        lifecycleScope.launch {
//------------------En caso de ser spinner de Strings-----------------------------------------------
            //Obtenemos la lista de los tipos de dias
            val tipoDiasDB = withContext(Dispatchers.IO) {
                viewModelT.obtenerTiposDiasList()
            }
            //Creamos una lista para poder añadir la opcion por defecto al spinner
            val tipoDiasDBSpin = mutableListOf("Seleccione una opción")

            //Iteramos sobre la lista y obtenemos los nombres de los tipos de días
            val nombresTiposDias = tipoDiasDB.map { it.nombreTipoDia }

            //Añadimos todos los valores de la base de datos a la lista con el valor por defecto
            tipoDiasDBSpin.addAll(nombresTiposDias)
            //Creamos un ArrayAdapter con la lista de nombres
            val adapterS = ArrayAdapter<String>(
                requireContext(),
                R.layout.simple_spinner_item,
                tipoDiasDBSpin
            )
            //Configuramos el ArrayAdapter para el Spinner
            adapterS.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

            //Asignamos el adapter
            binding.spinnerTipo1Dia15.adapter = adapterS
            binding.spinnerTipo2Dia15.adapter = adapterS
            binding.spinnerTipo3Dia15.adapter = adapterS
//--------------------------Fin spinner Strings-----------------------------------------------------

//--------------------------Inicio de spinners de Int-----------------------------------------------
            //Configuramos los spinner
            //Obtenemos listado de valores para el spinner
            spinnerItems = resources.getIntArray(com.example.niundiagratis.R.array.spinner_max_items).toMutableList()
            //Añadimos valor por defecto 0
            spinnerItems.add(0, 0)
            //Configuramos el adapter y lo asignamos
            val adapterI = ArrayAdapter<Int>(
                requireContext(),
                R.layout.simple_spinner_item,
                spinnerItems
            )
            adapterI.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinner1Proporcion15.adapter = adapterI
            binding.spinner2Proporcion15.adapter = adapterI
            binding.spinner3Proporcion15.adapter = adapterI
            //Inicializamos el valor de cada spinner int a su valor de la entidad
            val posicionEntidad1 = spinnerItems.indexOf(entidad.requisitosDiasAct1)
            binding.spinner1Proporcion15.setSelection(posicionEntidad1)
            val posicionEntidad2 = spinnerItems.indexOf(entidad.requisitosDiasAct2)
            binding.spinner2Proporcion15.setSelection(posicionEntidad2)
            val posicionEntidad3 = spinnerItems.indexOf(entidad.requisitosDiasAct3)
            binding.spinner3Proporcion15.setSelection(posicionEntidad3)
//-----------------------------Fin configuracion spinner de items Int-------------------------------



//------------------------------Solo necesario en fragments modificar-------------------------------
            val spinSelec1 = adapterS.getPosition(entidad.tipoDiasGenerados1)
            println(spinSelec1)
            println(entidad.tipoDiasGenerados1)
            val spinSelec2 = adapterS.getPosition(entidad.tipoDiasGenerados2)
            println(spinSelec1)
            println(entidad.tipoDiasGenerados2)
            val spinSelec3 = adapterS.getPosition(entidad.tipoDiasGenerados3)
            println(spinSelec1)
            println(entidad.tipoDiasGenerados3)

            //Establecemos la selección en el Spinner
            binding.spinnerTipo1Dia15.setSelection(spinSelec1)
            binding.spinnerTipo2Dia15.setSelection(spinSelec2)
            binding.spinnerTipo3Dia15.setSelection(spinSelec3)
//-------------------------------Fin parte dedicada a fragments modificar---------------------------

            //Agregamos los valores al adaptador
            //max Anual
            binding.spinnerTipo1Dia15.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo1Dia15.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo1Dia15.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            binding.spinnerTipo2Dia15.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo2Dia15.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo2Dia15.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            binding.spinnerTipo3Dia15.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo3Dia15.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo3Dia15.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            binding.spinner1Proporcion15.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinner1Proporcion15.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinner1Proporcion15.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            binding.spinner2Proporcion15.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinner2Proporcion15.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinner2Proporcion15.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            binding.spinner3Proporcion15.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinner3Proporcion15.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinner3Proporcion15.setSelection(0)
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
            val tipoDia1 = binding.spinnerTipo1Dia15.selectedItem.toString()
            val tipoDia2= binding.spinnerTipo2Dia15.selectedItem.toString()
            val tipoDia3= binding.spinnerTipo3Dia15.selectedItem.toString()

//-------------------------------Spinner valores predefinidos en lista------------------------------
            val propDias1= spinnerItems[binding.spinner1Proporcion15.selectedItemPosition]
            val propDias2= spinnerItems[binding.spinner2Proporcion15.selectedItemPosition]
            val propDias3= spinnerItems[binding.spinner3Proporcion15.selectedItemPosition]

            println("A guardar datos2 $tipoDia1")
            println("A guardar datos2 $tipoDia2")
            println("A guardar datos2 $tipoDia3")
            println("A guardar datos2 $propDias1")
            println("A guardar datos2 $propDias2")
            println("A guardar datos2 $propDias3")

//---Obtencion de valores de BBDD en funcion de valor de spinner solo para datos obtenidos de BBDD---
            val tipoDia11 = daoT.getTipoDiaById(tipoDia1)
            val tipoDia21 = daoT.getTipoDiaById(tipoDia2)
            val tipoDia31 = daoT.getTipoDiaById(tipoDia3)

//--------------------------Varible tipo para pasar los datos a la BBDD-----------------------------
            println("A guardar datos7")
            val tipoDiaNuevo = entidad.nombreTipoAct?.let { it1 ->
                TiposActividades(
                    nombreTipoAct = binding.editTextNombreTipoAct15.text.toString(),
                    tipoDiasGenerados1 = if (tipoDia11?.nombreTipoDia == "Seleccione una opción") null else tipoDia11?.nombreTipoDia,
                    tipoDiasGenerados2 = if (tipoDia21?.nombreTipoDia == "Seleccione una opción") null else tipoDia21?.nombreTipoDia,
                    tipoDiasGenerados3 = if (tipoDia31?.nombreTipoDia == "Seleccione una opción") null else tipoDia31?.nombreTipoDia,
                    requisitosDiasAct1 = if (propDias1 == 0) null else propDias1,
                    requisitosDiasAct2 = if (propDias2 == 0) null else propDias2,
                    requisitosDiasAct3 = if (propDias3 == 0) null else propDias3,
                    esGuardia = binding.checkBoxGuardia15.isChecked
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
                            "Nombre tipo dia: ${tipoDiaNuevo?.nombreTipoAct}\n" +
                            "Tipo dias gen 1: ${tipoDiaNuevo?.tipoDiasGenerados1}\n" +
                            "Tipo dias gen 2: ${tipoDiaNuevo?.tipoDiasGenerados2}\n" +
                            "Tipo dias gen 3: ${tipoDiaNuevo?.tipoDiasGenerados3}\n" +
                            "Proporción 1: ${tipoDiaNuevo?.requisitosDiasAct1}\n" +
                            "Proporción 2: ${tipoDiaNuevo?.requisitosDiasAct2}\n" +
                            "Proporción 3: ${tipoDiaNuevo?.requisitosDiasAct3}\n" +
                            "Es Guardia?: ${tipoDiaNuevo?.esGuardia}\n"
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
                                dao.update(tipoDiaNuevo)
                                println("A guardar datos terminado")

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