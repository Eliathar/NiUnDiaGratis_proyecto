package com.example.niundiagratis.data.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.ui.home.HomeViewModel

class ViewModelFactory (
    private val computoGlobalDao: ComputoGlobalDao,
    private val actividadesRealizadasDao: ActividadesRealizadasDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(computoGlobalDao, actividadesRealizadasDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
