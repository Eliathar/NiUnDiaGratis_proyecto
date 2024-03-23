package com.example.niundiagratis.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.DiasFestivos
import com.example.niundiagratis.data.viewholder.FestivosViewHolder

class FestivosAdapter(
    val datos: List<DiasFestivos>,
    private val onclickListener: (DiasFestivos) -> Unit
): RecyclerView.Adapter<FestivosViewHolder>()  {

    var selectedItem: DiasFestivos? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FestivosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FestivosViewHolder(layoutInflater.inflate(R.layout.item_dias_festivos, parent, false))
    }

    override fun onBindViewHolder(holder: FestivosViewHolder, position: Int) {
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