package com.example.niundiagratis.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.DiasDisfrutados
import com.example.niundiagratis.data.db.GuardiasRealizadas
import com.example.niundiagratis.data.db.TiposActividades
import com.example.niundiagratis.data.db.TiposDias


class ViewModelSimple(
    private val dao: Any
): ViewModel() {
    //Almacenamos los datos seleccionados
    private val _selectedData = MutableLiveData<Any>()
    private val _listaActividades = MutableLiveData<List<Any>>()
    private val _listaTiposDias = MutableLiveData<List<Any>>()
    //Livedata visible
    val selectedData: LiveData<Any> get() = _selectedData
    val listaActividades: LiveData<List<Any>> get() = _listaActividades
    val listaTiposDias: LiveData<List<Any>> get() = _listaTiposDias

    fun obtenerActividades(): LiveData<List<ActividadesRealizadas>> {
        if (dao is ActividadesRealizadasDao) {
            return dao.getAllActividades()
        } else {
            return MutableLiveData(emptyList())
        }
    }
//Dos tipos de obtenertiposdias, pues ambas versiones de las listas son necesarias------------------
    fun obtenerTiposDias(): LiveData<List<TiposDias>>{
        if (dao is TiposDiasDao) {
            return dao.getAllTiposDias()
        } else {
            return MutableLiveData(emptyList())
        }
    }
    fun obtenerTiposDiasList(): List<TiposDias>{
        if (dao is TiposDiasDao) {
            return dao.getAllTiposDiasList()
        } else throw IllegalArgumentException("DAO no es una instancia de TiposDiasDao")

//--------------------------------------------------------------------------------------------------
    }
    fun obtenerGuardias(): LiveData<List<GuardiasRealizadas>> {
        if (dao is GuardiasRealizadasDao) {
            return dao.getAllGuardiasRealizadas()
        }else {
            return MutableLiveData(emptyList())
        }
    }
    fun obtenerDiasDisLive(): LiveData<List<DiasDisfrutados>>{
        if (dao is DiasDisfrutadosDao) {
            return dao.obtenerDiasDisfrutadosLive()
        }else {
            return MutableLiveData(emptyList())
        }
    }
    fun obtenerDiasDis(): List<DiasDisfrutados>{
        if (dao is DiasDisfrutadosDao) {
            return dao.obtenerDiasDisfrutados()
        }else throw IllegalArgumentException("DAO no es una instancia de DiasDisfrutados")
    }
    fun obtenerTiposActividadesLive(): LiveData<List<TiposActividades>>{
        if (dao is TiposActividadesDao) {
            return dao.getAllTiposActividadesLive()
        } else throw IllegalArgumentException("DAO no es una instancia de TiposDiasActividadesDao")
    }
    fun obtenerTiposActividades(): List<TiposActividades>{
        if (dao is TiposActividadesDao) {
            return dao.getAllTiposActividades()
        } else throw IllegalArgumentException("DAO no es una instancia de TiposDiasActividadesDao")
    }

    fun actualizarListaTiposDias(nuevosTiposDias: List<Any>) {
        _listaTiposDias.value = nuevosTiposDias
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