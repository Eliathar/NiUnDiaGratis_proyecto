package com.example.niundiagratis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.GuardiasRealizadas

//Declaramos como interfaz DAO
@Dao
interface GuardiasRealizadasDao {
    //Para insercion de nuevas entidades
    @Insert
    fun insert(guardiaRealizada: GuardiasRealizadas)

    //Devuelve una lista con todos las entidades
    @Query("SELECT * FROM tablaGuardiasRealizadas")
    fun getAllGuardiasRealizadas():LiveData<List<GuardiasRealizadas>>

    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaGuardiasRealizadas WHERE idGuarOk = :id")
    fun getGuardiaRealizadaById(id: Long): GuardiasRealizadas?

    //Se actualiza una entidad
    @Update
    fun update(guardiaRealizada: GuardiasRealizadas)

    //Se elimina una entidad
    @Delete
    fun delete(guardiaRealizada: GuardiasRealizadas)
}