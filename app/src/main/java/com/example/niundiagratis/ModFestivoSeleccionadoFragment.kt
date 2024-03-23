package com.example.niundiagratis

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.niundiagratis.DatabaseActive.databaseAct
import com.example.niundiagratis.data.dao.DiasFestivosDao
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.DiasFestivos
import com.example.niundiagratis.databinding.FragmentModFestivoSeleccionadoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ModFestivoSeleccionadoFragment : Fragment() {
    private lateinit var binding: FragmentModFestivoSeleccionadoBinding
    private lateinit var fechaDiaF: Date
    private lateinit var dao: DiasFestivosDao
    private lateinit var entidad: DiasFestivos
    private lateinit var navController: NavController
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentModFestivoSeleccionadoBinding.inflate(inflater, container, false)
        val view = binding.root
        //Declaramos el bundle
        val bundle = this.arguments
        //Obtenemos valores del bundle
        id = bundle!!.getInt("id")
        println("esta linea 1 la id es $id")
        //Obtenemos instancia de la base de datos
        navController = findNavController()
        /* Obtenemos los datos del registro con la id del bundle en otro hilo, pero esperando a que
        termine de obtener los datos para continuar */
        runBlocking {
            println("esta linea la id es $id")
            entidad = withContext(Dispatchers.IO) {
                //Obtenemos instancia del Dao
                dao = databaseAct!!.fDiasFestivosDao()
                dao.getDiaFestivoById(id)!!
            }
            println(entidad.fechaDia.toString())
            println(entidad.nombreDia)
            //Asignamos los valores a los campos
            //Obtenemos las fechas, las formateamoss y las asignamos a los textos de los botones correspondientes
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaFormateada = formato.format(entidad.fechaDia)
            binding.btnFechaFestivo21.text = getString(R.string.inicio, fechaFormateada)
            binding.editTxtNombreFestivo21.setText(entidad.nombreDia)
            /* Inicializamos valores de campos de fechas para los botones, pues pese a tener el
            texto escrito las variables solo son inicializadas en el onclick, si no se produce el
            evento las variables no tienen valor */
            fechaDiaF = entidad.fechaDia
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFechaFestivo21.setOnClickListener {
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fechaDiaF = fechaSelec
                binding.btnFechaFestivo21.text = getString(R.string.inicio, formatearFecha(fechaDiaF))
            }

        }
        binding.btnGuardar21.setOnClickListener {
            btnCalcular()
        }
    }
    private fun btnCalcular(){
//-------------------------------------Boton calcular-----------------------------------------------

        println("calcular pulsado")
        lifecycleScope.launch(Dispatchers.IO) {
            //Asignamos los valores a una variable del tipo adecuado para guardar los datos
            println("A guardar datos")
            val festivoMod = DiasFestivos(
                id = entidad.id,
                nombreDia = binding.editTxtNombreFestivo21.text.toString(),
                fechaDia = fechaDiaF
            )
            println("datos: ${festivoMod.id}, ${festivoMod.nombreDia}, $fechaDiaF")
            /*
            -------------------------Creamos el cuadro de confirmacion------------------------------------------
            Estamos en un hilo secundario, pero el cuadro de dialogo solo se ejecuta en el hilo principal, no
            obstante es necesario que el cauadro aparezca despues de la asignacion de valores, por lo que deb
            ser llamado en el hilo secundario para asegurar que tiene los datos cargados para ejecutarse en el
            hilo principal
            */
            withContext(Dispatchers.Main) {
                val construct = AlertDialog.Builder(context)
                construct.setTitle("Confirmar datos")
                construct.setMessage(
                    "¿Estas seguro de que quieres guardar estos datos?:\n\n" +
                            "Fecha: ${festivoMod.fechaDia}\n" +
                            "Festividad: ${festivoMod.nombreDia}\n"
                )
                //Controlamos la reaccion de pulsar aceptar
                construct.setPositiveButton("Aceptar") { _, _ ->
                    println("datos asignados")
//------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                    lifecycleScope.launch(Dispatchers.IO) {
                        dao.update(festivoMod)
                        BBDDHandler.actualizarComputoGlobal(databaseAct!!)
                    }
//------------------------------------Fin hilo secundario-------------------------------------------
                    println("datos guardados?")
                    //------------Cargamos el fragment home al guardar los datos en la base de datos--------------------
                    navController.navigate(R.id.nav_home)
                }
                construct.setNegativeButton("Cancelar", null)
                construct.show()
            }
        }
//-----------------------------------Fin hilo secundario--------------------------------------------

//-----------------------------------Fin boton calcular---------------------------------------------
    }


}