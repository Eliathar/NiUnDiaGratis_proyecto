package com.example.niundiagratis.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.ActividadesRealizadas



/*class ItemActRealAdapter(private val dataList: List<ActividadesRealizadas>) : RecyclerView.Adapter<ItemActReal>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemActReal {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_act_real, parent, false)
        return ItemActReal(itemView)
    }
    override fun onBindViewHolder(holder: ItemActReal, position: Int) {
        val item = dataList[position]
        holder.txtTipo.text = item.tipoActOk
        holder.txtNombre.text = item.nombreActOk
        holder.txtFecha.text = item.fechaInActOk.toString()
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
}*/
