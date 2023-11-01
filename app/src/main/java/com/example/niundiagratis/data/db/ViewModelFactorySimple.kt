package com.example.niundiagratis.data.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao


/*

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Dao
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.ui.home.HomeViewModel

class ViewModelFactorySimple<T : ViewModel>(
    private val dao: Dao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelSimple::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelSimple(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/
class ViewModelFactorySimple(
    private val dao: Any
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelSimple::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return when (dao) {
                is ActividadesRealizadasDao -> ViewModelSimple(dao as ActividadesRealizadasDao) as T
                is GuardiasRealizadasDao -> ViewModelSimple(dao as GuardiasRealizadasDao) as T
                is DiasDisfrutadosDao -> ViewModelSimple(dao as DiasDisfrutadosDao) as T
                else -> throw IllegalArgumentException("Unknown DAO class")
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
