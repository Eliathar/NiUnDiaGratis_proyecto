package com.example.niundiagratis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
//TODO: controlar seleccion del listado y como pasarle los parametros
//TODO: implementar base de datos
private var selMenuInt = -1
class ModTipoActividadFragment : Fragment() {
    // TODO: Rename and change types of parameters

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
        return inflater.inflate(R.layout.fragment_mod_tipo_actividad, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        //TODO llenar lista de bbdd de actividades hechas

        val btn1: TextView = view.findViewById(R.id.btn_Aceptar_14)
        btn1.setOnClickListener {
            selMenuInt = 15
            println(6)
            cargarFragment(selMenuInt, navController)
        }
    }
}