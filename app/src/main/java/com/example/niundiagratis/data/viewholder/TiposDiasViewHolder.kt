package com.example.niundiagratis.data.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.TiposDias
import com.example.niundiagratis.databinding.ItemListActRealBinding

class TiposDiasViewHolder (view: View): RecyclerView.ViewHolder(view){
    val binding = ItemListActRealBinding.bind(view)
    fun render(tiposDiasModel: TiposDias, onclickListener:(TiposDias)->Unit){
        binding.txtTipo.text = tiposDiasModel.nombreTipoDia
        binding.txtNombre.text = null
        binding.txtFecha.text = tiposDiasModel.maxDias.toString()
        itemView.setOnClickListener { onclickListener(tiposDiasModel) }
    }
}