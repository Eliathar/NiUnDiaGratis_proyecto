package com.example.niundiagratis.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.data.db.GuardiasRealizadas
import com.example.niundiagratis.viewholder.ItemActRealViewHolder


class ItemActRealAdapter(private val dataList: List<Any>, private val opcion: Int) : RecyclerView.Adapter<ItemActRealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemActRealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_act_real, parent, false)
        return ItemActRealViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ItemActRealViewHolder, position: Int) {
        val item = dataList[position]
        when (opcion){
            1 ->{
                if (item is ActividadesRealizadas) {
                    holder.txtTipo.text = item.tipoActOk
                    println("1 $holder.txtTipo.text")
                    holder.txtNombre.text = item.nombreActOk
                    println(holder.txtNombre.text)
                    holder.txtFecha.text = item.fechaInActOk.toString()
                    println(holder.txtFecha.text)
                }
            }
            2-> {
                if (item is GuardiasRealizadas) {
                    holder.txtTipo.text = item.tipoGuardiaOk
                    holder.txtNombre.text = null
                    holder.txtFecha.text = item.fechaGuar.toString()
                }
            }
            3->{
                println("en computoblogal3")
                if (item is ComputoGlobal) {
                    holder.txtTipo.text = item.tipoDiaGlobal
                    println(holder.txtTipo.text)
                    holder.txtNombre.text = if (item.maxGlobal.toString() == "null") "" else item.maxGlobal.toString()
                    holder.txtFecha.text = item.saldoGlobal.toString()
                }
            }
        }
    }
    override fun getItemCount(): Int {
        println(dataList.size)
        return dataList.size
    }
}
