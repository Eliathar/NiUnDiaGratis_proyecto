package com.example.niundiagratis.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.db.GuardiasRealizadas


class ViewModelSimple(
    private val dao: Any
): ViewModel() {
    //Almacenamos los datos seleccionados
    private val _selectedData = MutableLiveData<Any>()
    private val _listaActividades = MutableLiveData<List<Any>>()
    //Livedata visible
    val selectedData: LiveData<Any> get() = _selectedData
    val listaActividades: LiveData<List<Any>> get() = _listaActividades

    fun obtenerActividades(): LiveData<List<ActividadesRealizadas>> {
        println("dentro obteneractividades ViewModelSimple $dao")
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
    fun setSelectedData(data: Any){
        _selectedData.value = data
    }
    fun actualizarListaActividades(nuevasActividades: List<Any>) {
        _listaActividades.value = nuevasActividades
    }
    fun onItemSelected(position: Int){
        //Controlar seleccion items
        _selectedData.value = _listaActividades.value?.get(position)
    }

}