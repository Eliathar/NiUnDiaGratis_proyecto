package com.example.niundiagratis.data.db

import android.content.Context
import androidx.room.Room
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.ui.home.HomeFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.runBlocking

//Creamos la base de datos
object BBDDHandler {
    suspend fun crearBBDD(context: Context): NiUnDiaGratisBBDD {
        //Cargamos por defecto el nombre de la base de datos en dbselector, donde se realiza el calculo y comprobacion del nombre
        val database: NiUnDiaGratisBBDD
        println("calculos sobre $dbSeleccionada")
        if (context.getDatabasePath(dbSeleccionada).exists()) {//codigo si la base de datos existe
            database = Room.databaseBuilder(
                context.applicationContext,
                NiUnDiaGratisBBDD::class.java, dbSeleccionada
            ).fallbackToDestructiveMigration()//Evita que se destruyan los datos existentes
                .build()
            database.openHelper.writableDatabase
        } else {//Codigo si la base de datos no existe
            database = Room.databaseBuilder(
                context.applicationContext,
                NiUnDiaGratisBBDD::class.java, dbSeleccionada
            ).build()
            database.openHelper.writableDatabase
            /* se realiza asi para esperar a que una funcion trermine antes de empezar con la
            siguiente y asi evitar problemas de datos sin inicializar */
            runBlocking {
                inicializarBBDD(database)
                inicializarBBDD1(database)
                inicializarBBDD2(database)
                inicializarBBDD3(database)
            }

        }
        println(database)
        return database
    }

