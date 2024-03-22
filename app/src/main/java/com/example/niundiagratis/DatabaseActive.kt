package com.example.niundiagratis

import android.content.Context
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseActive {
    /*Obtenemos la instancia para la base de datos, abriendola a escritura, todos los fragments
    recurriran a esta instancia en concreto, debiendo llamar a la funcion getDatabase par abrir
    una base de datos nueva, permitiendo de este modo cambiar de base de datos durante la
    ejecucion de la aplicacion, por defecto se cargara la base de datos del año actual
    */
    var databaseAct: NiUnDiaGratisBBDD? = null
    suspend fun getDatabase(context: Context): NiUnDiaGratisBBDD {
        println(dbSeleccionada)
        databaseAct = withContext(Dispatchers.IO) {
            NiUnDiaGratisBBDD.obtenerInstancia(context.applicationContext, dbSeleccionada)
        }
        println(databaseAct)

        return databaseAct!!
    }
}
