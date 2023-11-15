package com.example.niundiagratis.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.viewholder.PermisosViewHolder

class PermisosAdapter(
    val datos: List<DiasDisfrutados>,
    private val onclickListener: (DiasDisfrutados) -> Unit
): RecyclerView.Adapter<PermisosViewHolder>()  {

    var selectedItem: DiasDisfrutados? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermisosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PermisosViewHolder(layoutInflater.inflate(R.layout.item_list_act_real, parent, false))
    }

    override fun onBindViewHolder(holder: PermisosViewHolder, position: Int) {
        val item = datos[position]
        holder.render(item, onclickListener)
        // Cambia el color de fondo si el elemento está seleccionado
        if (item == selectedItem) {
            holder.itemView.setBackgroundColor(Color.BLUE)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

    }
    override fun getItemCount(): Int = datos.size
}