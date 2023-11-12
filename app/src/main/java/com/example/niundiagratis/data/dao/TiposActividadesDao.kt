package com.example.niundiagratis.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.TiposActividades

//Declaramos como interfaz DAO
@Dao
interface TiposActividadesDao {

    //Para insercion de nuevas entidades
    @Insert
    fun insert(tipoActividad: TiposActividades)

    //Devuelve una lista con todos las entidades
    @Query("SELECT * FROM tablaTiposActividades")
    fun getAllTiposActividades(): List<TiposActividades>

    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaTiposActividades WHERE nombreTipoAct = :nombre")
    fun getTipoActividadByNombre(nombre: String): TiposActividades?

    //Se actualiza una entidad
    @Update
    fun update(tipoActividad: TiposActividades)

    //Se elimina una entidad
    @Delete
    fun delete(tipoActividad: TiposActividades)
}