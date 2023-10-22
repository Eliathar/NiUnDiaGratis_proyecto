package com.example.niundiagratis.data.dao

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
    fun getAllComputoGlobal(): List<ComputoGlobal>

    //Devuelve la entidad seleccionada mediante el id
    @Query("SELECT * FROM tablaComputoGlobal WHERE id = :id")
    fun getComputoGlobalById(id: Long): ComputoGlobal?

    //Se actualiza una entidad
    @Update
    fun update(computoglobal: ComputoGlobal)

    //Se elimina una entidad
    @Delete
    fun delete(computoglobal: ComputoGlobal)
    //
    @Query("SELECT SUM(genGlobal) - SUM(conGlobal) FROM tablaComputoGlobal")
    fun getSaldoDias(): Long

    //TODO Computo de generados/disfrutados segun tipo de dia
    /*@Query(*//* value = *//* "SELECT tipoDiaGen, SUM(diasGen) AS SumaDiasGenerados, SUM() AS SumaDiasDisfrutados FROM tabla_DiasGenerados WHERE TipoDias = :tipoDiaGen GROUP BY Tabla_TipoDias.")
    //Usamos @NotNull para evitar que devuelva listas vacias al no especificar el TipoDias
    //Devuelve un listado de objetos
    fun calcularComputoGlobal(@NotNull tipoDiaGen: String): List<ComputoGlobalResult>

    //Computo de generados/disfrutados por dias e impersion de listado segun tipo de dia
    @Query("SELECT " +
            "TipoDias, " +
            //Sumatorio de dias generados
                "SUM(DiasGenerados) AS SumaDiasGenerados, " +
            //Sumatorio de dias disfrutados
                "SUM(DiasConsumidos) AS SumaDiasConsumidos " +
            "FROM " +
                "DiasGenerados")
    //Devuelve un unico objeto con todos los datos
    fun calcularComputoGlobalGeneral(): ComputoGlobalResult*/
}

//Definimos la clase para realizar las consultas
/*
data class ComputoGlobalResult(
    val TipoDias: String,
    val SumaDiasGenerados: Int,
    val SumaDiasConsumidos: Int
)*/
