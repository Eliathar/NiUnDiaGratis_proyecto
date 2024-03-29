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
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.TiposDias
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModTipoDiaSelecBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext



class ModTipoDiaSelecFragment : Fragment() {
    private lateinit var binding: FragmentModTipoDiaSelecBinding
    private val job = Job()
    private lateinit var dao: TiposDiasDao
    private lateinit var entidad: TiposDias
    private lateinit var navController: NavController
    private lateinit var spinnerItems: MutableList<Int>
    private lateinit var database: NiUnDiaGratisBBDD
    private lateinit var daoT: ComputoGlobalDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentModTipoDiaSelecBinding.inflate(inflater, container, false)
        val view = binding.root

        //Declaramos el bundle
        val bundle = this.arguments

        //Obtenemos valores del bundle
        val id = bundle?.getString("id").toString()


        //Obtenemos instancia de la base de datos
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        navController = findNavController()

        runBlocking {
            println(id)
            entidad = withContext(Dispatchers.IO) {
                //Obtenemos instancia del Dao
                dao = database.fTiposDiasDao()
                dao.getTipoDiaById(id)!!
            }

            //Asignamos los valores a los campos
            binding.editTextNombre17.setText(entidad.nombreTipoDia)
            daoT= database.fComputoGlobalDao()

        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        spinConfig()

        binding.btnGuardar17.setOnClickListener() {
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
            //Spinner relativo al maximo anual del tipo de dia
            spinnerItems = resources.getIntArray(com.example.niundiagratis.R.array.spinner_prop_items).toMutableList()
            spinnerItems.add(0, 0)
            val adapterProp = ArrayAdapter<Int>(requireContext(), R.layout.simple_spinner_item, spinnerItems)
            adapterProp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerMax17.adapter = adapterProp
            val posicionEntidad = spinnerItems.indexOf(entidad.maxDias)
            binding.spinnerMax17.setSelection(posicionEntidad)

            //Agregamos los valores al adaptador
            //Tipo de dia 1
            binding.spinnerMax17.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
            {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemSel = binding.spinnerMax17.selectedItem.toString()
                    if (itemSel == "Ninguna selección") {
                        //Acciones si no hay seleccion
                        binding.spinnerMax17.setSelection(0)
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
            val maxTipoDia = binding.spinnerMax17.selectedItem.toString().toInt()
            println("A guardar datos2 $maxTipoDia")
            val nombreTipoDia = binding.editTextNombre17.text
            println("A guardar datos3 $nombreTipoDia")

            //Asignamos los valores a una variable del tipo adecuado para guardar los datos
            println("A guardar datos")
            val tipoDiaNuevo = nombreTipoDia.let { it1 ->
                TiposDias(
                    nombreTipoDia = nombreTipoDia.toString(),
                    maxDias = maxTipoDia
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
                            "Nombre del tipo de dia: ${tipoDiaNuevo.nombreTipoDia}\n" +
                            "Máximo anual: ${tipoDiaNuevo.maxDias}"
                )
                //Controlamos la reaccion de pulsar aceptar
                construct.setPositiveButton("Aceptar") { dialog, wich ->
                    if (tipoDiaNuevo != null) {
                        println("datos asignados")
//------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                        lifecycleScope.launch(Dispatchers.IO) {
                            dao.update(tipoDiaNuevo)
                            val computoModificar = daoT.getComputoGlobalByTipo(nombreTipoDia.toString())

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



                            BBDDHandler.actualizarComputoGlobal(database)
                        }
//------------------------------------Fin hilo secundario-------------------------------------------
                        println("datos guardados?")
                    }
                    //------Cargamos el fragment home al guardar los datos en la base de datos----------
                    navController.navigate(com.example.niundiagratis.R.id.nav_home)
                }
                construct.setNegativeButton("Cancelar", null)
                construct.show()
            }
        }
//-----------------------------------Fin hilo secundario--------------------------------------------

//-----------------------------------Fin boton calcular---------------------------------------------
    }
}
