package com.example.niundiagratis


import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DBSelector {
    var dbSeleccionada: String = "NiUnDiaGratis_${
        SimpleDateFormat("yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
    }"
}