package com.example.niundiagratis.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.viewmodel.ViewModelCompartir

class CompartirDatosAdapter(
    private val viewModel: ViewModelCompartir,
    private val datos: List<Any>
    ): RecyclerView.Adapter<CompartirDatosAdapter.ViewHolderCompartir>() {

    class ViewHolderCompartir(val vista: View) :RecyclerView.ViewHolder(vista)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCompartir {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_act_real, parent, false)
        return ViewHolderCompartir(vista)
    }

    override fun onBindViewHolder(holder: ViewHolderCompartir, position: Int) {
        val registro = datos[position]
        // Aquí puedes actualizar la vista de 'holder' con los valores de 'registro'
        holder.vista.setOnClickListener {
            viewModel.selectedData.value = registro
        }
    }
    override fun getItemCount() = datos.size
}