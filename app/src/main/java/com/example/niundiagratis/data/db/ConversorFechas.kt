package com.example.niundiagratis.data.db
import androidx.room.TypeConverter
import java.util.Date

class ConversorFechasDB {
    @TypeConverter
    fun desdeFecha(fecha: Date?): Long? {
        return fecha?.time
    }

    @TypeConverter
    fun aFecha(fechadb: Long?): Date? {
        return fechadb?.let { Date(it) }
    }
}
