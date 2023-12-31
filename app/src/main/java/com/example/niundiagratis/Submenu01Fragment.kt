package com.example.niundiagratis

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController


class Submenu01Fragment : Fragment() {
    //Usamos lateinit para indicaral compilador que la variable sera inicializada antes de ser usada
    private lateinit var fragmentManager: FragmentManager


    //Accedemos al parametro que se ha pasado a la funcion
    private var selMenuInt = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_submenu01, container, false)
        selMenuInt = arguments?.getInt("opcion_submenu_1") ?: -1

        return view
    }

    //Controlamos el comportamiento de los elementos del fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navController = findNavController()
        super.onViewCreated(view, savedInstanceState)
        fragmentManager = parentFragmentManager
        requireContext()
        this::class.java.simpleName

        //Obtenemos la referencia al toolbar para usar tituloOp1 como texto en ella
        val tituloOp1: Toolbar? = activity?.findViewById(R.id.mainToolbar)
        val context = activity?.applicationContext ?: return
        tituloOp1?.title = selTitulo(selMenuInt, context) as String

        //Creacion de variables para asignar texto a los botones
        val btn1: TextView = view.findViewById(R.id.btn_Sub_Opcion1)

        val btn2: TextView = view.findViewById(R.id.btn_Sub_Opcion2)

        //llamadas a las funciones para obtener el titulo y el texto de los botones
        selTextoBotones(selMenuInt, btn1, btn2)

        //Limpiamos la vista en la proxima oprtunidad que tenga el sistema, al seleccionar otra opcion que implique submenu01*/
        view.invalidate()

        //Prueba nueva
        btn1.setOnClickListener {
            when (btn1.text.toString()){

                getString(R.string.opciones_1_conf_btn1)-> {
                    selMenuInt = 4
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_act_btn1)-> {
                    selMenuInt = 6
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_perm_btn1)-> {
                    selMenuInt = 9
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_add_tipo_act_btn1)-> {
                    selMenuInt = 12
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_mod_tipo_act_btn1)-> {
                    selMenuInt = 14
                    cargarFragment(selMenuInt, navController)
                }

            }
        }
        btn2.setOnClickListener{
            when (btn2.text.toString()){

                getString(R.string.opciones_1_conf_btn2)-> {
                    selMenuInt = 5
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_act_btn2)-> {
                    selMenuInt = 7
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_perm_btn2)-> {
                    selMenuInt = 10
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_add_tipo_dia_btn2)-> {
                    selMenuInt = 13
                    cargarFragment(selMenuInt, navController)
                }
                getString(R.string.opciones_1_mod_tipo_dia_btn2)-> {
                    selMenuInt = 16
                    cargarFragment(selMenuInt, navController)
                }

            }
            //selecProxFragment(contexto, fragmentManager, btn1)
            println(71)
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentManager = requireActivity().supportFragmentManager
    }


}

