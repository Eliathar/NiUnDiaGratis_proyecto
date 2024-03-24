package com.example.niundiagratis.data.db

class ReposNiUnDiaGratis {
    fun copiarDatos(instanciaAntigua: NiUnDiaGratisBBDD, instanciaNueva: NiUnDiaGratisBBDD) {
        val tiposDiasAntiguos = instanciaAntigua.fTiposDiasDao().getAllTiposDiasList().reversed()
        val tiposActividadesAntiguas = instanciaAntigua.fTiposActividadesDao().getAllTiposActividades().reversed()
        val diasFestivosAntiguos = instanciaAntigua.fDiasFestivosDao().getAllDiasFestivosList().reversed()

        tiposDiasAntiguos.forEach { instanciaNueva.fTiposDiasDao().insert(it) }
        tiposActividadesAntiguas.forEach { instanciaNueva.fTiposActividadesDao().insert(it) }
        diasFestivosAntiguos.forEach { instanciaNueva.fDiasFestivosDao().insert(it) }
    }
}