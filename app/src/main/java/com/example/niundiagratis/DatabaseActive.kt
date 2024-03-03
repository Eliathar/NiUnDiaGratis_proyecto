package com.example.niundiagratis

import android.content.Context
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.data.db.BBDDHandler.crearBBDD
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.ui.home.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object DatabaseActive {
    var databaseAct: NiUnDiaGratisBBDD? = null
    suspend fun getDatabase(context: Context): NiUnDiaGratisBBDD {
        println(dbSeleccionada)
        databaseAct = withContext(Dispatchers.IO) {
            crearBBDD(context.applicationContext)
        }
        println(databaseAct)
        return databaseAct!!
    }
}
