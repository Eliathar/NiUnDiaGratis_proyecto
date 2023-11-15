package com.example.niundiagratis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.DiasDisfrutados

//Declaramos como interfaz DAO
@Dao
interface DiasDisfrutadosDao {
    //Para insercion de nuevas entidades
    @Insert
    fun insert(diasdisfrutados: DiasDisfrutados)

    //Para insertar varios registros
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objetos: List<DiasDisfrutados>)

    //Devuelve una lista con todos las entidades
    @Query("SELECT * FROM tablaDiasDisfrutados")
    fun obtenerDiasDisfrutadosLive(): LiveData<List<DiasDisfrutados>>

    @Query("SELECT * FROM tablaDiasDisfrutados")
    fun obtenerDiasDisfrutados(): List<DiasDisfrutados>

    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaDiasDisfrutados WHERE id = :id")
    fun getDiasdisfrutadosById(id: Int): DiasDisfrutados?

    @Query("SELECT COUNT(*) FROM tablaDiasDisfrutados WHERE tipoDiaDis = :tipoDiaGen")
    fun getTotalDiasDisfrutados(tipoDiaGen: String): Int

    //Se actualiza una entidad
    @Update
    fun update(diasdisfrutados: DiasDisfrutados)
    //Se elimina una entidad
    @Delete
    fun delete(diasdisfrutados: DiasDisfrutados)

}