package com.example.niundiagratis.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.ui.home.HomeViewModel




class HomeViewModel(
    private val computoGlobalDao: ComputoGlobalDao,
    private val actividadesRealizadasDao: ActividadesRealizadasDao
): ViewModel() {
    val listaComputoGlobal: LiveData<List<ComputoGlobal>> = computoGlobalDao.getAllComputoGlobal()
    fun obtenerActividades(): LiveData<List<ActividadesRealizadas>>{
       return actividadesRealizadasDao.obtenerActividades()
   }
    fun obtenerGuardias(): LiveData<List<ActividadesRealizadas>>{
        return actividadesRealizadasDao.obtenerGuardias()
    }

}