package com.example.niundiagratis.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.niundiagratis.data.adapter.ItemActRealAdapter
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import kotlinx.coroutines.runBlocking
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.DatabaseActive
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelFactory
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentHomeBinding
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var actRealAdapter: ItemActRealAdapter
    private lateinit var computoGlobalDao: ComputoGlobalDao
    private lateinit var actividadesRealizadasDao: ActividadesRealizadasDao
    private lateinit var database: NiUnDiaGratisBBDD
    private lateinit var nombreBD: String
    private lateinit var daoT: ComputoGlobalDao

    //private lateinit var context: Context
    private val viewModelT: ViewModelSimple by lazy {
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        daoT = database.fComputoGlobalDao()
        ViewModelSimple(daoT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        //obtenemos instancia del layoutmanager
        val layoutManager1 = LinearLayoutManager(context)
        val layoutManager2 = LinearLayoutManager(context)
        val layoutManager3 = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, layoutManager1.orientation)

        //Asignamos los layoutmanager a los recyclerviews
        binding.rVActReal.layoutManager = layoutManager1
        binding.rVGuardias.layoutManager = layoutManager2
        binding.rVComputo.layoutManager = layoutManager3
        /*//Obtenemos la instancia de la base de datos y la abrimos para que sea editable
        runBlocking {
            withContext(Dispatchers.IO) {
                crearBBDD(requireContext()).also { database = it }
            }
        }*/
        //ToDO crear mensaje y control de mensaje para que solo aparezca en el inicio

        /*// Obtenemos una instancia de la base de datos seleccionada
        database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), dbSeleccionada)*/
        runBlocking {
            database = DatabaseActive.getDatabase(requireContext())
        }
        println("La base de datos es $database y el nombre es $dbSeleccionada")


        // Inicializamos DAOs
        computoGlobalDao = database.fComputoGlobalDao()
        actividadesRealizadasDao = database.fActividadesRealizadasDao()

        //Creamos una variable viewmodelfactory para pasarle los datos al viewmodel
        val factory = ViewModelFactory(computoGlobalDao, actividadesRealizadasDao)

        //Creamos el viewmodel
        val homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        //Funciones para obtener los datos del viewmodel para los recyclerviews
        runBlocking {
            homeViewModel.obtenerActividades().observe(viewLifecycleOwner) { actividades ->
                val listaActividades = actividades?.filter { !it.esGuardiaOk } ?: emptyList()
                val actRealAdapter = ItemActRealAdapter(listaActividades, 1)
                binding.rVActReal.adapter = actRealAdapter
                binding.rVActReal.addItemDecoration(decoration)
            }
            homeViewModel.obtenerGuardias().observe(viewLifecycleOwner) { actividades ->
                val listaGuardias = actividades?.filter { it.esGuardiaOk } ?: emptyList()
                val actGuardias = ItemActRealAdapter(listaGuardias, 2)
                binding.rVGuardias.adapter = actGuardias
                binding.rVGuardias.addItemDecoration(decoration)

            }

            homeViewModel.listaComputoGlobal.observe(viewLifecycleOwner) { datos ->
                val listaComputoGlobal = datos ?: emptyList()
                println(listaComputoGlobal.size)
                val computoGlobal = ItemActRealAdapter(listaComputoGlobal, 3)
                binding.rVComputo.adapter = computoGlobal
                binding.rVComputo.addItemDecoration(decoration)
            }

        }
        return view
    }
}
