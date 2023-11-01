package com.example.niundiagratis

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun abrirFragment(fragment: Fragment) {
    /* Inicializamos view a null, siempre deberia tener un valor diferente de nulo,
    pero se usa ? para prevenir errores */
    val view: View? = null

    //Obtenemos la actividad, usamos ? para prevenir errores por valor nulo
    val fragmnt = view?.context as FragmentManager

    /* Añadimos el fragment a la actividad, no es necesario ? pues si view es nulo esta
    linea no se ejecutara */
    fragmnt.beginTransaction().replace(R.id.nav_view, fragment).commit()
}

//Obtenemos el titulo del submenu segun el id
fun selTitulo(submenuId: Int, context: Context?): CharSequence {
    return when (submenuId) {
        1 -> context?.resources?.getText(R.string.opciones_1_gest_actividades)!!//Gestión ed actividades
        2 -> context?.resources?.getText(R.string.opciones_1_gest_permisos)!!//Gestión de permisos
        3 -> context?.resources?.getText(R.string.opciones_1_gest_config)!!//Gestión de configuración
        4 -> context?.resources?.getText(R.string.opciones_1_config_add_tipo)!!//Gestión de configuración
        5 -> context?.resources?.getText(R.string.opciones_1_config_mod_tipo)!!//Gestión de configuración
        else -> "error"
    }
}

fun selTextoBotones(selMenuInt: Int, btn1: TextView, btn2: TextView) {
    when (selMenuInt) {
        //Configuramos textos a mostrar segun strings.xml y opcion seleccionada por parametro
        1 -> {//Submenu01Fragment v1
            btn1.setText(R.string.opciones_1_act_btn1)
            btn2.setText(R.string.opciones_1_act_btn2)
        }

        2 -> {//Submenu01Fragment v2
            btn1.setText(R.string.opciones_1_perm_btn1)
            btn2.setText(R.string.opciones_1_perm_btn2)

        }

        3 -> {//Submenu01Fragment v3
            btn1.setText(R.string.opciones_1_conf_btn1)
            btn2.setText(R.string.opciones_1_conf_btn2)

        }

        4 -> {//Submenu01Fragment v4
            btn1.setText(R.string.opciones_1_add_tipo_act_btn1)
            btn2.setText(R.string.opciones_1_add_tipo_dia_btn2)

        }

        5 -> {//Submenu01Fragment v5
            btn1.setText(R.string.opciones_1_mod_tipo_act_btn1)
            btn2.setText(R.string.opciones_1_mod_tipo_dia_btn2)

        }


    }
}

fun cargarFragment(seleccion: Int, navController: NavController, datos: Any? = null){

    try {
        if(seleccion !=-1) {
            val bundle = Bundle()
            bundle.putInt("opcion_submenu_1", seleccion)
            //TODO Ir añadiendo las llamadas a los fragments, "seleccion" solo se utiliza para llamadas desde Submenu01
            when(seleccion){
                0 -> navController.navigate(R.id.nav_home)
                in 1..5 -> navController.navigate(R.id.action_global_submenu01Fragment, bundle)
                6 -> navController.navigate(R.id.action_submenu01Fragment_to_addActividadFragment, bundle)
                7 -> navController.navigate(R.id.action_submenu01Fragment_to_modActividadFragment, bundle)
                8 -> navController.navigate(R.id.action_modActividadFragment_to_modActividadSeleccionadaFragment, bundle)
                9 -> navController.navigate(R.id.action_submenu01Fragment_to_addPermisoFragment, bundle)
                10 -> navController.navigate(R.id.action_submenu01Fragment_to_modPermisoFragment, bundle)
                11 -> navController.navigate(R.id.action_modPermisoFragment_to_modPermisoSeleccionadoFragment, bundle)
                12 -> navController.navigate(R.id.action_submenu01Fragment_to_addTipoActividadFragment, bundle)
                13 -> navController.navigate(R.id.action_submenu01Fragment_to_addTipoDiaFragment, bundle)
                14 -> navController.navigate(R.id.action_submenu01Fragment_to_modTipoActividadFragment, bundle)
                15 -> navController.navigate(R.id.action_modTipoActividadFragment_to_modTipoActividadSelecFragment, bundle)
                16 -> navController.navigate(R.id.action_submenu01Fragment_to_modTipoDiaFragment, bundle)
                17 -> navController.navigate(R.id.action_modTipoDiaFragment_to_modTipoDiaSelecFragment, bundle)
            }



        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/*fun showDatePickerDialog(context: Context): Date {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate: Date? = null

    val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
        // En este bloque, puedes manejar la fecha seleccionada
        selectedDate = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }.time
    }, year, month, day)

    datePickerDialog.show()

    // Espera hasta que el usuario haya seleccionado una fecha
    while (selectedDate == null) {
        // Puedes agregar un retardo muy corto aquí si lo deseas
    }

    return selectedDate!!
}*/
fun showDatePickerDialog(context: Context, onDateSelected: (Date) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
        val selectedDate = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }.time
        onDateSelected(selectedDate)
    }, year, month, day)

    datePickerDialog.show()
}
fun formatearFecha(fecha: Date): String {
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
    return formato.format(fecha)
}



fun obtenerTipoDiasBD(): List<String>{
    //TODO logica para obtener valores de BBDD
    //val tipoDiasBBDD = //logica para obtener valores
    //Obtenemos un valor predeterminado para el spinner, asi evitamos un textView
    val valoresPredeterminados = mutableListOf("Selecciona una opción")

    //Actualizamos al obtener valores de la BBDD
    //valoresPredeterminados.addAll(tipoDiasBBDD)

    //Retornamos lista de valores obtenidos
    return valoresPredeterminados
}



