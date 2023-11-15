package com.example.niundiagratis.data.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.databinding.ItemListActRealBinding

class TiposActividadesViewHolder (view: View): RecyclerView.ViewHolder(view){
    val binding = ItemListActRealBinding.bind(view)
    fun render(tiposActividadesModel: TiposActividades, onclickListener:(TiposActividades)->Unit){
        binding.txtTipo.text = null
        binding.txtNombre.text = tiposActividadesModel.nombreTipoAct
        binding.txtFecha.text = if(tiposActividadesModel.esGuardia) "Guardia" else null
        itemView.setOnClickListener { onclickListener(tiposActividadesModel) }
    }
}