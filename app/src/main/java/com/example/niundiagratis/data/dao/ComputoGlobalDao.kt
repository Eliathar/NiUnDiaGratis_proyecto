package com.example.niundiagratis.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.niundiagratis.data.db.ComputoGlobal


//Declaramos como interfaz DAO
@Dao
interface ComputoGlobalDao {
    //Para insercion de nuevas entidades
    @Insert
    fun insert(computoglobal: ComputoGlobal)

    //Devuelve una lista con todos las entidades
    @Query("SELECT * FROM tablaComputoGlobal")
    fun getAllComputoGlobal(): LiveData<List<ComputoGlobal>>

    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaComputoGlobal WHERE id = :id")
    fun getComputoGlobalById(id: Long): ComputoGlobal?
    @Query("SELECT * FROM tablaComputoGlobal WHERE tipoDiaGlobal = :id")
    fun getComputoGlobalByTipo(id: String): ComputoGlobal?

    @Query("SELECT * FROM tablaComputoGlobal WHERE tipoDiaGlobal = :id")
    fun getComputoGlobalBTipoDia(id: String): ComputoGlobal?

    //Se actualiza una entidad
    @Update
    fun update(computoglobal: ComputoGlobal)

    //Se elimina una entidad
    @Delete
    fun delete(computoglobal: ComputoGlobal)
    //
    @Query("SELECT SUM(genGlobal) - SUM(conGlobal) FROM tablaComputoGlobal")
    fun getSaldoDias(): Long
}