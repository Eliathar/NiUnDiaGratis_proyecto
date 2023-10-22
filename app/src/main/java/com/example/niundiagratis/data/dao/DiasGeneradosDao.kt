package com.example.niundiagratis.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.DiasGenerados

//Declaramos como interfaz DAO
@Dao
interface DiasGeneradosDao {
    //Para insercion de nuevas entidades
    @Insert
    fun insert(diasgenerados: DiasGenerados)
    //Devuelve una lista con todos las entidades
    @Query("SELECT * FROM tablaDiasGenerados")
    fun getAllDiasGenerados(): List<DiasGenerados>
    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaDiasGenerados WHERE id = :id")
    fun getDiasGeneradosById(id: Long): DiasGenerados?
    //Se actualiza una entidad
    @Update
    fun update(diasgenerados: DiasGenerados)
    //Se elimina una entidad
    @Delete
    fun delete(diasgenerados: DiasGenerados)
    //Consulta del numero total segun campo nombre de la entidad
    @Query("SELECT SUM(diasGen) FROM tablaDiasGenerados WHERE tipoDiaGen = :tipoDiaGen")
    fun getTotalDiasGenerados(tipoDiaGen: String): Long
}