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
import com.example.niundiagratis.databinding.FragmentAddFestivoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date

class AddFestivoFragment : Fragment() {

    private lateinit var binding: FragmentAddFestivoBinding
    private lateinit var dao: DiasFestivosDao
    private lateinit var navController: NavController
    private lateinit var fecha: Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddFestivoBinding.inflate(inflater, container, false)
        val view = binding.root
        dao = databaseAct!!.fDiasFestivosDao()
        navController = findNavController()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // binding.btnFechaFestivo
        //binding.editTxtNombreFestivo
        binding.btnFechaFestivo.setOnClickListener {
            showDatePickerDialog(requireContext()) { fechaSelec ->
                fecha = fechaSelec
                binding.btnFechaFestivo.text = formatearFecha(fecha)
            }

        }
        binding.btnGuardar18.setOnClickListener{
            btnCalcular()
        }
    }
    private fun btnCalcular(){
        lifecycleScope.launch(Dispatchers.IO) {
            val festivoNuevo = DiasFestivos(
                id = 0,
                fechaDia = fecha,
                nombreDia = binding.editTxtNombreFestivo.text.toString()
            )
            withContext(Dispatchers.Main) {
                val construct = AlertDialog.Builder(context)
                println("A guardar festivo9")
                construct.setTitle("Confirmar datos")
                println("A guardar festivo10")
                construct.setMessage(
                    "¿Estas seguro de que quieres guardar estos datos?:\n\n" +
                            "Fecha: $fecha\n" +
                            "Festividad: ${binding.editTxtNombreFestivo.text}\n"
                )
                println("A guardar festivo11")
                //Controlamos la reaccion de pulsar aceptar

                construct.setPositiveButton("Aceptar") { _, _ ->
                    runBlocking {
                        println("datos asignados")
                        //------------------------Volvemos a un hilo secundario para guardar los datos----------------------
                        lifecycleScope.launch(Dispatchers.IO) {
                            println("A guardar datos guardando")
                            dao.insert(festivoNuevo)
                            BBDDHandler.actualizarComputoGlobal(databaseAct!!)

                            println("A guardar datos terminado")
                            withContext(Dispatchers.Main){
                                navNuevo()
                            }

                        }
                        //------------------------------------Fin hilo secundario-------------------------------------------
//                        println("datos guardados?")
//                        //------Cargamos el fragment home al guardar los datos en la base de datos----------
//                        navController.navigate(R.id.nav_home)
                    }

                }
                construct.setNegativeButton("Cancelar", null)
                construct.show()
            }
        }
    }
    private fun navNuevo () {
        //------Cargamos el fragment home al guardar los datos en la base de datos----------
        navController.navigate(R.id.nav_home)
    }
}