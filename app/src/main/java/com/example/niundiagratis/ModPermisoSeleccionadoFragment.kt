package com.example.niundiagratis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.util.Date

// TODO: Rename parameter arguments, choose names that match

class ModPermisoSeleccionadoFragment : Fragment() {
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

        return inflater.inflate(R.layout.fragment_mod_permiso_seleccionado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val btn1: TextView = view.findViewById(R.id.btnFechaIni11)
        val btn2: TextView = view.findViewById(R.id.btnFechaFin11)
        btn1.setOnClickListener(){
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaInicio = fechaSelec
                /*val dateFormatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.getDefault())
                val fechaFormato = dateFormatter.format(fechaInicio)*/
                btn1.text = "Inicio: ${formatearFecha(fechaInicio)}"
            }

        }
        btn2.setOnClickListener(){
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaFin = fechaSelec
                /*val dateFormatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.getDefault())
                val fechaFormato = dateFormatter.format(fechaInicio)*/
                btn2.text = "Inicio: ${formatearFecha(fechaFin)}"
            }

        }
    }
}