    //Inicializacion de la base de datos
    private suspend fun inicializarBBDD(database: NiUnDiaGratisBBDD) {
        val daoTiposDias = database.fTiposDiasDao()

        lateinit var nuevoTipoDia: TiposDias
        //Inicializamos tipos de dias
        runBlocking {
            for (i in 1..6) {
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
        }
    }
    private suspend fun inicializarBBDD1(database: NiUnDiaGratisBBDD) {
        runBlocking {
            val daoTiposDias = database.fTiposDiasDao()
            val daoTiposActividades = database.fTiposActividadesDao()
            lateinit var nuevoTipoDia: TiposDias
            lateinit var nuevoTiposActividades: TiposActividades
            lateinit var nuevocomputoGlobal: ComputoGlobal
            lateinit var nuevaActReal: ActividadesRealizadas
            //Inicializamos tipos de dias
            //Inicializamos datos de tipos de actividades
            //Definimos los valores para las foreign keys especificadas
            val idTipoDia1 = daoTiposDias.getTipoDiaById("DA")?.nombreTipoDia
            val idTipoDia3 = daoTiposDias.getTipoDiaById("DPP")?.nombreTipoDia
            val idTipoDia5 = daoTiposDias.getTipoDiaById("MO")?.nombreTipoDia
            val idTipoDia6 = daoTiposDias.getTipoDiaById("DO")?.nombreTipoDia

            for (j in 1..7) {
                when (j) {
                    1 -> nuevoTiposActividades = TiposActividades(
                        nombreTipoAct = "Maniobras",
                        tipoDiasGenerados1 = idTipoDia6,
                        tipoDiasGenerados2 = idTipoDia3,
                        tipoDiasGenerados3 = idTipoDia5,
                        requisitosDiasAct1 = null,
                        requisitosDiasAct2 = 5,
                        requisitosDiasAct3 = null,
                        esGuardia = false
                    )

                    2 -> nuevoTiposActividades = TiposActividades(
                        nombreTipoAct = "Guarida seguridad",
                        tipoDiasGenerados1 = idTipoDia6,
                        tipoDiasGenerados2 = idTipoDia1,
                        tipoDiasGenerados3 = null,
                        requisitosDiasAct1 = 1,
                        requisitosDiasAct2 = 1,
                        requisitosDiasAct3 = null,
                        esGuardia = true
                    )

                    3 -> nuevoTiposActividades = TiposActividades(
                        nombreTipoAct = "Guardia orden",
                        tipoDiasGenerados1 = idTipoDia6,
                        tipoDiasGenerados2 = idTipoDia1,
                        tipoDiasGenerados3 = null,
                        requisitosDiasAct1 = 1,
                        requisitosDiasAct2 = 1,
                        requisitosDiasAct3 = null,
                        esGuardia = true
                    )

                    4 -> nuevoTiposActividades = TiposActividades(
                        nombreTipoAct = "Continuada",
                        tipoDiasGenerados1 = idTipoDia6,
                        tipoDiasGenerados2 = idTipoDia1,
                        tipoDiasGenerados3 = null,
                        requisitosDiasAct1 = 2,
                        requisitosDiasAct2 = 2,
                        requisitosDiasAct3 = null,
                        esGuardia = false
                    )

                    5 -> nuevoTiposActividades = TiposActividades(
                        nombreTipoAct = "Prolongada",
                        tipoDiasGenerados1 = null,
                        tipoDiasGenerados2 = null,
                        tipoDiasGenerados3 = null,
                        requisitosDiasAct1 = null,
                        requisitosDiasAct2 = null,
                        requisitosDiasAct3 = null,
                        esGuardia = false
                    )

                    6 -> nuevoTiposActividades = TiposActividades(
                        nombreTipoAct = "Curso",
                        tipoDiasGenerados1 = idTipoDia3,
                        tipoDiasGenerados2 = idTipoDia5,
                        tipoDiasGenerados3 = null,
                        requisitosDiasAct1 = 5,
                        requisitosDiasAct2 = 5,
                        requisitosDiasAct3 = null,
                        esGuardia = false
                    )

                    7 -> nuevoTiposActividades = TiposActividades(
                        nombreTipoAct = "Concentración",
                        tipoDiasGenerados1 = idTipoDia3,
                        tipoDiasGenerados2 = null,
                        tipoDiasGenerados3 = null,
                        requisitosDiasAct1 = 5,
                        requisitosDiasAct2 = null,
                        requisitosDiasAct3 = null,
                        esGuardia = false
                    )
                }
                daoTiposActividades.insert(nuevoTiposActividades)
            }
        }
    }
    private suspend fun inicializarBBDD2(database: NiUnDiaGratisBBDD) {
        runBlocking {
            val daoTiposDias = database.fTiposDiasDao()
            val daoComputoGlobal = database.fComputoGlobalDao()
            lateinit var nuevocomputoGlobal: ComputoGlobal
            //Inicializamos tipos de dias
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
            for (k in 1..6) {
                when (k) {
                    1 -> nuevocomputoGlobal =
                        ComputoGlobal(0, idTipoDiaG1.toString(), maxTipoDiaG1!!, 0, 0, 0)

                    2 -> nuevocomputoGlobal =
                        ComputoGlobal(0, idTipoDiaG2.toString(), maxTipoDiaG2!!, 0, 0, 0)

                    3 -> nuevocomputoGlobal =
                        ComputoGlobal(0, idTipoDiaG3.toString(), maxTipoDiaG3!!, 0, 0, 0)

                    4 -> nuevocomputoGlobal =
                        ComputoGlobal(0, idTipoDiaG4.toString(), maxTipoDiaG4!!, 0, 0, 0)

                    5 -> nuevocomputoGlobal =
                        ComputoGlobal(0, idTipoDiaG5.toString(), maxTipoDiaG5, 0, 0, 0)

                    6 -> nuevocomputoGlobal =
                        ComputoGlobal(0, idTipoDiaG6.toString(), maxTipoDiaG6, 0, 0, 0)
                }
                daoComputoGlobal.insert(nuevocomputoGlobal)
            }
        }
    }
    private suspend fun inicializarBBDD3(database: NiUnDiaGratisBBDD) {
        runBlocking {

            val daoTiposDias = database.fTiposDiasDao()
            val daoTiposActividades = database.fTiposActividadesDao()
            val daoActividadesRealizadas = database.fActividadesRealizadasDao()
            lateinit var nuevaActReal: ActividadesRealizadas
            //Inicializamos tipos de dias
            //Actividades realizadas
            val tipoActReal = daoTiposActividades.getTipoActividadByNombre("Maniobras")?.nombreTipoAct
            val idTipoDia1 = daoTiposDias.getTipoDiaById("DA")?.nombreTipoDia
            val idTipoDia3 = daoTiposDias.getTipoDiaById("DPP")?.nombreTipoDia
            val idTipoDia5 = daoTiposDias.getTipoDiaById("MO")?.nombreTipoDia
            val idTipoDia6 = daoTiposDias.getTipoDiaById("DO")?.nombreTipoDia
            val tipoActReal1 = daoTiposActividades.getTipoActividadByNombre("Continuada")?.nombreTipoAct
            val tipoActReal2 = daoTiposActividades.getTipoActividadByNombre("Prolongada")?.nombreTipoAct
            val fecha = SimpleDateFormat("dd-MM-yyyy")
            println(fecha)

            for (l in 1..3) {
                when (l) {
                    1 -> {
                        nuevaActReal = ActividadesRealizadas(
                            0,
                            "Maniobras1",
                            tipoActReal.toString(),
                            idTipoDia1.toString(),
                            idTipoDia3.toString(),
                            idTipoDia5.toString(),
                            1,
                            2,
                            3,
                            fecha.parse("31-01-2023"),
                            fecha.parse("10-02-2023"),
                            false
                        )
                    }

                    2 -> {
                        nuevaActReal = ActividadesRealizadas(
                            0,
                            "maniobras2",
                            tipoActReal1.toString(),
                            idTipoDia3.toString(),
                            idTipoDia3.toString(),
                            idTipoDia3.toString(),
                            1,
                            2,
                            3,
                            fecha.parse("29-01-2023"),
                            fecha.parse("10-02-2023"),
                            false
                        )
                    }

                    3 -> {
                        nuevaActReal = ActividadesRealizadas(
                            0,
                            "maniobras3",
                            tipoActReal2.toString(),
                            idTipoDia5.toString(),
                            idTipoDia5.toString(),
                            idTipoDia5.toString(),
                            1,
                            2,
                            3,
                            fecha.parse("28-01-2023"),
                            fecha.parse("11-02-2023"),
                            false
                        )
                        println(nuevaActReal)
                    }
                }

                daoActividadesRealizadas.insert(nuevaActReal)

            }
        }
    }

    fun actualizarDiasGenerados(nuevaActividad: ActividadesRealizadas, database: NiUnDiaGratisBBDD, opcion: Int) {
        // Obtén los tipos de días y los días generados de la nueva actividad
        val tiposDias = listOf(nuevaActividad.tipoDiasActOk1, nuevaActividad.tipoDiasActOk2, nuevaActividad.tipoDiasActOk3)
        val diasGenerados = listOf(nuevaActividad.diasGenActOk1, nuevaActividad.diasGenActOk2, nuevaActividad.diasGenActOk3)



        // Recorre cada tipo de día
        for (i in tiposDias.indices) {
            val tipoDia = tiposDias[i]
            val diasGen = diasGenerados[i]

            // Si el tipo de día y los días generados no son nulos, crea un nuevo registro en DiasGenerados
            if (tipoDia != null && diasGen != null) {
                for (j in 0 until diasGen) {
                    when (opcion){
                        1 -> {
                            val nuevoDiaGenerado = DiasGenerados(
                                id = 0,  // El ID se generará automáticamente
                                tipoDiaGen = tipoDia,
                                nombreActgen = nuevaActividad.nombreActOk,
                                fechaGen = nuevaActividad.fechaInActOk,  // Usa la fecha de inicio de la actividad como fecha de generación
                            )

                            // Añade el nuevo registro a la base de datos
                            database.fDiasGeneradosDao().insert(nuevoDiaGenerado)
                        }
                        2 -> {
                            // Busca el DiasGenerados existente en la base de datos
                            val diasGeneradosExistentes = database.fDiasGeneradosDao().getDiasGeneradosPorActividad(nuevaActividad.nombreActOk, tipoDia)
                            println(diasGeneradosExistentes)
                            println(diasGeneradosExistentes.size)

                            // Recorre cada DiasGenerados existente
                            for (diaGeneradoExistente in diasGeneradosExistentes) {
                                // Actualiza los campos del DiasGenerados existente
                                diaGeneradoExistente.fechaGen = nuevaActividad.fechaInActOk

                                // Pasa el DiasGenerados actualizado a la función update
                                database.fDiasGeneradosDao().update(diaGeneradoExistente)

                            }
                            // Insertamos nuevos registros para los días en caso de generarse mas al modificarlos
                            for (j in diasGeneradosExistentes.size until diasGen) {
                                val nuevoDiaGenerado = DiasGenerados(
                                    id = 0,  // El ID se generará automáticamente
                                    tipoDiaGen = tipoDia,
                                    nombreActgen = nuevaActividad.nombreActOk,
                                    fechaGen = nuevaActividad.fechaInActOk
                                )// Usamos la fecha de inicio de la actividad como fecha de generación

                                database.fDiasGeneradosDao().insert(nuevoDiaGenerado)
                            }
                            // Si hay más registros existentes que días generados, elimina la diferencia
                            if (diasGeneradosExistentes.size > diasGen) {
                                val diasGeneradosExcedentes = diasGeneradosExistentes.subList(diasGen, diasGeneradosExistentes.size)

                                for (diaGeneradoExcedente in diasGeneradosExcedentes) {
                                    database.fDiasGeneradosDao().delete(diaGeneradoExcedente)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fun actualizarComputoGlobal(database: NiUnDiaGratisBBDD) {

        // Obtén los tipos de días
        val tiposDias = database.fTiposDiasDao().getAllTiposDiasListNombres()

        // Recorre cada tipo de día
        for (tipoDia in tiposDias) {
            // Obtén el total de días generados y consumidos para este tipo de día
            val totalDiasGenerados = database.fDiasGeneradosDao().getTotalDiasGenerados(tipoDia)
            val totalDiasConsumidos = database.fDiasDisfrutadosDao().getTotalDiasDisfrutados(tipoDia)

            //Obtenemos el dia para acceder a sus campos
            val nombreDia = database.fTiposDiasDao().getTipoDiaById(tipoDia)

            //Calculamos los días restantes
            var diasRestantes = 0

            //Si el tipo de día es "PO" o "AP", realiza la resta sobre maxDias
            if (tipoDia == "PO" || tipoDia == "AP") {
                diasRestantes = nombreDia?.maxDias?.minus(totalDiasConsumidos) ?: 0
            }else diasRestantes = totalDiasGenerados - totalDiasConsumidos


            // Busca el registro de cómputo global para este tipo de día
            val computoGlobal = database.fComputoGlobalDao().getComputoGlobalByTipo(tipoDia)

            if (computoGlobal != null) {
                // Si existe un registro de cómputo global, actualízalo
                computoGlobal.saldoGlobal= diasRestantes
                database.fComputoGlobalDao().update(computoGlobal)
            } else {
                // Si no existe un registro de cómputo global, crea uno nuevo
                val nuevoComputoGlobal = ComputoGlobal(
                    id= 0,
                    tipoDiaGlobal = tipoDia,
                    maxGlobal = nombreDia?.maxDias,
                    genGlobal = totalDiasConsumidos,
                    conGlobal = totalDiasGenerados,
                    saldoGlobal = diasRestantes
                )
                database.fComputoGlobalDao().insert(nuevoComputoGlobal)
            }
        }
    }

}

