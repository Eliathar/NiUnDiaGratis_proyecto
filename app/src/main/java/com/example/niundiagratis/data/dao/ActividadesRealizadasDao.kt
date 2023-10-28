package com.example.niundiagratis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.ActividadesRealizadas

//Declaramos como interfaz DAO
@Dao
interface ActividadesRealizadasDao {
        //Para insercion de nuevas entidades
        @Insert
        fun insert(actividadesRealizadas: ActividadesRealizadas)

        //Devuelve una lista con todos las entidades
        @Query("SELECT * FROM tablaActividadesRealizadas")
        fun getAllActividades(): List<ActividadesRealizadas>

        //Devuelve la entidad seleccionada mediante el id
        @Query("SELECT * FROM tablaActividadesRealizadas WHERE id = :id")
        fun getActividadById(id: Long): ActividadesRealizadas?

        @Query("SELECT * FROM tablaActividadesRealizadas WHERE esGuardiaOk = 0")
        fun obtenerActividades(): LiveData<List<ActividadesRealizadas>>

        @Query("SELECT * FROM tablaActividadesRealizadas WHERE esGuardiaOk = 1")
        fun obtenerGuardias(): LiveData<List<ActividadesRealizadas>>

        //Se actualiza una entidad
        @Update
        fun update(actividadesRealizadas: ActividadesRealizadas)

        //Se elimina una entidad
        @Delete
        fun delete(actividadesRealizadas: ActividadesRealizadas)
}