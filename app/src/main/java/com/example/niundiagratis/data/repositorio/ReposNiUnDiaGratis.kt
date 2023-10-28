package com.example.niundiagratis.data.repositorio

import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.DiasGeneradosDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.ComputoGlobal

class ReposNiUnDiaGratis(
    private val actividadesRealizadasDao: ActividadesRealizadasDao,
    private val computoGlobalDao: ComputoGlobalDao,
    private val diasDisfrutadosDao: DiasDisfrutadosDao,
    private val diasGeneradosDao: DiasGeneradosDao,
    private val guardiasRealizadasDao: GuardiasRealizadasDao,
    private val tiposDiasDao: TiposDiasDao,
    private val tiposActividadesDao: TiposActividadesDao
) {

    suspend fun obtenerComputoGlobal(): List<ComputoGlobal> {
        val resultados = mutableListOf<ComputoGlobal>()

        // Obtener todos los tipos de días de la tabla TiposDias
        val tiposDias = tiposDiasDao.getAllTiposDias()

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