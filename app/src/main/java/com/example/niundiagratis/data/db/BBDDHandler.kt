package com.example.niundiagratis.data.db

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.DiasGeneradosDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import androidx.lifecycle.lifecycleScope

/*private lateinit var database: RoomDatabase*/
object BBDDHandler {
    suspend fun crearBBDD(context: Context) {
        val periodo = SimpleDateFormat(
            "yyyy",
            Locale.getDefault()
        ).format(
            Calendar.getInstance().time
        )
        //Creamos el nombre de la base de datos usando el año
        val nombreBD = "NiUnDiaGratis_$periodo"
        val database: NiUnDiaGratisBBDD
        if (context.getDatabasePath(nombreBD).exists()) {
            database = Room.databaseBuilder(
                context.applicationContext,
                NiUnDiaGratisBBDD::class.java, nombreBD
            ).fallbackToDestructiveMigration()//Evita que se destruyan los datos existentes
                .build()
            database.openHelper.writableDatabase
            println("bbdd abierta1")
        } else {
            database = Room.databaseBuilder(
                context.applicationContext,
                NiUnDiaGratisBBDD::class.java,
                nombreBD
            ).build()
            database.openHelper.writableDatabase
            inicializarBBDD(database)

            println("bbdd no existe1")
        }

    }
    private suspend fun inicializarBBDD(database: NiUnDiaGratisBBDD){
        val daoTiposDias = database.fTiposDiasDao()
        /*val daoTiposActividades = database.fTiposActividadesDao()
        val daoActividadesRealizadas = database.fActividadesRealizadasDao()
        val daoDiasGenerados = database.fDiasGeneradosDao()
        val daoDiasDisfrutados = database.fDiasDisfrutadosDao()
        val daoComputoGlobal = database.fComputoGlobalDao()*/
        lateinit var nuevoTipoDia: TiposDias
        //Inicializamos tipos de dias
        for (i in 1..5){
            println("bucle for $i")
            when (i){
                1 -> nuevoTipoDia = TiposDias(nombreTipoDia = "DA", maxDias = 10)
                2 -> nuevoTipoDia = TiposDias(nombreTipoDia = "PO", maxDias = 22)
                3 -> nuevoTipoDia = TiposDias(nombreTipoDia = "DPP", maxDias = 10)
                4 -> nuevoTipoDia = TiposDias(nombreTipoDia = "AP", maxDias = 8)
                5 -> nuevoTipoDia = TiposDias(nombreTipoDia = "MO", maxDias = null)
            }
            daoTiposDias.insert(nuevoTipoDia)
        }
    }


}

