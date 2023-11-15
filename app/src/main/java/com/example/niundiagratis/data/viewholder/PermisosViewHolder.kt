package com.example.niundiagratis.data.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.databinding.ItemListActRealBinding

class PermisosViewHolder (view: View): RecyclerView.ViewHolder(view){
    val binding = ItemListActRealBinding.bind(view)
    fun render(permisosModel: DiasDisfrutados, onclickListener:(DiasDisfrutados)->Unit){
        binding.txtTipo.text = permisosModel.id.toString()
        binding.txtNombre.text = permisosModel.tipoDiaDis
        binding.txtFecha.text = permisosModel.fechaCon.toString()
        itemView.setOnClickListener { onclickListener(permisosModel) }
    }
}