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

class HomeViewModel(
    private val reposNiUnDiaGratis: ReposNiUnDiaGratis,
    private val actividadesRealizadasDao: ActividadesRealizadasDao,
    private val guardiasRealizadasDao: GuardiasRealizadasDao,
    private val tiposDiasDao: TiposDiasDao,
    private val diasGeneradosDao: DiasGeneradosDao,
    private val diasDisfrutadosDao: DiasDisfrutadosDao,
    private val computoGlobalDao: ComputoGlobalDao
    ) : ViewModel() {
    val listaComputoGlobal: LiveData<List<ComputoGlobal>> = computoGlobalDao.getAllComputoGlobal()

    suspend fun obtenerActividades(): LiveData<List<ActividadesRealizadas>>{
       return actividadesRealizadasDao.obtenerActividades()
   }
    suspend fun obtenerGuardias(): LiveData<List<GuardiasRealizadas>>{
        return guardiasRealizadasDao.getAllGuardiasRealizadas()
    }
}