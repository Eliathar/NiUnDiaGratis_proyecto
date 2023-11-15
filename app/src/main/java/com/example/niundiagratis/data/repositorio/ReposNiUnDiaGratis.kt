package com.example.niundiagratis.data.repositorio

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.DiasGeneradosDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.ActividadesRealizadas
import com.example.niundiagratis.data.db.ComputoGlobal
import com.example.niundiagratis.data.db.GuardiasRealizadas
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD

class ReposNiUnDiaGratis(
    private val database: NiUnDiaGratisBBDD,
    private val actividadesRealizadasDao: ActividadesRealizadasDao,
    private val computoGlobalDao: ComputoGlobalDao,
    private val diasDisfrutadosDao: DiasDisfrutadosDao,
    private val diasGeneradosDao: DiasGeneradosDao,
    private val guardiasRealizadasDao: GuardiasRealizadasDao,
    private val tiposDiasDao: TiposDiasDao,
    private val tiposActividadesDao: TiposActividadesDao
) {

    // Funciones para obtener datos de la base de datos

    fun obtenerActividades(): LiveData<List<ActividadesRealizadas>> {
        return actividadesRealizadasDao.obtenerActividades()
    }

    fun getAllGuardiasRealizadas(): LiveData<List<GuardiasRealizadas>>{
        return guardiasRealizadasDao.getAllGuardiasRealizadas()
    }
    suspend fun obtenerComputoGlobal(): List<ComputoGlobal> {

        val resultados = mutableListOf<ComputoGlobal>()

        // Obtener todos los tipos de días de la tabla TiposDias
        val tiposDias = tiposDiasDao.getAllTiposDiasList()

        for (tipoDia in tiposDias) {
            val nombreTipoDia = tipoDia.nombreTipoDia
            val maxGlobal = tipoDia.maxDias

            // Obtener el número de días generados para el tipo de día
            val genGlobal = diasGeneradosDao.getTotalDiasGenerados(nombreTipoDia)

            // Obtener el número de días consumidos para el tipo de día
            val conGlobal = diasDisfrutadosDao.getTotalDiasDisfrutados(nombreTipoDia)

            val saldoGlobal = genGlobal - conGlobal

            resultados.add(ComputoGlobal(0, nombreTipoDia, maxGlobal ?: 0, genGlobal, conGlobal, saldoGlobal))
        }

        return resultados
    }
    //TODO en la siguiente funcion actualizar todos los campos y
    // tablas necesarios al agregar o modificar informacion de la bbdd
    suspend fun actualizarTodo(){

    }

}