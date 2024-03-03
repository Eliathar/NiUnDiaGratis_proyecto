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
import com.example.niundiagratis.data.adapter.PermisosAdapter
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModPermisoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private var selMenuInt = -1


class ModPermisoFragment : Fragment() {
    private lateinit var binding: FragmentModPermisoBinding
    private val viewModel: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)
        val dao = database.fDiasDisfrutadosDao()
        ViewModelSimple(dao)
    }
    private var listaDisfrutados: List<DiasDisfrutados> = emptyList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var selectedItem: DiasDisfrutados
    private lateinit var navController: NavController
    private lateinit var database: NiUnDiaGratisBBDD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentModPermisoBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnAceptar10.isEnabled = false
        navController = findNavController()

        //Obtenemos instancia del layoutmanager
        layoutManager = LinearLayoutManager(context)

        //Asignamos el layoutmanager al recyclerview
       binding.rVModPer10.layoutManager = layoutManager

        /*//Obtenemos el nombre de la base de datos

        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }*/

        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)

        initRecyclerView()
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        binding.btnAceptar10.setOnClickListener {
            selMenuInt = 11
            println(6)
            // Crea un Bundle para pasar los datos
            val bundle = Bundle().apply {
                putInt("id", selectedItem.id)
                putString("nombreBD", dbSeleccionada)
            }
            println("esta linea 1 la id es $id")
            // Navega al siguiente fragmento con el Bundle
            navController.navigate(R.id.action_modPermisoFragment_to_modPermisoSeleccionadoFragment, bundle)
        }
    }
    private fun initRecyclerView(){
        val fragment = this
        val launch = lifecycleScope.launch {
            viewModel.obtenerDiasDisLive().observe(viewLifecycleOwner) { actividades ->
                val listaDisfrutadosScope = actividades ?: emptyList()
                val actRealAdapter = PermisosAdapter(listaDisfrutadosScope, fragment::onItemSelected)
                binding.rVModPer10.adapter = actRealAdapter

                viewModel.actualizarListaActividades(listaDisfrutadosScope)
                //actualizamos listaActividades del fragment para controlar mensaje de no hay datos
                listaDisfrutados = listaDisfrutadosScope
                //Controlamos visibilidad del mensaje de no hay datos
                binding.txtVControl10.visibility = if (listaDisfrutados.isEmpty()) View.VISIBLE else View.GONE

            }
        }
        val manager = layoutManager
        val decoration = DividerItemDecoration(context, manager.orientation)
        binding.rVModPer10.layoutManager = manager
        binding.rVModPer10.adapter = PermisosAdapter(listaDisfrutados) {onItemSelected(it)}
        //Añadimos linea divisoria entre items
        binding.rVModPer10.addItemDecoration(decoration)
    }

    private fun onItemSelected(permiso: DiasDisfrutados){
        // Actualiza el elemento seleccionado en el adaptador
        (binding.rVModPer10.adapter as? PermisosAdapter)?.let { adapter ->
            val oldIndex = adapter.selectedItem?.let { adapter.datos.indexOf(it) }
            val newIndex = adapter.datos.indexOf(permiso)

            adapter.selectedItem = permiso

            oldIndex?.let { adapter.notifyItemChanged(it) }
            adapter.notifyItemChanged(newIndex)
        }
        binding.btnAceptar10.isEnabled = true
        selectedItem = permiso

    }
}