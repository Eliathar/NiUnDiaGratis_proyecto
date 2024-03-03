package com.example.niundiagratis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.data.adapter.TiposDiasAdapter
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.TiposDias
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModTipoDiaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
private var selMenuInt = -1
class ModTipoDiaFragment : Fragment() {
    lateinit var binding: FragmentModTipoDiaBinding
    private val viewModel: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        val dao = database.fTiposDiasDao()
        ViewModelSimple(dao)
    }
    private var listaTiposDias: List<TiposDias> = emptyList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var selectedItem: TiposDias
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
        // Inflamos el layout del fragment
        binding = FragmentModTipoDiaBinding.inflate(inflater, container, false)
        val view = binding.root
        //Controlamos visibilidad boton aceptar a false hasta que haya registro seleccionado
        binding.btnAceptar16.isEnabled = false

        navController = findNavController()

        //Obtenemos instancia del layoutmanager
        layoutManager = LinearLayoutManager(context)

        //Asignamos el layoutmanager al recyclerview
        binding.rVmodTiposDias16.layoutManager = layoutManager

        /*//Obtenemos el nombre de la base de datos
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }*/
        //Obtenemos instancia de la base de datos
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)

        initRecyclerView()

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        //TODO llenar lista de bbdd de actividades hechas

        binding.btnAceptar16.setOnClickListener {
            selMenuInt = 8
            // Crea un Bundle para pasar los datos
            val bundle = Bundle().apply {
                putString("id", selectedItem.nombreTipoDia)
                putString("nombreBD", dbSeleccionada)
            }
            // Navega al siguiente fragmento con el Bundle
            navController.navigate(R.id.action_modTipoDiaFragment_to_modTipoDiaSelecFragment, bundle)
            println("actividad seleccionada")
            println(id)
        }
    }
    private fun initRecyclerView(){
        val fragment = this
        val launch = lifecycleScope.launch {
            viewModel.obtenerTiposDias().observe(viewLifecycleOwner) { tiposDias ->
                val listaTiposDiasScope = tiposDias ?: emptyList()
                val tiposDiasAdapter = TiposDiasAdapter(listaTiposDiasScope, fragment::onItemSelected)
                binding.rVmodTiposDias16.adapter =  tiposDiasAdapter

                viewModel.actualizarListaTiposDias(listaTiposDiasScope)
                //actualizamos listaActividades del fragment para controlar mensaje de no hay datos
                listaTiposDias = listaTiposDiasScope
                //Controlamos visibilidad del mensaje de no hay datos
                binding.txtVControl16.visibility = if (listaTiposDias.isEmpty()) View.VISIBLE else View.GONE

            }
        }
        val manager = layoutManager
        val decoration = DividerItemDecoration(context, manager.orientation)
        binding.rVmodTiposDias16.layoutManager = manager
        binding.rVmodTiposDias16.adapter = TiposDiasAdapter(listaTiposDias) {onItemSelected(it)}
        //Añadimos linea divisoria entre items
        binding.rVmodTiposDias16.addItemDecoration(decoration)
    }

    private fun onItemSelected(tipoDia: TiposDias){
        // Actualiza el elemento seleccionado en el adaptador
        (binding.rVmodTiposDias16.adapter as? TiposDiasAdapter)?.let { adapter ->
            val oldIndex = adapter.selectedItem?.let { adapter.datos.indexOf(it) }
            val newIndex = adapter.datos.indexOf(tipoDia)

            adapter.selectedItem = tipoDia

            oldIndex?.let { adapter.notifyItemChanged(it) }
            adapter.notifyItemChanged(newIndex)
        }
        binding.btnAceptar16.isEnabled = true
        selectedItem = tipoDia

    }
}