package com.example.niundiagratis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.niundiagratis.databinding.FragmentAddPermisoBinding
import java.util.Date

// TODO: Rename parameter arguments, choose names that match

class AddPermisoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentAddPermisoBinding
    private lateinit var fechaInicio: Date
    private lateinit var fechaFin: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddPermisoBinding.inflate(inflater, container, false)
        val view = binding.root
        val btn1: TextView = view.findViewById(R.id.btnFechaIniPerm09)
        val btn2: TextView = view.findViewById(R.id.btnFechaFinPerm09)
        val spinner = binding.spinnerTipo09

        //Configuramos el spinner
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, obtenerTipoDiasBD())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //TODO actualizar valores de base de datos para tipo de dias, simulado hasta implementar la bbdd
        val tipoDiasDB = obtenerTipoDiasBD()

        btn1.setOnClickListener(){
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaInicio = fechaSelec
                btn1.text = "Inicio: ${formatearFecha(fechaInicio)}"
            }

        }
        btn2.setOnClickListener(){
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
}