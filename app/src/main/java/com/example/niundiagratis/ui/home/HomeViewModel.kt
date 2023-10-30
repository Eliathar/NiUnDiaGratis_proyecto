package com.example.niundiagratis.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.DiasGeneradosDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.data.db.GuardiasRealizadas
import com.example.niundiagratis.data.repositorio.ReposNiUnDiaGratis
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.LifecycleOwner
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.ui.home.HomeViewModel




class HomeViewModel(
    private val computoGlobalDao: ComputoGlobalDao,
    private val actividadesRealizadasDao: ActividadesRealizadasDao,
    private val guardiasRealizadasDao: GuardiasRealizadasDao
): ViewModel() {

    val listaComputoGlobal: LiveData<List<ComputoGlobal>> = computoGlobalDao.getAllComputoGlobal()


    fun obtenerActividades(): LiveData<List<ActividadesRealizadas>>{
        println("dentro obteneractividades homeviewmodel")
       return actividadesRealizadasDao.obtenerActividades()
   }
    fun obtenerGuardias(): LiveData<List<GuardiasRealizadas>>{
        println("dentro obtenerGUardias homeviewmodel")
        return guardiasRealizadasDao.getAllGuardiasRealizadas()
    }
    fun escribir(){
        println("dentro del viewmodel")
    }

}