package com.example.niundiagratis.data.db
import android.content.Context
import android.os.Parcelable
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.DatabaseActive
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.DiasFestivosDao
import com.example.niundiagratis.data.dao.DiasGeneradosDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
import com.example.niundiagratis.data.db.BBDDHandler.crearBBDD
import kotlinx.coroutines.runBlocking
import java.util.Date


//Marcamos la clase como entidad de Room con @Entity
@Entity(tableName = "tablaTiposActividades",
    foreignKeys = [
        ForeignKey(
            entity = TiposDias::class,
            parentColumns = ["nombreTipoDia"],
            childColumns = ["tipoDiasGenerados1"]
        ),
        ForeignKey(
            entity = TiposDias::class,
            parentColumns = ["nombreTipoDia"],
            childColumns = ["tipoDiasGenerados2"]
        ),
        ForeignKey(
            entity = TiposDias::class,
            parentColumns = ["nombreTipoDia"],
            childColumns = ["tipoDiasGenerados3"]
        )
    ],
    indices = [
        Index(value = ["tipoDiasGenerados1"]),
        Index(value = ["tipoDiasGenerados2"]),
        Index(value = ["tipoDiasGenerados3"]),
        Index(value = ["nombreTipoAct"], unique = true)
    ])
data class TiposActividades(
    @PrimaryKey(autoGenerate = false)
    val nombreTipoAct: String,
    // Tipo de dias generados
    val tipoDiasGenerados1: String?,
    val tipoDiasGenerados2: String?,
    val tipoDiasGenerados3: String?,
    //Requisitos para concesion de dias
    val requisitosDiasAct1: Int?,
    val requisitosDiasAct2: Int?,
    val requisitosDiasAct3: Int?,
    //Control guardia
    val esGuardia: Boolean
)

@Entity(tableName = "tablaTiposDias")
data class TiposDias(
    @PrimaryKey(autoGenerate = false)
    val nombreTipoDia: String,
    val maxDias: Int?
)

@Entity(tableName = "tablaActividadesRealizadas",
    foreignKeys = [
        ForeignKey(
            entity = TiposActividades::class,
            parentColumns = ["nombreTipoAct"],
            childColumns = ["tipoActOk"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tipoActOk"]),
        Index(value = ["fechaInActOk"], unique = true),
        Index(value = ["nombreActOk"], unique = true)
    ]
)
class ActividadesRealizadas(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    //Nombre de la actividad
    var nombreActOk: String,
    // El campo tipoActOk es un valor de la tabla TiposActividades
    var tipoActOk: String,
    // Los campos tipoDiasActOk son los valores de tiposDiasGenerados de la tabla TiposActividades
    var tipoDiasActOk1: String,
    var tipoDiasActOk2: String,
    var tipoDiasActOk3: String,
    // Los campos diasGenActOk son un cálculo que precisa tanto de tipoDiasGenActOk como de requisitosDiasAct(en TiposActividades)
    var diasGenActOk1: Int?,
    var diasGenActOk2: Int?,
    var diasGenActOk3: Int?,
    // Fechas
    var fechaInActOk: Date,
    var fechaFiActOk: Date,
    //Control guardia
    val esGuardiaOk: Boolean
)
/* TODO cambiar a id, id actividad generadora, tipo dia y cantidad de dias de ese tipo, un registro
    por tipo de dias, ¿eliminar esta tabla y operar directamente con la actividad realizada? */
@Entity(tableName = "tablaDiasGenerados",
    foreignKeys = [
        ForeignKey(
            entity = ActividadesRealizadas::class,
            parentColumns = ["nombreActOk"],
            childColumns = ["nombreActgen"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["nombreActgen"], unique = false)
    ]
)
data class DiasGenerados(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tipoDiaGen: String,
    val nombreActgen: String,
    var fechaGen: Date,
    var totalDias: Int
)


@Entity(tableName = "tablaDiasDisfrutados",
    foreignKeys = [
        ForeignKey(
            entity = TiposDias::class,
            parentColumns = ["nombreTipoDia"],
            childColumns = ["tipoDiaDis"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tipoDiaDis"])
    ]
)
data class DiasDisfrutados(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tipoDiaDis: String,
    val fechaCon: Date,
    val fechaFinPer: Date,
    val diasTotales: Int
)

@Entity(tableName = "tablaComputoGlobal",
    foreignKeys = [
        ForeignKey(
            entity = TiposDias::class,
            parentColumns = ["nombreTipoDia"],
            childColumns = ["tipoDiaGlobal"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tipoDiaGlobal"])
    ]
)
data class ComputoGlobal(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tipoDiaGlobal: String,
    val maxGlobal: Int?,
    var genGlobal: Int,
    var conGlobal: Int,
    var saldoGlobal: Int
)
//Definimos la tabla para los dias festivos
@Entity(tableName = "tablaDiasFestivos")
data class DiasFestivos(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombreDia: String,
    val fechaDia: Date
)
//Definimos la base de datos en Room, sus entidades y la version de la base de datos
@Database(
    entities = [TiposActividades::class, TiposDias::class, ActividadesRealizadas::class, DiasDisfrutados::class, ComputoGlobal::class, DiasGenerados::class, DiasFestivos::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConversorFechasDB::class)
abstract class NiUnDiaGratisBBDD : RoomDatabase() {

    abstract fun fTiposActividadesDao(): TiposActividadesDao
    abstract fun fTiposDiasDao(): TiposDiasDao
    abstract fun fActividadesRealizadasDao(): ActividadesRealizadasDao
    abstract fun fDiasGeneradosDao(): DiasGeneradosDao
    abstract fun fDiasDisfrutadosDao(): DiasDisfrutadosDao
    abstract fun fComputoGlobalDao(): ComputoGlobalDao
    abstract fun fDiasFestivosDao(): DiasFestivosDao
    companion object{
        private var instancia: NiUnDiaGratisBBDD? = null

         fun obtenerInstancia(context: Context, nombreBD: String): NiUnDiaGratisBBDD{

            if (context.getDatabasePath(dbSeleccionada).exists()) {//codigo si la base de datos existe
                instancia = Room.databaseBuilder(
                    context.applicationContext,
                    NiUnDiaGratisBBDD::class.java, dbSeleccionada
                ).fallbackToDestructiveMigration()//Evita que se destruyan los datos existentes
                    .build()
            } else {//Codigo si la base de datos no existe
                instancia = Room.databaseBuilder(
                    context.applicationContext,
                    NiUnDiaGratisBBDD::class.java, dbSeleccionada
                ).build()
                println(DatabaseActive.databaseAct)
                println("dentro de obtener bbdd")
                runBlocking {
                    println("antes de inicializar bbdd")
                    crearBBDD(instancia!!)
                    println("despues de inicializar bbdd")
                }
            }
            return instancia!!
        }
    }
}

