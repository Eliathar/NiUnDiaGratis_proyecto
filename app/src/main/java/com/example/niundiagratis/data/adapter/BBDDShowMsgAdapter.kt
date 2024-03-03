package com.example.niundiagratis.data.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R

class BBDDShowMsgAdapter(
    private val nombresBBDD: List<String>,
    private val onDatabaseSelected: (String) -> Unit
): RecyclerView.Adapter<BBDDShowMsgAdapter.ViewHolder>(){

    var selectedItem: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bbdd_select_msg, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = nombresBBDD[position]
        holder.nombreTextView.text = item
        holder.itemView.setOnClickListener {
            // Invocamos la función onDatabaseSelected cuando se selecciona un elemento
            selectedItem = item
            onDatabaseSelected(item)
            notifyDataSetChanged()
        }
        // Cambia el color de fondo si el elemento está seleccionado
        if (item == selectedItem) {
            holder.itemView.setBackgroundColor(Color.BLUE)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun getItemCount(): Int = nombresBBDD.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreMsg)
    }
}