package com.example.niundiagratis.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.ActividadesRealizadas

//Declaramos como interfaz DAO
@Dao
interface ActividadesRealizadasDao {
    @Dao
    interface ActividadDao {
        //Para insercion de nuevas entidades
        @Insert
        suspend fun insert(actividadesRealizadasDao: ActividadesRealizadasDao)

        //Devuelve una lista con todos las entidades
        @Query("SELECT * FROM tablaActividadesRealizadas")
        suspend fun getAllActividades(): List<ActividadesRealizadas>

        //Devuelve la entidad seleccionada mediante el id
        @Query("SELECT * FROM tablaActividadesRealizadas WHERE id = :id")
        suspend fun getActividadById(id: Long): ActividadesRealizadas?

        //Se actualiza una entidad
        @Update
        suspend fun update(actividadesRealizadas: ActividadesRealizadas)

        //Se elimina una entidad
        @Delete
        suspend fun delete(actividadesRealizadas: ActividadesRealizadas)
    }
}