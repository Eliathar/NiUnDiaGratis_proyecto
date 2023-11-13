package com.example.niundiagratis

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.adapter.CompartirDatosAdapter
import com.example.niundiagratis.data.adapter.ItemActRealAdapter
import com.example.niundiagratis.data.adapter.SimpleAdapter
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelFactorySimple
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.data.viewmodel.ViewModelCompartir
import com.example.niundiagratis.data.viewmodel.ViewModelFactory
import com.example.niundiagratis.databinding.FragmentModActividadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private var selMenuInt = -1
class ModActividadFragment : Fragment() {
    lateinit var binding: FragmentModActividadBinding
    private val viewModel: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        val dao = database.fActividadesRealizadasDao()
        ViewModelSimple(dao)
    }
    private lateinit var nombreBD: String
    private lateinit var rVmodAct: RecyclerView
    private lateinit var btn1: Button
    private var listaActividades: List<ActividadesRealizadas> = emptyList()
    private lateinit var txtV: TextView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var selectedItem: ActividadesRealizadas
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflamos el layout del fragment
        binding = FragmentModActividadBinding.inflate(inflater, container, false)
        val view = binding.root
        //Obtenemos la referencia al boton aceptar
        btn1= binding.btnAceptar07

        btn1.isEnabled = false

        navController = findNavController()



        //Enlazamos el recycler view
        rVmodAct = binding.rVModAct
        //Obtenemos instancia del layoutmanager
        layoutManager = LinearLayoutManager(context)
        //Asignamos el layoutmanager al recyclerview
        rVmodAct.layoutManager = layoutManager

        //Definimos el textview de control de que no hay datos
        txtV = binding.txtVControl07

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
        btn1.setOnClickListener {
            selMenuInt = 8
            // Crea un Bundle para pasar los datos
            val bundle = Bundle().apply {
                putInt("id", selectedItem.id)
                putString("nombreBD", nombreBD)
            }
            // Navega al siguiente fragmento con el Bundle
            navController.navigate(R.id.action_modActividadFragment_to_modActividadSeleccionadaFragment, bundle)
            println("actividad seleccionada")
            println(id)
        }
    }

    private fun initRecyclerView(){
        val fragment = this
        val launch = lifecycleScope.launch {
            viewModel.obtenerActividades().observe(viewLifecycleOwner) { actividades ->
                val listaActividadesScope = actividades ?: emptyList()
                val actRealAdapter = SimpleAdapter(listaActividadesScope, fragment::onItemSelected)
                rVmodAct.adapter = actRealAdapter
                viewModel.actualizarListaActividades(listaActividadesScope)
                //actualizamos listaActividades del fragment para controlar mensaje de no hay datos
                listaActividades = listaActividadesScope
                //Controlamos visibilidad del mensaje de no hay datos
                txtV.visibility = if (listaActividades.isEmpty()) View.VISIBLE else View.GONE

            }
        }
        val manager = layoutManager
        val decoration = DividerItemDecoration(context, manager.orientation)
        binding.rVModAct.layoutManager = manager
        binding.rVModAct.adapter = SimpleAdapter(listaActividades) {onItemSelected(it)}
        //Añadimos linea divisoria entre items
        binding.rVModAct.addItemDecoration(decoration)
    }

    private fun onItemSelected(actReal: ActividadesRealizadas){
        // Actualiza el elemento seleccionado en el adaptador
        (rVmodAct.adapter as? SimpleAdapter)?.let { adapter ->
            val oldIndex = adapter.selectedItem?.let { adapter.datos.indexOf(it) }
            val newIndex = adapter.datos.indexOf(actReal)

            adapter.selectedItem = actReal

            oldIndex?.let { adapter.notifyItemChanged(it) }
            adapter.notifyItemChanged(newIndex)
        }
        btn1.isEnabled = true
        selectedItem = actReal

    }

}

