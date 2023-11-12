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
        val navController = findNavController()
        btn1.setOnClickListener {
            selMenuInt = 8
            println(6)
            val date: Date = selectedItem.fechaInActOk
            val datef: Date = selectedItem.fechaFiActOk
            val dateLong: Long = date.time
            val dateLong1: Long = datef.time

            // Crea un Bundle para pasar los datos
            val bundle = Bundle().apply {
                putInt("id", selectedItem.id)
                putString("nombreActOk", selectedItem.nombreActOk)
                putString("tipoActOk", selectedItem.tipoActOk)
                putString("tipoDiasActOk1", selectedItem.tipoDiasActOk1)
                putString("tipoDiasActOk2", selectedItem.tipoDiasActOk2)
                putString("tipoDiasActOk3", selectedItem.tipoDiasActOk3)
                putInt("diasGenActOk1", selectedItem.diasGenActOk1 ?: 0)
                putInt("diasGenActOk2", selectedItem.diasGenActOk2 ?: 0)
                putInt("diasGenActOk3", selectedItem.diasGenActOk3 ?: 0)
                putLong("fechaInActOk", dateLong)
                putLong("fechaFiActOk", dateLong1)
                putBoolean("esGuardiaOk", selectedItem.esGuardiaOk)
            }

            // Navega al siguiente fragmento con el Bundle
            cargarFragment(selMenuInt, navController, bundle)
            println("cargar frgament iniciado")
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

