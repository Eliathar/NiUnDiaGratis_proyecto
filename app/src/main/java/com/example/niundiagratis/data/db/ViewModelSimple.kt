package com.example.niundiagratis.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.data.db.GuardiasRealizadas
import com.example.niundiagratis.ui.home.HomeViewModel




class ViewModelSimple(
    private val dao: Any
): ViewModel() {
    fun obtenerActividades(): LiveData<List<ActividadesRealizadas>> {
        println("dentro obteneractividades ViewModelSimple")
        if (dao is ActividadesRealizadasDao) {
            return dao.getAllActividades()
        } else {
            return MutableLiveData(emptyList())
        }
    }
    fun obtenerGuardias(): LiveData<List<GuardiasRealizadas>> {
        println("dentro obtenerGUardias ViewModelSimple")
        if (dao is GuardiasRealizadasDao) {
            return dao.getAllGuardiasRealizadas()
        }else {
            return MutableLiveData(emptyList())
        }
    }
    fun obtenerDiasDis(): LiveData<List<DiasDisfrutados>>{
        println("dentro obtener dias ViewModelSimple")
        if (dao is DiasDisfrutadosDao) {
            return dao.obtenerDiasDisfrutados()
        }else {
            return MutableLiveData(emptyList())
        }
    }
    fun escribir(){
        println("dentro del viewmodel")
    }

}