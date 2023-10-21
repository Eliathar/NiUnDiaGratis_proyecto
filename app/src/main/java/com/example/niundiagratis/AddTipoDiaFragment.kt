package com.example.niundiagratis

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.niundiagratis.databinding.FragmentAddTipoDiaBinding

class AddTipoDiaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentAddTipoDiaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddTipoDiaBinding.inflate(inflater, container, false)
        val view = binding.root

        val spinner = binding.spinnerMax13

        //Configuramos los spinner
        //Spinner relativo a la proporcion de dias
        val spinnerItems = resources.getIntArray(com.example.niundiagratis.R.array.spinner_max_items).toList()
        val adapter = ArrayAdapter<Int>(requireContext(), R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        //val selecSpin = spinnerItems[spinner.selectedItemPosition]

        //TODO actualizar valores de base de datos para tipo de dias, simulado hasta implementar la bbdd
        val tipoDiasDB = obtenerTipoDiasBD()

        return view
    }
}