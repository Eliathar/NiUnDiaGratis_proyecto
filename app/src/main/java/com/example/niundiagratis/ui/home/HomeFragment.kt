package com.example.niundiagratis.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.adapter.ItemActRealAdapter
import com.example.niundiagratis.data.db.BBDDHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var actRealAdapter: ItemActRealAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

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

        runBlocking {
            homeViewModel.obtenerActividades().observe(viewLifecycleOwner){
                    actividades -> val listaActividades = actividades ?: emptyList()
                val actRealAdapter = ItemActRealAdapter(listaActividades, 1)
                rVActReal.adapter = actRealAdapter
            }
            homeViewModel.obtenerGuardias().observe(viewLifecycleOwner){
                    guardias -> val listaGuardiasRealizadas = guardias ?: emptyList()
                val actGuardias = ItemActRealAdapter(listaGuardiasRealizadas, 2)
                rVGuardias.adapter = actGuardias
            }
            homeViewModel.listaComputoGlobal.observe(viewLifecycleOwner) { datos ->
                val listaComputoGlobal = datos ?: emptyList()
                val computoGlobal = ItemActRealAdapter(listaComputoGlobal, 3)
                rVComputo.adapter = computoGlobal
            }
        }
        return view
    }
}