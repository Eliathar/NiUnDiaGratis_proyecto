package com.example.niundiagratis.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.data.viewholder.TiposActividadesViewHolder

class TiposActividadesAdapter(
    val datos: List<TiposActividades>,
        private val onclickListener: (TiposActividades) -> Unit
    ): RecyclerView.Adapter<TiposActividadesViewHolder>()  {

        var selectedItem: TiposActividades? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiposActividadesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return TiposActividadesViewHolder(layoutInflater.inflate(R.layout.item_list_act_real, parent, false))
        }

        override fun onBindViewHolder(holder: TiposActividadesViewHolder, position: Int) {
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