package com.example.niundiagratis

//import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentAddTipoActividadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date
import com.example.niundiagratis.R



class AddTipoActividadFragment : Fragment() {
    private lateinit var binding: FragmentAddTipoActividadBinding
    private lateinit var fechaInicio: Date
    private lateinit var fechaFinal: Date
    private lateinit var entidad: TiposActividades

    //Valores para el listado del spinner-----------------------------------------------------------
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
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
        binding = FragmentAddTipoActividadBinding.inflate(inflater, container, false)
        val view = binding.root

        /*//Obtenemos el nombre de la base de datos
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }*/
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        daoT = database.fTiposDiasDao()
        dao = database.fTiposActividadesDao()
        navController = findNavController()
         return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        spinConfig()
//--------------------------------Botones-----------------------------------------------------------

        binding.buttonCalcular12.setOnClickListener(){
            btnCalcular()
        }
    }

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            // Obtener el spinner
            val spinner = parent as Spinner

            // Obtener el valor seleccionado del spinner
            val itemSel = spinner.selectedItem as String

            // Si no hay selección, no hacer nada
            if (itemSel == "Ninguna selección") {
                return
            }

            // Acciones si hay selección
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
    private fun spinConfig(){
//----------Obtenemos valores e inicializamos el spinner en un hilo secundario-----------------------
        lifecycleScope.launch {
            //Obtenemos la lista de los tipos de dias
            val tipoDiasDB = withContext(Dispatchers.IO) {
                viewModelT.obtenerTiposDiasList()
            }
            //Creamos una lista para poder añadir la opcion por defecto al spinner
            val tipoDiasDBSpin = mutableListOf("Seleccione una opción")

            //Iteramos sobre la lista y obtenemos los nombres de los tipos de días
            val nombresTiposDias = tipoDiasDB?.map { it.nombreTipoDia } ?: emptyList()

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
            binding.spinnerTipo1Dia12.adapter = adapter
            binding.spinnerTipo2Dia12.adapter = adapter
            binding.spinnerTipo3Dia12.adapter = adapter

            //Spinner relativo a la proporcion de dias
            spinnerItems = resources.getIntArray(com.example.niundiagratis.R.array.spinner_prop_items).toMutableList()
            spinnerItems.add(0, 0)
            val adapterProp = ArrayAdapter<Int>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerItems)
            adapterProp.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
            binding.spinner1Proporcion12.adapter = adapterProp
            binding.spinner2Proporcion12.adapter = adapterProp
            binding.spinner3Proporcion12.adapter = adapterProp

            //Solo necesario en fragments modificar
            /*val spinSelec = adapter.getPosition(entidad.tipoDiaDis)
            println(spinSelec)
            println(entidad.tipoDiaDis)

            //Establecemos la selección en el Spinner
            binding.spinnerTipo09.setSelection(spinSelec)*/

            //Agregamos los valores al adaptador
            //Tipo de dia 1
            binding.spinnerTipo1Dia12.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo1Dia12.selectedItem as String
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo1Dia12.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            //Tipo de dia 2
            binding.spinnerTipo2Dia12.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo2Dia12.selectedItem as String
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo2Dia12.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            //Tipo de dia 3
            binding.spinnerTipo3Dia12.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerTipo3Dia12.selectedItem as String
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerTipo3Dia12.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            //Tipo de proporcion dias 1
            binding.spinner1Proporcion12.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinner1Proporcion12.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinner1Proporcion12.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            //Tipo de proporcion dias 2
            binding.spinner2Proporcion12.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinner2Proporcion12.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinner2Proporcion12.setSelection(0)
                    } else {
                        //Acciones si hay seleccion
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                    //No se ha realizado ninguna seleccion
                }
            }
            //Tipo de proporcion dias
            binding.spinner3Proporcion12.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinner3Proporcion12.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinner3Proporcion12.setSelection(0)
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
            val tipoDia1 = binding.spinnerTipo1Dia12.selectedItem.toString()
            val tipoDia2= binding.spinnerTipo2Dia12.selectedItem.toString()
            val tipoDia3= binding.spinnerTipo3Dia12.selectedItem.toString()
            val propDias1= spinnerItems[binding.spinner1Proporcion12.selectedItemPosition]
            val propDias2= spinnerItems[binding.spinner2Proporcion12.selectedItemPosition]
            val propDias3= spinnerItems[binding.spinner3Proporcion12.selectedItemPosition]
            println("A guardar datos2 $tipoDia1")
            println("A guardar datos2 $tipoDia2")
            println("A guardar datos2 $tipoDia3")
            println("A guardar datos2 $propDias1")
            println("A guardar datos2 $propDias2")
            println("A guardar datos2 $propDias3")
            val tipoDia11 = daoT.getTipoDiaById(tipoDia1)
            val tipoDia21 = daoT.getTipoDiaById(tipoDia2)
            val tipoDia31 = daoT.getTipoDiaById(tipoDia3)

            println("A guardar datos7")
            val tipoActNuevo = tipoDia1?.let { it1 ->
                TiposActividades(
                    nombreTipoAct = binding.editTextNombreTipoAct12.text.toString(),
                    tipoDiasGenerados1 = if (tipoDia11?.nombreTipoDia == "Seleccione una opción") null else tipoDia11?.nombreTipoDia,
                    tipoDiasGenerados2 = if (tipoDia21?.nombreTipoDia == "Seleccione una opción") null else tipoDia21?.nombreTipoDia,
                    tipoDiasGenerados3 = if (tipoDia31?.nombreTipoDia == "Seleccione una opción") null else tipoDia31?.nombreTipoDia,
                    requisitosDiasAct1 = if (propDias1 == 0) null else propDias1,
                    requisitosDiasAct2 = if (propDias2 == 0) null else propDias2,
                    requisitosDiasAct3 = if (propDias3 == 0) null else propDias3,
                    esGuardia = binding.checkBoxGuardia12.isChecked
                )
            }
            println(tipoActNuevo)
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
                            "Nombre tipo: ${tipoActNuevo?.nombreTipoAct}\n" +
                            "Dias generados 1: ${tipoActNuevo?.tipoDiasGenerados1}\n" +
                            "Dias generados 2: ${tipoActNuevo?.tipoDiasGenerados2}\n" +
                            "Dias generados 3: ${tipoActNuevo?.tipoDiasGenerados3}\n" +
                            "Requisitos dias 1: ${tipoActNuevo?.requisitosDiasAct1}\n" +
                            "Requisitos dias 2: ${tipoActNuevo?.requisitosDiasAct2}\n" +
                            "Requisitos dias 3: ${tipoActNuevo?.requisitosDiasAct3}\n" +
                            "Es guardia: ${tipoActNuevo?.esGuardia}\n"
                )
                println("A guardar datos11")
                //Controlamos la reaccion de pulsar aceptar

                construct.setPositiveButton("Aceptar") { dialog, wich ->
                    runBlocking {
                        if (tipoActNuevo != null) {
                            println("datos asignados")
                            //------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                            lifecycleScope.launch(Dispatchers.IO) {
                                println("A guardar datos guardando")
                                dao.insert(tipoActNuevo)
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