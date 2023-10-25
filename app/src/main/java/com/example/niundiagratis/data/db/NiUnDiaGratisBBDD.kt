package com.example.niundiagratis.data.db
import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.niundiagratis.data.dao.ActividadesRealizadasDao
import com.example.niundiagratis.data.dao.ComputoGlobalDao
import com.example.niundiagratis.data.dao.DiasDisfrutadosDao
import com.example.niundiagratis.data.dao.DiasGeneradosDao
import com.example.niundiagratis.data.dao.TiposActividadesDao
import com.example.niundiagratis.data.dao.TiposDiasDao
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
        Index(value = ["tipoDiasGenerados1"], unique = true),
        Index(value = ["tipoDiasGenerados2"], unique = true),
        Index(value = ["tipoDiasGenerados3"], unique = true)
    ])
data class TiposActividades(
    @PrimaryKey(autoGenerate = false)
    val nombreTipoAct: String,
    // Tipo de dias generados
    val tipoDiasGenerados1: String,
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
            childColumns = ["tipoActOk"]
        )
    ],
    indices = [
        Index(value = ["tipoActOk"], unique = true),
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

@Entity(tableName = "tablaGuardiasRealizadas",
    foreignKeys = [
        ForeignKey(
            entity = ActividadesRealizadas::class,
            parentColumns = ["tipoActOk"],
            childColumns = ["tipoGuardiaOk"]
        )
    ],
    indices = [
        Index(value = ["tipoGuardiaOk"])
    ]
)
data class GuardiasRealizadas(
    @PrimaryKey(autoGenerate = true)
    val idGuarOk: Int,
    val tipoGuardiaOk: String,
    val tipoDiasGeneradosGuarOk: String,
    val diasGeneradosT1GuarOk: Int,
    val tipoDiasGeneradosGuarOk2: String,
    val diasGeneradosT2GuarOk: Int
)

@Entity(tableName = "tablaDiasGenerados",
    foreignKeys = [
        ForeignKey(
            entity = ActividadesRealizadas::class,
            parentColumns = ["nombreActOk"],
            childColumns = ["nombreActgen"]
        )
    ],
    indices = [
        Index(value = ["nombreActgen"])
    ]
)
data class DiasGenerados(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tipoDiaGen: String,
    val nombreActgen: String,
    var fechaGen: Date,
    val diasGen: Int
)


@Entity(tableName = "tablaDiasDisfrutados",
    foreignKeys = [
        ForeignKey(
            entity = TiposDias::class,
            parentColumns = ["nombreTipoDia"],
            childColumns = ["tipoDiaCon"]
        )
    ],
    indices = [
        Index(value = ["tipoDiaCon"])
    ]
)
data class DiasDisfrutados(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tipoDiaCon: String,
    val fechaCon: Date
)

@Entity(tableName = "tablaComputoGlobal",
    foreignKeys = [
        ForeignKey(
            entity = TiposDias::class,
            parentColumns = ["nombreTipoDia"],
            childColumns = ["tipoDiaGlobal"]
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
    val maxGlobal: Int,
    val genGlobal: Int,
    val conGlobal: Int,
    val saldoGlobal: Int
)
//Definimos la base de datos en Room, sus entidades y la version de la base de datos
@Database(
    entities = [TiposActividades::class, TiposDias::class, ActividadesRealizadas::class, GuardiasRealizadas::class, DiasDisfrutados::class, ComputoGlobal::class, DiasGenerados::class],
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
    companion object{
        private var instancia: NiUnDiaGratisBBDD? = null
        //Esta funcion se usara para acceder a la base de datos abierta en el momento de usar la aplicacion
        fun obtenerInstancia(context: Context): NiUnDiaGratisBBDD{
            println("en obtenerinstancia")
            if(instancia == null){
                println("en if de obtenerinstancia")
                synchronized(NiUnDiaGratisBBDD::class){
                    println("en sincronizado")
                    instancia = Room.databaseBuilder(context.applicationContext, NiUnDiaGratisBBDD::class.java, "NiUnDiaGratis").build()
                    println("instanciado")
                }
            }
            println("retornando")
            return instancia!!
        }

    }
}

