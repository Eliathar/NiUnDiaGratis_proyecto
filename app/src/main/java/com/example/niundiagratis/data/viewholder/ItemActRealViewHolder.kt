package com.example.niundiagratis.data.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R

class ItemActRealViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val txtTipo: TextView = itemView.findViewById(R.id.txtTipo)
    val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
    val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
}