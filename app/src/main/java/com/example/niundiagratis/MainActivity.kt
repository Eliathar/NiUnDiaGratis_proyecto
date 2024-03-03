package com.example.niundiagratis

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.niundiagratis.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.data.adapter.BBDDShowMsgAdapter
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.BBDDHandler.actualizarComputoGlobal
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.ui.home.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    //Usamos lateinit para indicaral compilador que la variable sera inicializada antes de ser usada
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    //Declaracion de navController
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BBDDShowMsgAdapter
    private lateinit var database: NiUnDiaGratisBBDD
    var mostrarMensajeInicio = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.mainToolbar)
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        /* TODO, Agregar aqui las ids de los fragments para que aparezca el menu en lugar de la
                flecha para ir atras */
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.submenu01Fragment, R.id.nav_gest_act, R.id.nav_gest_perm,
                R.id.nav_config, R.id.addActividadFragment, R.id.modActividadFragment,
                R.id.modActividadSeleccionadaFragment, R.id.addPermisoFragment,
                R.id.modPermisoFragment, R.id.modPermisoSeleccionadoFragment,
                R.id.addTipoActividadFragment, R.id.addTipoDiaFragment, R.id.modTipoActividadFragment,
                R.id.modTipoActividadSelecFragment, R.id.modTipoDiaFragment, R.id.modTipoDiaSelecFragment
            ), drawerLayout
        )
        // Inicializa navController
        navController = findNavController(R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        navView.setNavigationItemSelectedListener { item ->
            onNavigationItemSelected(item)
            true // Indica que el evento ha sido manejado correctamente
        }
    }

    fun onNavigationItemSelected(item: MenuItem) {
        onMenuItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun onMenuItemSelected(item: MenuItem) {
        // Lógica para manejar la selección del menú aquí
        /* Controlamos que elemento se selecciona del menu y le asignamos un valor para pasar al
        fragment de submenu01 y, con el, controlar el texto mostrado en los botones */
        val seleccion = when (item.itemId){
            R.id.nav_home -> 0
            R.id.nav_gest_act -> 1
            R.id.nav_gest_perm -> 2
            R.id.nav_config -> 3
            R.id.salir -> 4
            R.id.Sel_bbdd -> -2
            else -> -1
        }

        //Cerramos el menu lateral al realizar la seleccion
        drawerLayout.closeDrawer(GravityCompat.START)

        //Salimos de la aplicacion al seleccionar salir en el menu lateral
        if (seleccion == 4) finish()

        //Si se pulsa para seleccionar la base de datos
        if (seleccion == -2) seleccionBBDD()
        println(dbSeleccionada)

        /* En caso de que exista una opcion valida, realizamos la llamada al fragment pasando
        como parametro el bundle creado con la seleccion, se usa bundle para facilitar posibles
        mejoras en el futuro, garantizando flexibilidad a la hora de pasar los datos */
         if(seleccion != -2) cargarFragment(seleccion, navController)
    }

    private fun seleccionBBDD(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar base de datos")

        //Creamos el recyclerview y su adaptador
        val rW =  RecyclerView(this)

        //Inicializamos layout
        rW.layoutManager = LinearLayoutManager(this)

        //Inicializamos el adaptador, pasando el dato obtenido del onclick del adaptador al propio
        // adaptador como paramtero para realizar su funcion
        val adaptador =  BBDDShowMsgAdapter(obtenerDBNames(this)){dbSeleccion->
            dbSeleccionada = dbSeleccion
        }
        rW.adapter = adaptador
        //Comprobamos cierre de base de datos anterior
        //database?.close()
        //Obtenemos la instancia de la base de datos y la abrimospara que sea editable
        runBlocking {
            withContext(Dispatchers.IO) {
                BBDDHandler.crearBBDD(applicationContext).also { database = it }
            }
        }
        // Obtiene una instancia del NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        // Obtiene una instancia del fragmento actual
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]

        //Establecemos el recyclerview como vista de dialogo
        builder.setView(rW)
        builder.setPositiveButton("Aceptar"){ dialog, which ->
            lifecycleScope.launch(Dispatchers.IO){
                runBlocking{
                    actualizarComputoGlobal(database)
                    println("la bbdd selec es $dbSeleccionada")
                    // Recarga el fragmento si es una instancia de HomeFragment

                }
                if (currentFragment is HomeFragment) {
                    val newFragment = HomeFragment()
                    val fragmentManager = currentFragment.parentFragmentManager
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, newFragment).commit()
                }else{

                    //navController.navigate(R.id.action_global_nav_home)
                }
            }


        }
        builder.setNegativeButton("Cancelar", null)

        builder.show()
    }

    /* TODO recargar home usando el boton calcular del final de cada linea, del mensaje donde se
        recarga el home con recalculo de los datos para mostrar los datos de la bbdd seleccionada */
}
