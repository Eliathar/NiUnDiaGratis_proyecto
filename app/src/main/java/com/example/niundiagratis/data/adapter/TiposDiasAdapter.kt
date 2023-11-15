package com.example.niundiagratis.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.TiposDias
import com.example.niundiagratis.data.viewholder.TiposDiasViewHolder

class TiposDiasAdapter (
    val datos: List<TiposDias>,
    private val onclickListener: (TiposDias) -> Unit
    ): RecyclerView.Adapter<TiposDiasViewHolder>()  {

        var selectedItem: TiposDias? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiposDiasViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return TiposDiasViewHolder(layoutInflater.inflate(R.layout.item_list_act_real, parent, false))
        }

        override fun onBindViewHolder(holder: TiposDiasViewHolder, position: Int) {
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