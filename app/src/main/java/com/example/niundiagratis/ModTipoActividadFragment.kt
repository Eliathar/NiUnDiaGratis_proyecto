package com.example.niundiagratis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.adapter.SimpleAdapter
import com.example.niundiagratis.data.adapter.TiposActividadesAdapter
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModActividadBinding
import com.example.niundiagratis.databinding.FragmentModTipoActividadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
//TODO: controlar seleccion del listado y como pasarle los parametros
//TODO: implementar base de datos
private var selMenuInt = -1
class ModTipoActividadFragment : Fragment() {
    lateinit var binding: FragmentModTipoActividadBinding
    private val viewModel: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        val dao = database.fTiposActividadesDao()
        ViewModelSimple(dao)
    }
    private lateinit var nombreBD: String
    private var listaTiposActividades: List<TiposActividades> = emptyList()
    private lateinit var listaTiposActividadesLive: LiveData<List<TiposActividades>>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var selectedItem: TiposActividades
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentModTipoActividadBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnAceptar14.isEnabled = false
        navController = findNavController()

        //Obtenemos instancia del layoutmanager
        layoutManager = LinearLayoutManager(context)
        //Asignamos el layoutmanager al recyclerview
        binding.rVMod14.layoutManager = layoutManager

        //Obtenemos el nombre de la base de datos
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext())
            }
        }
        //Obtenemos instancia de la base de datos
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD )

        initRecyclerView()
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        val btn1: TextView = view.findViewById(R.id.btn_Aceptar_14)
        btn1.setOnClickListener {
            selMenuInt = 15
            println(6)
            // Crea un Bundle para pasar los datos
            val bundle = Bundle().apply {
                putString("id", selectedItem.nombreTipoAct)
                putString("nombreBD", nombreBD)
            }
            // Navega al siguiente fragmento con el Bundle
            navController.navigate(R.id.action_modTipoActividadFragment_to_modTipoActividadSelecFragment, bundle)
            println("actividad seleccionada")
            println(id)
        }
    }
    private fun initRecyclerView(){
        val fragment = this
        val launch = lifecycleScope.launch {
            viewModel.obtenerTiposActividadesLive().observe(viewLifecycleOwner) { actividades ->
                val listaTiposActividadesScope = actividades ?: emptyList()
                val actRealAdapter = TiposActividadesAdapter(listaTiposActividadesScope, fragment::onItemSelected)
                binding.rVMod14.adapter = actRealAdapter

                viewModel.actualizarListaActividades(listaTiposActividadesScope)
                //actualizamos listaActividades del fragment para controlar mensaje de no hay datos
                listaTiposActividades = listaTiposActividadesScope
                //Controlamos visibilidad del mensaje de no hay datos
                binding.txtVControl14.visibility = if (listaTiposActividades.isEmpty()) View.VISIBLE else View.GONE

            }
        }
        val manager = layoutManager
        val decoration = DividerItemDecoration(context, manager.orientation)
        binding.rVMod14.layoutManager = manager
        binding.rVMod14.adapter = TiposActividadesAdapter(listaTiposActividades) {onItemSelected(it)}
        //Añadimos linea divisoria entre items
        binding.rVMod14.addItemDecoration(decoration)
    }

    private fun onItemSelected(tipoAct: TiposActividades){
        // Actualiza el elemento seleccionado en el adaptador
        (binding.rVMod14.adapter as? TiposActividadesAdapter)?.let { adapter ->
            val oldIndex = adapter.selectedItem?.let { adapter.datos.indexOf(it) }
            val newIndex = adapter.datos.indexOf(tipoAct)

            adapter.selectedItem = tipoAct

            oldIndex?.let { adapter.notifyItemChanged(it) }
            adapter.notifyItemChanged(newIndex)
        }
        binding.btnAceptar14.isEnabled = true
        selectedItem = tipoAct

    }
}