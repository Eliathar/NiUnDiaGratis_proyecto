package com.example.niundiagratis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.DatabaseActive.databaseAct
import com.example.niundiagratis.data.adapter.FestivosAdapter
import com.example.niundiagratis.data.db.DiasFestivos
import com.example.niundiagratis.data.viewmodel.ViewModelSimple
import com.example.niundiagratis.databinding.FragmentModFestivoBinding
import kotlinx.coroutines.launch

private var selMenuInt = -1
class ModFestivoFragment : Fragment() {
    lateinit var binding: FragmentModFestivoBinding
    private val viewModel: ViewModelSimple by lazy {
        val dao =  databaseAct!!.fDiasFestivosDao()
        ViewModelSimple(dao)
    }
    private var listaFestivos: List<DiasFestivos> = emptyList()
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var selectedItem: DiasFestivos
    private lateinit var navController: NavController



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentModFestivoBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnAceptar20.isEnabled = false
        navController = findNavController()
        //Obtenemos instancia del layoutmanager
        layoutManager = LinearLayoutManager(context)
        //Asignamos el layoutmanager al recyclerview
        binding.rVModFest.layoutManager = layoutManager
        initRecyclerView()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAceptar20.setOnClickListener {
            selMenuInt = 8
            // Crea un Bundle para pasar los datos
            val bundle = Bundle().apply {
                putInt("id", selectedItem.id)
                putString("nombreBD", dbSeleccionada)
            }
            // Navega al siguiente fragmento con el Bundle
            navController.navigate(R.id.action_modFestivoFragment_to_modFestivoSeleccionadoFragment, bundle)
        }
    }
    private fun initRecyclerView(){
        val fragment = this
        lifecycleScope.launch {
            viewModel.obtenerDiasFestivos().observe(viewLifecycleOwner) { festivos ->
                val listaFestivosScope = festivos ?: emptyList()
                val festivosAdapter = FestivosAdapter(listaFestivosScope, fragment::onItemSelected)
                binding.rVModFest.adapter = festivosAdapter
                viewModel.actualizarListaActividades(listaFestivosScope)
                //actualizamos listaActividades del fragment para controlar mensaje de no hay datos
                listaFestivos = listaFestivosScope
                //Controlamos visibilidad del mensaje de no hay datos
                binding.txtVControl20.visibility = if (listaFestivos.isEmpty()) View.VISIBLE else View.GONE

            }
        }
        val manager = layoutManager
        val decoration = DividerItemDecoration(context, manager.orientation)
        binding.rVModFest.layoutManager = manager
        binding.rVModFest.adapter = FestivosAdapter(listaFestivos) {onItemSelected(it)}
        //Añadimos linea divisoria entre items
        binding.rVModFest.addItemDecoration(decoration)
    }
    private fun onItemSelected(festivo: DiasFestivos){
        // Actualiza el elemento seleccionado en el adaptador
        (binding.rVModFest.adapter as? FestivosAdapter)?.let { adapter ->
            val oldIndex = adapter.selectedItem?.let { adapter.datos.indexOf(it) }
            val newIndex = adapter.datos.indexOf(festivo)

            adapter.selectedItem = festivo

            oldIndex?.let { adapter.notifyItemChanged(it) }
            adapter.notifyItemChanged(newIndex)
        }
        binding.btnAceptar20.isEnabled = true
        selectedItem = festivo
    }


}