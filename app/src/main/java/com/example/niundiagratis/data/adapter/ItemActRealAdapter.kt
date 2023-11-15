package com.example.niundiagratis.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.R
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.viewholder.ItemActRealViewHolder
import com.example.niundiagratis.formatearFecha


class ItemActRealAdapter(
    private val dataList: List<Any>,
    private val opcion: Int
) : RecyclerView.Adapter<ItemActRealViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemActRealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_act_real, parent, false)
        return ItemActRealViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ItemActRealViewHolder, position: Int) {
        val item = dataList[position]
        when (opcion){
            1 ->{
                if (item is ActividadesRealizadas && !item.esGuardiaOk) {
                    println("Actividad localizada ${item.nombreActOk}")
                    holder.txtTipo.text = item.tipoActOk
                    holder.txtNombre.text = item.nombreActOk
                    holder.txtFecha.text = formatearFecha(item.fechaInActOk)
                }
            }
            2-> {
                if (item is ActividadesRealizadas && item.esGuardiaOk) {
                    println("Guardia localizada ${item.nombreActOk}")
                    holder.txtTipo.text = item.tipoActOk
                    holder.txtNombre.text = item.nombreActOk
                    holder.txtFecha.text = formatearFecha(item.fechaInActOk)
                }
            }
            3->{
                println("en computoblogal3")
                if (item is ComputoGlobal) {
                    holder.txtTipo.text = item.tipoDiaGlobal
                    println("Computo global localizad ${item.tipoDiaGlobal}")
                    println(holder.txtTipo.text)
                    holder.txtNombre.text = if (item.maxGlobal.toString() == "null") "" else item.maxGlobal.toString()
                    holder.txtFecha.text = item.saldoGlobal.toString()
                }
            }
            4->{
                println("en computoblogal3")
                if (item is DiasDisfrutados) {
                    holder.txtTipo.text = item.id.toString()
                    println(holder.txtTipo.text)
                    holder.txtNombre.text = item.tipoDiaDis
                    holder.txtFecha.text = item.fechaCon.toString()
                }
            }
        }
    }
    override fun getItemCount(): Int {
        println(dataList.size)
        return dataList.size
    }
}
