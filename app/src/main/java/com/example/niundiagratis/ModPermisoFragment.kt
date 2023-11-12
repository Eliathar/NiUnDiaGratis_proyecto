package com.example.niundiagratis

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.adapter.CompartirDatosAdapter
import com.example.niundiagratis.data.adapter.ItemActRealAdapter
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.data.viewmodel.ViewModelFactorySimple
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.data.viewmodel.ViewModelCompartir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private var selMenuInt = -1


class ModPermisoFragment : Fragment() {
    private lateinit var nombreBD: String
    private lateinit var diasDisfrutadosDao: DiasDisfrutadosDao
    private var listaDisfrutados: List<DiasDisfrutados> = emptyList()
    private lateinit var rVModPer: RecyclerView


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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mod_permiso, container, false)
        rVModPer = view.findViewById(R.id.rVModPer)

        //Obtenemos instancia del layoutmanager
        val layoutManager = LinearLayoutManager(context)

        //Asignamos el layoutmanager al recyclerview
        rVModPer.layoutManager = layoutManager


        //Obtenemos el nombre de la base de datos

        runBlocking {
            withContext(Dispatchers.IO) {
                nombreBD = BBDDHandler.crearBBDD(requireContext()).toString()
            }
        }

        val database = NiUnDiaGratisBBDD.obtenerInstancia(requireContext(), nombreBD )

        //Obtenemos el DAO
        diasDisfrutadosDao = database.fDiasDisfrutadosDao()
        //Inicializamos el factory
        val factory = ViewModelFactorySimple(diasDisfrutadosDao)
        //Obtenemos instancia del viewmodel para llenar el recyclerView
        val viewModelSimple = ViewModelProvider(this, factory).get(ViewModelSimple::class.java)
        //Obtenemos el listado de dias disfrutados
        lifecycleScope.launch {
            viewModelSimple.obtenerDiasDis().observe(viewLifecycleOwner) { permisos ->
                listaDisfrutados = permisos ?: emptyList()
                println(listaDisfrutados.size)
                val actRealAdapter = ItemActRealAdapter(listaDisfrutados, 4)
                rVModPer.adapter = actRealAdapter
            }
        }
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        //Definimos el boton aceptar
        val btn1: Button = view.findViewById(R.id.btn_Aceptar_14)
        /* Lo desactivamos para que no se muestre en caso de estar la lista vcia, pues el control
        se realiza sobre si hay cambios en la lista, por lo que al inicio esta vacia,  no hay
        cambios y se mostraria */
        btn1.isEnabled = false

        //definimos el textview de control
        val txtV: TextView = view.findViewById(R.id.txtVControl)

        //Iniciamos proceso para la seleccion de registro mostrado
        val viewModelCompartir = ViewModelProvider(this).get(ViewModelCompartir::class.java)
        val adapter: CompartirDatosAdapter = CompartirDatosAdapter(viewModelCompartir, listaDisfrutados)
        rVModPer.adapter = adapter

        //Controlamos visibilidad de boton aceptar si hay datos
        viewModelCompartir.selectedData.observe(viewLifecycleOwner){datos ->
            btn1.isEnabled = datos !=null
        }
        //Controlamos mensaje de no hay datos
        if (listaDisfrutados.isEmpty()) {
            txtV.visibility = View.VISIBLE
        } else {
            txtV.visibility = View.GONE
        }

        btn1.setOnClickListener {
            selMenuInt = 11
            println(6)
            // Obtiene los datos del registro seleccionado
            val datos = viewModelCompartir.selectedData.value
            // Crea un Bundle para pasar los datos
            val bundle = Bundle().apply {
                if (datos is Parcelable) {
                    putParcelable("datos", datos)
                }
            }
            // Navega al siguiente fragmento con el Bundle
            cargarFragment(selMenuInt, navController, bundle)
        }
    }
}