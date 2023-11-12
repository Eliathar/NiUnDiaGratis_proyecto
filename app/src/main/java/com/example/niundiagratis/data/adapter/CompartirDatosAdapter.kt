package com.example.niundiagratis.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.viewholder.ViewHolderCompartir
import com.example.niundiagratis.data.viewmodel.ViewModelCompartir

class CompartirDatosAdapter(
    private val viewModel: ViewModelCompartir,
    private val datos: List<Any>
    ): RecyclerView.Adapter<ViewHolderCompartir>() {
    private var itemSelec: Int = -1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCompartir {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_act_real, parent, false)
        return ViewHolderCompartir(vista)
    }

    override fun onBindViewHolder(holder: ViewHolderCompartir, position: Int) {
        val dato = datos[position]
        holder.vista.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemSelec = pos
                viewModel.selectedData.value = datos[pos]
                notifyDataSetChanged()
            }
        }
        if (position == itemSelec) {
            holder.vista.setBackgroundColor(Color.BLUE)
        } else {
            holder.vista.setBackgroundColor(Color.TRANSPARENT)
        }
    }
    override fun getItemCount() = datos.size
}