package com.example.niundiagratis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.util.Date

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ModActividadSeleccionadaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var fechaInicio: Date
    private lateinit var fechaFin: Date

    private var selMenuInt = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_actividad_seleccionada, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val btn1: TextView = view.findViewById(R.id.btnFechaIni08)
        val btn2: TextView = view.findViewById(R.id.btnFechaFin08)
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
    }
}