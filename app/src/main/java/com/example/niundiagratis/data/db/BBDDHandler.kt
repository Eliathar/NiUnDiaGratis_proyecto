package com.example.niundiagratis.data.db

import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId
import java.util.Date

//Creamos la base de datos
object BBDDHandler {
    fun crearBBDD(instancia: NiUnDiaGratisBBDD) {
        runBlocking {
        inicializarBBDD(instancia)
        inicializarBBDD1(instancia)
        inicializarBBDD2(instancia)
        inicializarBBDD3(instancia)
        inicializarBBDD4(instancia)
        }
    }

    //Inicializacion de la base de datos
    //Tipos de dias
    private fun inicializarBBDD(database: NiUnDiaGratisBBDD) {
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
                    5 -> nuevoTipoDia = TiposDias(nombreTipoDia = "PU", maxDias = null)
                    6 -> nuevoTipoDia = TiposDias(nombreTipoDia = "DO", maxDias = null)
                }
                daoTiposDias.insert(nuevoTipoDia)
            }
        }
        println("inicializar 1 completo")
    }
    //Tipos de actividades
    private fun inicializarBBDD1(database: NiUnDiaGratisBBDD) {
        runBlocking {
            val daoTiposDias = database.fTiposDiasDao()
            val daoTiposActividades = database.fTiposActividadesDao()
            lateinit var nuevoTiposActividades: TiposActividades
            //Inicializamos tipos de dias
            //Inicializamos datos de tipos de actividades
            //Definimos los valores para las foreign keys especificadas
            val idTipoDia1 = daoTiposDias.getTipoDiaById("DA")?.nombreTipoDia
            val idTipoDia3 = daoTiposDias.getTipoDiaById("DPP")?.nombreTipoDia
            val idTipoDia5 = daoTiposDias.getTipoDiaById("PU")?.nombreTipoDia
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
        println("inicializar 2 completo")
    }
    //Computo global
    fun inicializarBBDD2(database: NiUnDiaGratisBBDD) {
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
            val idTipoDiaG5 = daoTiposDias.getTipoDiaById("PU")?.nombreTipoDia
            val idTipoDiaG6 = daoTiposDias.getTipoDiaById("DO")?.nombreTipoDia
            val maxTipoDiaG1 = daoTiposDias.getTipoDiaById("DA")!!.maxDias
            val maxTipoDiaG2 = daoTiposDias.getTipoDiaById("PO")!!.maxDias
            val maxTipoDiaG3 = daoTiposDias.getTipoDiaById("DPP")!!.maxDias
            val maxTipoDiaG4 = daoTiposDias.getTipoDiaById("AP")!!.maxDias
            val maxTipoDiaG5 = daoTiposDias.getTipoDiaById("PU")?.maxDias
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
        println("inicializar 3 completo")
    }
    //Actividades realizadas
    suspend fun inicializarBBDD3(database: NiUnDiaGratisBBDD) {
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
            val idTipoDia5 = daoTiposDias.getTipoDiaById("PU")?.nombreTipoDia
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
        //Actualizamos computo global segun datos de inicializacion
        actualizarComputoGlobal(database)
        println("inicializar 4 completo")
    }
    //Inicializamos festivos nacionales
    private fun inicializarBBDD4(database: NiUnDiaGratisBBDD) {
        runBlocking {

            val daoDiasFestivos = database.fDiasFestivosDao()
            lateinit var nuevoDiaFestivo: DiasFestivos
            val year = Year.now().value
            val localDate1 = LocalDate.of(year, 1, 1)
            val localDate2 = LocalDate.of(year, 1, 6)
            val localDate3 = LocalDate.of(year, 5, 1)
            val localDate4 = LocalDate.of(year, 8, 15)
            val localDate5 = LocalDate.of(year, 10, 12)
            val localDate6 = LocalDate.of(year, 11, 1)
            val localDate7 = LocalDate.of(year, 12, 6)
            val localDate8 = LocalDate.of(year, 12, 8)
            val localDate9 = LocalDate.of(year, 12, 25)
            val fecha1 = Date.from(localDate1.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha2 = Date.from(localDate2.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha3 = Date.from(localDate3.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha4 = Date.from(localDate4.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha5 = Date.from(localDate5.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha6 = Date.from(localDate6.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha7 = Date.from(localDate7.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha8 = Date.from(localDate8.atStartOfDay(ZoneId.systemDefault()).toInstant())
            val fecha9 = Date.from(localDate9.atStartOfDay(ZoneId.systemDefault()).toInstant())

            for (l in 1..9) {
                when (l) {
                    1 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Año nuevo",
                            fecha1
                        )
                    }

                    2 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Epifania/Reyes Magos",
                            fecha2
                        )
                    }

                    3-> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Dia del trabajo",
                            fecha3
                        )
                    }
                    4 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Virgen de la Asunción",
                            fecha4
                        )
                    }
                    5 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Dia de la Hispanidad",
                            fecha5
                        )
                    }
                    6 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Todos los santos",
                            fecha6
                        )
                    }
                    7 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Día de la Constitución",
                            fecha7
                        )
                    }
                    8 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "La Inmaculada Concepción",
                            fecha8
                        )
                    }
                    9 -> {
                        nuevoDiaFestivo = DiasFestivos(
                            0,
                            "Navidad",
                            fecha9
                        )
                    }
                }

                daoDiasFestivos.insert(nuevoDiaFestivo)


            }
        }
        //Actualizamos computo global segun datos de inicializacion
        actualizarComputoGlobal(database)
        println("inicializar 4 completo")
    }


    fun actualizarDiasGenerados(nuevaActividad: ActividadesRealizadas, database: NiUnDiaGratisBBDD, opcion: Int) {
        // Obtén los tipos de días y los días generados de la nueva actividad
        val tiposDias = listOf(nuevaActividad.tipoDiasActOk1, nuevaActividad.tipoDiasActOk2, nuevaActividad.tipoDiasActOk3)
        val diasGenerados = listOf(nuevaActividad.diasGenActOk1, nuevaActividad.diasGenActOk2, nuevaActividad.diasGenActOk3)


        /*
        TODO modificar entidad con id, id actividad generadora, tipo dias y cantidad, poniendo un
         registro por cada tipo de dia, para modificar buscar el tipo de dia adecuado y modificar,
         ¿eliminar tabla dias generados y operar directamente con la actividad?
        Recorre cada tipo de día
        */
        for (i in tiposDias.indices) {
            val tipoDia = tiposDias[i]
            val diasGen = diasGenerados[i]

            // Si el tipo de día y los días generados no son nulos, crea un nuevo registro en DiasGenerados
            if (tipoDia != null && diasGen != null) {
                when (opcion){
                    1 -> {
                        val nuevoDiaGenerado = DiasGenerados(
                            id = 0,  // El ID se generará automáticamente
                            tipoDiaGen = tipoDia,
                            nombreActgen = nuevaActividad.nombreActOk,
                            fechaGen = nuevaActividad.fechaInActOk,  // Usa la fecha de inicio de la actividad como fecha de generación
                            totalDias = diasGen
                        )

                        // Añade el nuevo registro a la base de datos
                        database.fDiasGeneradosDao().insert(nuevoDiaGenerado)
                    }
                    2, 3 -> {
                        // Busca el DiasGenerados existente en la base de datos
                        val diasGeneradosExistentes = database.fDiasGeneradosDao().getDiasGeneradosPorActividad(nuevaActividad.nombreActOk, tipoDia)
                        //Si no mesta vacio lo actualiza
                        if(diasGeneradosExistentes.isNotEmpty()){
                            val diaGeneradoExistente = diasGeneradosExistentes[0]
                            diaGeneradoExistente.fechaGen = nuevaActividad.fechaInActOk
                            diaGeneradoExistente.totalDias = diasGen
                            database.fDiasGeneradosDao().update(diaGeneradoExistente)
                        }else{
                            val nuevoDiaGenerado1 = DiasGenerados(
                                id = 0,  // El ID se generará automáticamente
                                tipoDiaGen = tipoDia,
                                nombreActgen = nuevaActividad.nombreActOk,
                                fechaGen = nuevaActividad.fechaInActOk,  // Usa la fecha de inicio de la actividad como fecha de generación
                                totalDias = diasGen
                            )
                            if (opcion == 2) database.fDiasGeneradosDao().insert(nuevoDiaGenerado1)
                            else if (opcion == 3) database.fDiasGeneradosDao().delete(nuevoDiaGenerado1)
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
            val totalDiasConsumidos = database.fDiasDisfrutadosDao().getTotalDiasDisfrutadosByTipoDia(tipoDia)
            println("$totalDiasGenerados y el total de consumidos $totalDiasConsumidos")

            //Obtenemos el dia para acceder a sus campos
            val nombreDia = database.fTiposDiasDao().getTipoDiaById(tipoDia)

            //Si el tipo de día es "PO" o "AP", realiza la resta sobre maxDias
            val diasRestantes: Int = if (tipoDia == "PO" || tipoDia == "AP") {
                nombreDia?.maxDias?.minus(totalDiasConsumidos) ?: 0
            } else totalDiasGenerados - totalDiasConsumidos


            // Busca el registro de cómputo global para este tipo de día
            val computoGlobal = database.fComputoGlobalDao().getComputoGlobalByTipo(tipoDia)

            if (computoGlobal != null) {
                // Si existe un registro de cómputo global, actualízalo
                computoGlobal.saldoGlobal = diasRestantes
                computoGlobal.genGlobal = totalDiasGenerados
                computoGlobal.conGlobal = totalDiasConsumidos
                database.fComputoGlobalDao().update(computoGlobal)
            } else {
                // Si no existe un registro de cómputo global, crea uno nuevo
                val nuevoComputoGlobal = ComputoGlobal(
                    id = 0,
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

