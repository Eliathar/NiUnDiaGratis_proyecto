package com.example.niundiagratis.data.db

import android.content.Context
import androidx.room.Room
import com.example.niundiagratis.MainActivity
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.GuardiasRealizadasDao
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.get
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import androidx.room.RoomDatabase

//Creamos la base de datos
object BBDDHandler{
    suspend fun crearBBDD(context: Context) : String{
        val periodo = SimpleDateFormat(
            "yyyy",
            Locale.getDefault()
        ).format(
            Calendar.getInstance().time
        )
        //Creamos el nombre de la base de datos usando el año
        val nombreBD = "NiUnDiaGratis_$periodo"
        val niUnDiaGratisDB: NiUnDiaGratisBBDD
        if (context.getDatabasePath(nombreBD).exists()) {
            niUnDiaGratisDB = Room.databaseBuilder(
                context.applicationContext,
                NiUnDiaGratisBBDD::class.java, nombreBD
            ).fallbackToDestructiveMigration()//Evita que se destruyan los datos existentes
                .build()
            niUnDiaGratisDB.openHelper.writableDatabase
            println("BBDD ya creada")
        } else {
            niUnDiaGratisDB = Room.databaseBuilder(
                context.applicationContext,
                NiUnDiaGratisBBDD::class.java,
                nombreBD

            ).build()
            niUnDiaGratisDB.openHelper.writableDatabase
            inicializarBBDD(niUnDiaGratisDB)
            println("bbdd inicializada")

        }
        return nombreBD
    }
    //Inicializacion de la base de datos
    private suspend fun inicializarBBDD(database: NiUnDiaGratisBBDD){
        val daoTiposDias = database.fTiposDiasDao()
        val daoTiposActividades = database.fTiposActividadesDao()
        val daoComputoGlobal = database.fComputoGlobalDao()
        lateinit var nuevoTipoDia: TiposDias
        lateinit var nuevoTiposActividades: TiposActividades
        lateinit var nuevocomputoGlobal: ComputoGlobal
        //Inicializamos tipos de dias
        for (i in 1..6) {
            println("bucle for $i")
            when (i) {
                1 -> nuevoTipoDia = TiposDias(nombreTipoDia = "DA", maxDias = 10)
                2 -> nuevoTipoDia = TiposDias(nombreTipoDia = "PO", maxDias = 22)
                3 -> nuevoTipoDia = TiposDias(nombreTipoDia = "DPP", maxDias = 10)
                4 -> nuevoTipoDia = TiposDias(nombreTipoDia = "AP", maxDias = 8)
                5 -> nuevoTipoDia = TiposDias(nombreTipoDia = "MO", maxDias = null)
                6 -> nuevoTipoDia = TiposDias(nombreTipoDia = "DO", maxDias = null)
            }
            daoTiposDias.insert(nuevoTipoDia)
        }
        //Inicializamos datos de tipos de actividades
        //Definimos los valores para las foreign keys especificadas
        val idTipoDia1 = daoTiposDias.getTipoDiaById("DA")?.nombreTipoDia
        val idTipoDia3 = daoTiposDias.getTipoDiaById("DPP")?.nombreTipoDia
        val idTipoDia5 = daoTiposDias.getTipoDiaById("MO")?.nombreTipoDia
        val idTipoDia6 = daoTiposDias.getTipoDiaById("DO")?.nombreTipoDia
        for (j in 1..7){
            println("bucle for actividades $j")
            when (j){
                1 -> nuevoTiposActividades = TiposActividades(nombreTipoAct = "Maniobras", tipoDiasGenerados1 = idTipoDia6, tipoDiasGenerados2 = idTipoDia3, tipoDiasGenerados3 = idTipoDia5, requisitosDiasAct1 = null, requisitosDiasAct2 = 5, requisitosDiasAct3 = null, esGuardia = false)
                2 -> nuevoTiposActividades = TiposActividades(nombreTipoAct = "Guarida seguridad", tipoDiasGenerados1 = idTipoDia6, tipoDiasGenerados2 = idTipoDia1, tipoDiasGenerados3 = null, requisitosDiasAct1 = 1, requisitosDiasAct2 = 1, requisitosDiasAct3 = null, esGuardia = true)
                3 -> nuevoTiposActividades = TiposActividades(nombreTipoAct = "Guardia orden", tipoDiasGenerados1 = idTipoDia6, tipoDiasGenerados2 = idTipoDia1, tipoDiasGenerados3 = null, requisitosDiasAct1 = 1, requisitosDiasAct2 = 1, requisitosDiasAct3 = null, esGuardia = true)
                4 -> nuevoTiposActividades = TiposActividades(nombreTipoAct = "Continuada", tipoDiasGenerados1 = idTipoDia6, tipoDiasGenerados2 = idTipoDia1, tipoDiasGenerados3 = null, requisitosDiasAct1 = 2, requisitosDiasAct2 = 2, requisitosDiasAct3 = null, esGuardia = false)
                5 -> nuevoTiposActividades = TiposActividades(nombreTipoAct = "Prolongada", tipoDiasGenerados1 = null, tipoDiasGenerados2 = null, tipoDiasGenerados3 = null, requisitosDiasAct1 = null, requisitosDiasAct2 = null, requisitosDiasAct3 = null, esGuardia = false)
                6 -> nuevoTiposActividades = TiposActividades(nombreTipoAct = "Curso", tipoDiasGenerados1 = idTipoDia3, tipoDiasGenerados2 = idTipoDia5, tipoDiasGenerados3 = null, requisitosDiasAct1 = 5, requisitosDiasAct2 = 5, requisitosDiasAct3 = 5, esGuardia = false)
                7 -> nuevoTiposActividades = TiposActividades(nombreTipoAct = "Concentración", tipoDiasGenerados1 = idTipoDia3, tipoDiasGenerados2 = null, tipoDiasGenerados3 = null, requisitosDiasAct1 = 5, requisitosDiasAct2 = null, requisitosDiasAct3 = null, esGuardia = false)
            }
            daoTiposActividades.insert(nuevoTiposActividades)
        }

        //Inicializamos datos de computo global
        //Definimos los valores para las foreign keys especificadas
        val idTipoDiaG1 = daoTiposDias.getTipoDiaById("DA")?.nombreTipoDia
        val idTipoDiaG2 = daoTiposDias.getTipoDiaById("PO")?.nombreTipoDia
        val idTipoDiaG3 = daoTiposDias.getTipoDiaById("DPP")?.nombreTipoDia
        val idTipoDiaG4 = daoTiposDias.getTipoDiaById("AP")?.nombreTipoDia
        val idTipoDiaG5 = daoTiposDias.getTipoDiaById("MO")?.nombreTipoDia
        val idTipoDiaG6 = daoTiposDias.getTipoDiaById("DO")?.nombreTipoDia
        val maxTipoDiaG1 = daoTiposDias.getTipoDiaById("DA")!!.maxDias
        val maxTipoDiaG2 = daoTiposDias.getTipoDiaById("PO")!!.maxDias
        val maxTipoDiaG3 = daoTiposDias.getTipoDiaById("DPP")!!.maxDias
        val maxTipoDiaG4 = daoTiposDias.getTipoDiaById("AP")!!.maxDias
        val maxTipoDiaG5 = daoTiposDias.getTipoDiaById("MO")?.maxDias
        val maxTipoDiaG6 = daoTiposDias.getTipoDiaById("DO")?.maxDias
        for (k in 1..6){
            println("bucle for computo global $k")
            when (k){
                1 -> nuevocomputoGlobal = ComputoGlobal(0, idTipoDiaG1.toString(), maxTipoDiaG1!!, 0, 0, 0)
                2 -> nuevocomputoGlobal = ComputoGlobal(0, idTipoDiaG2.toString(), maxTipoDiaG2!!, 0, 0, 0)
                3 -> nuevocomputoGlobal = ComputoGlobal(0, idTipoDiaG3.toString(), maxTipoDiaG3!!, 0, 0, 0)
                4 -> nuevocomputoGlobal = ComputoGlobal(0, idTipoDiaG4.toString(), maxTipoDiaG4!!, 0, 0, 0)
                5 -> nuevocomputoGlobal = ComputoGlobal(0, idTipoDiaG5.toString(), maxTipoDiaG5, 0, 0, 0)
                6 -> nuevocomputoGlobal = ComputoGlobal(0, idTipoDiaG6.toString(), maxTipoDiaG6, 0, 0, 0)
            }
            daoComputoGlobal.insert(nuevocomputoGlobal)
            println("computo terminado")
        }
    }
}

