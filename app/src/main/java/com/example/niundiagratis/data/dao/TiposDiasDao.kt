package com.example.niundiagratis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.TiposDias

//Declaramos como interfaz DAO
@Dao
interface TiposDiasDao {
    //Para insercion de nuevas entidades
    @Insert
    fun insert(tiposDias: TiposDias)

    //Devuelve una lista Live con todos las entidades
    @Query("SELECT * FROM tablaTiposDias")
    fun getAllTiposDias(): LiveData<List<TiposDias>>


    @Query("SELECT * FROM tablaTiposDias")
    fun getAllTiposDiasList(): List<TiposDias>
    @Query("SELECT nombreTipoDia FROM tablaTiposDias")
    fun getAllTiposDiasListNombres(): List<String>


    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaTiposDias WHERE nombreTipoDia = :id")
    fun getTipoDiaById(id: String): TiposDias?

    //Se actualiza una entidad
    @Update
    fun update(tiposDias: TiposDias)

    //Se elimina una entidad
    @Delete
    fun delete(tiposDias: TiposDias)
}