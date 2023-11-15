package com.example.niundiagratis.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao

class ViewModelFactorySimple(
    private val dao: Any
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelSimple::class.java)) {
            @Suppress("UNCHECKED_CAST")
            /* Definimos que parametro se le pasa al viewmodel en funcion de desde donde se
            realiza la llamada, esto permite reutilizar este viewmodelfactory*/
            return when (dao) {
                is ActividadesRealizadasDao -> ViewModelSimple(dao as ActividadesRealizadasDao) as T
                is DiasDisfrutadosDao -> ViewModelSimple(dao as DiasDisfrutadosDao) as T
                else -> throw IllegalArgumentException("Unknown DAO class")
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
