package com.example.niundiagratis

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.niundiagratis.databinding.FragmentAddTipoActividadBinding


class AddTipoActividadFragment : Fragment() {
    private lateinit var binding: FragmentAddTipoActividadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddTipoActividadBinding.inflate(inflater, container, false)
        val view = binding.root

        val spinnerProp = binding.spinnerProporcion12
        val spinner1 = binding.spinnerTipo1Dia12
        val spinner2 = binding.spinnerTipo2Dia12
        val spinner3 = binding.spinnerTipo3Dia12

        //Configuramos los spinner
        //Spinners relativos al tipo de dia, base de datos
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item, obtenerTipoDiasBD())
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter
        spinner2.adapter = adapter
        spinner3.adapter = adapter

        //Spinner relativo a la proporcion de dias
        val spinnerItems = resources.getIntArray(com.example.niundiagratis.R.array.spinner_prop_items).toList()
        val adapterProp = ArrayAdapter<Int>(requireContext(), R.layout.simple_spinner_item, spinnerItems)
        adapterProp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinnerProp.adapter = adapterProp
        val selecSpinProp = spinnerItems[spinnerProp.selectedItemPosition]

        //TODO actualizar valores de base de datos para tipo de dias, simulado hasta implementar la bbdd
        val tipoDiasDB = obtenerTipoDiasBD()

        //Agregamos los valores al adaptador desde base de datos
        spinner1.onItemSelectedListener = onItemSelectedListener
        spinner2.onItemSelectedListener = onItemSelectedListener
        spinner3.onItemSelectedListener = onItemSelectedListener
        return view
    }
    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            // Obtener el spinner
            val spinner = parent as Spinner

            // Obtener el valor seleccionado del spinner
            val itemSel = spinner.selectedItem as String

            // Si no hay selección, no hacer nada
            if (itemSel == "Ninguna selección") {
                return
            }

            // Acciones si hay selección
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }
}