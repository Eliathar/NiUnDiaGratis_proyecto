package com.example.niundiagratis


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.niundiagratis.databinding.FragmentAddActividadBinding
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
interface OnMenuItemSelectedListener {
    fun onMenuItemSelected(item: MenuItem)
}
class AddActividadFragment : Fragment(), OnMenuItemSelectedListener {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentAddActividadBinding
    private var mainActivity: MainActivity? = null
    private lateinit var fechaInicio: Date
    private lateinit var fechaFin: Date


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        //Obtenemos fecha del calendario

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddActividadBinding.inflate(inflater, container, false)
        val view = binding.root

        val spinner = binding.spinnerTipoActividad06
        val btn1: Button = binding.btnFechaIni06
        val btn2: Button = binding.btnFechaFin06


        //Configuramos el spinner
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, obtenerTipoDiasBD())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //TODO actualizar valores de base de datos para tipo de dias, simulado hasta implementar la bbdd
        val tipoDiasDB = obtenerTipoDiasBD()
        btn1.setOnClickListener(){
            println("dentro de onclick, llamando a calendar")
            println("contexto ${requireContext()}")
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaInicio = fechaSelec
                btn1.text = "Inicio: ${formatearFecha(fechaInicio)}"
            }

        }
        btn2.setOnClickListener(){
            println("dentro de onclick, llamando a calendar")
            println("contexto ${requireContext()}")
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaFin = fechaSelec
                btn2.text = "Inicio: ${formatearFecha(fechaFin)}"
            }

        }


        //Agregamos los valores al adaptador
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemSel = spinner.selectedItem as String
                if (itemSel == "Ninguna selección"){
                    //Acciones si no hay seleccion
                }else {
                    //Acciones si hay seleccion
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
                //No se ha realizado ninguna seleccion
            }
        }
        return view
    }
    override fun onMenuItemSelected(item: MenuItem) {
        mainActivity?.onNavigationItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
            println("attached mainActivity $mainActivity")
        }
    }
    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }


}