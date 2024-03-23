package com.example.niundiagratis.data.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.data.db.DiasFestivos
import com.example.niundiagratis.databinding.ItemDiasFestivosBinding
import com.example.niundiagratis.formatearFecha

class FestivosViewHolder (view: View): RecyclerView.ViewHolder(view){
    val binding = ItemDiasFestivosBinding.bind(view)
    fun render(diasFestivosModel: DiasFestivos, onclickListener:(DiasFestivos)->Unit){
        binding.txtNombreFest.text = diasFestivosModel.nombreDia
        binding.txtFechaFest.text = formatearFecha(diasFestivosModel.fechaDia)
        itemView.setOnClickListener { onclickListener(diasFestivosModel) }
    }
}