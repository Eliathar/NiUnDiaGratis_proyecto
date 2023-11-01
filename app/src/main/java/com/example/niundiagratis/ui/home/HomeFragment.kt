package com.example.niundiagratis.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.MainActivity
import com.example.niundiagratis.R
import com.example.niundiagratis.data.adapter.ItemActRealAdapter
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.BBDDHandler.crearBBDD
import com.example.niundiagratis.data.repositorio.ReposNiUnDiaGratis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import androidx.lifecycle.LiveData
import com.example.niundiagratis.ui.home.HomeViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.db.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var actRealAdapter: ItemActRealAdapter
    private lateinit var computoGlobalDao: ComputoGlobalDao
    private lateinit var actividadesRealizadasDao: ActividadesRealizadasDao
    private lateinit var guardiasRealizadasDao: GuardiasRealizadasDao
    private lateinit var database: NiUnDiaGratisBBDD
    private lateinit var nombreBD: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Enlazamos los recycler views
        val rVActReal: RecyclerView = view.findViewById(R.id.rVActReal)
        val rVGuardias: RecyclerView = view.findViewById(R.id.rVGuardias)
        val rVComputo: RecyclerView = view.findViewById(R.id.rVComputo)

        //obtenemos instancia del layoutmanager
        val layoutManager1 = LinearLayoutManager(context)
        val layoutManager2 = LinearLayoutManager(context)
        val layoutManager3 = LinearLayoutManager(context)

        //Asignamos los layoutmanager a los recyclerviews
        rVActReal.layoutManager = layoutManager1
        rVGuardias.layoutManager = layoutManager2
        rVComputo.layoutManager = layoutManager3


        //Creacion e inicializacion de base de datos
        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = crearBBDD(requireContext()).toString()
            }
        }
        println(nombreBD)
        // Obtenemos una instancia de la base de datos seleccionada
        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD)
        // Inicializamos DAOs
        computoGlobalDao = database.fComputoGlobalDao()
        actividadesRealizadasDao = database.fActividadesRealizadasDao()
        guardiasRealizadasDao = database.fGuardiasRealizadasDao()
        //Creamos una variable viewmodelfactory para pasarle los datos al viewmodel
        val factory = ViewModelFactory(computoGlobalDao, actividadesRealizadasDao, guardiasRealizadasDao)
        //Creamos el viewmodel
        val homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        println("dentro de 0")
        homeViewModel.escribir()
        //Funciones para obtener los datos del viewmodel para ls recyclerviews
        lifecycleScope.launch {
            homeViewModel.obtenerActividades().observe(viewLifecycleOwner) { actividades ->
                val listaActividades = actividades ?: emptyList()
                println("dentro de 1")
                println(listaActividades.size)
                val actRealAdapter = ItemActRealAdapter(listaActividades, 1)
                println("dentro de 2")
                rVActReal.adapter = actRealAdapter
                println("dentro de 3")
            }
        }
        lifecycleScope.launch {
            homeViewModel.obtenerGuardias().observe(viewLifecycleOwner){guardias ->
                val listaGuardiasRealizadas = guardias ?: emptyList()
                println("dentro de 4")
                println(listaGuardiasRealizadas.size)
                val actGuardias = ItemActRealAdapter(listaGuardiasRealizadas, 2)
                rVGuardias.adapter = actGuardias
            }
        }
        lifecycleScope.launch {
            homeViewModel.listaComputoGlobal.observe(viewLifecycleOwner) { datos ->
                val listaComputoGlobal = datos ?: emptyList()
                println("dentro de 5")
                println(listaComputoGlobal.size)
                for (i in 0..<listaComputoGlobal.size){
                    println(listaComputoGlobal[i].toString())
                }
                val computoGlobal = ItemActRealAdapter(listaComputoGlobal, 3)
                println("dentro de 6")
                rVComputo.adapter = computoGlobal
                println("dentro de 7")
            }
        }
        return view
    }
}