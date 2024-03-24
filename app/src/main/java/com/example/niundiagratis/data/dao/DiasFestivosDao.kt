package com.example.niundiagratis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.DiasFestivos
import java.util.Date

@Dao
interface DiasFestivosDao {

    //Para insercion de nuevas entidades
    @Insert
    fun insert(diasFestivos: DiasFestivos)

    //Devuelve una lista Live con todos las entidades
    @Query("SELECT * FROM tablaDiasFestivos")
    fun getAllDiasFestivos(): LiveData<List<DiasFestivos>>


    @Query("SELECT * FROM tablaDiasFestivos")
    fun getAllDiasFestivosList(): List<DiasFestivos>
    /*@Query("SELECT nombreDia FROM tablaDiasFestivos")
    fun getAllDiasFestivosListNombres(): List<String>*/


    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaDiasFestivos WHERE id = :id")
    fun getDiaFestivoById(id: Int): DiasFestivos?

    //Devuelve entidad seleccionada mediante fecha
    @Query("SELECT * FROM tablaDiasFestivos WHERE fechaDia= :fecha")
    fun getDiaFestivoByFecha(fecha: Date): DiasFestivos?

    //Se actualiza una entidad
    @Update
    fun update(diasFestivos: DiasFestivos)

    //Se elimina una entidad
   /* @Delete
    fun delete(diasFestivos: DiasFestivos)*/

}