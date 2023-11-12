package com.example.niundiagratis.data.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.databinding.ItemListActRealBinding

class SimpleViewHolder (view: View): RecyclerView.ViewHolder(view){
    val binding = ItemListActRealBinding.bind(view)
    fun render(actividadesRealizadasModel: ActividadesRealizadas, onclickListener:(ActividadesRealizadas)->Unit){
        binding.txtTipo.text = actividadesRealizadasModel.tipoActOk
        binding.txtNombre.text = actividadesRealizadasModel.nombreActOk
        binding.txtFecha.text = actividadesRealizadasModel.fechaInActOk.toString()
        itemView.setOnClickListener { onclickListener(actividadesRealizadasModel) }
    }
}