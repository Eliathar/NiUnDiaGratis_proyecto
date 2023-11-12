package com.example.niundiagratis.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.ModActividadFragment
import com.example.niundiagratis.ModPermisoFragment
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.viewholder.SimpleViewHolder
import com.example.niundiagratis.data.viewmodel.ViewModelSimple

class SimpleAdapter(
    val datos: List<ActividadesRealizadas>,
    private val onclickListener: (ActividadesRealizadas) -> Unit
): RecyclerView.Adapter<SimpleViewHolder>()  {

    var selectedItem: ActividadesRealizadas? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SimpleViewHolder(layoutInflater.inflate(R.layout.item_list_act_real, parent, false))
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
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