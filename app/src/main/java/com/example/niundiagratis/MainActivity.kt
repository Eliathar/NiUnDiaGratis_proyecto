package com.example.niundiagratis

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.niundiagratis.DBSelector.dbSeleccionada
import com.example.niundiagratis.DatabaseActive.databaseAct
import com.example.niundiagratis.data.adapter.BBDDShowMsgAdapter
import com.example.niundiagratis.data.db.BBDDHandler
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import com.example.niundiagratis.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    //Usamos lateinit para indicaral compilador que la variable sera inicializada antes de ser usada
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //Declaracion de navController
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout


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
                R.id.nav_home,
                R.id.submenu01Fragment,
                R.id.nav_gest_act,
                R.id.nav_gest_perm,
                R.id.nav_config,
                R.id.addActividadFragment,
                R.id.modActividadFragment,
                R.id.modActividadSeleccionadaFragment,
                R.id.addPermisoFragment,
                R.id.modPermisoFragment,
                R.id.modPermisoSeleccionadoFragment,
                R.id.addTipoActividadFragment,
                R.id.addTipoDiaFragment,
                R.id.modTipoActividadFragment,
                R.id.modTipoActividadSelecFragment,
                R.id.modTipoDiaFragment,
                R.id.modTipoDiaSelecFragment,
                R.id.addFestivoFragment,
                R.id.modFestivoFragment,
                R.id.modFestivoSeleccionadoFragment
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
        val seleccion = when (item.itemId) {
            R.id.Sel_bbdd -> -2
            R.id.nav_home -> 0
            R.id.nav_gest_act -> 1
            R.id.nav_gest_perm -> 2
            R.id.nav_gest_fest -> 18
            R.id.nav_config -> 3
            R.id.salir -> 4
            else -> -1
        }
        println("la seleccion1 del menu es $seleccion")

        //Cerramos el menu lateral al realizar la seleccion
        drawerLayout.closeDrawer(GravityCompat.START)

        //Salimos de la aplicacion al seleccionar salir en el menu lateral
        if (seleccion == 4) finish()

        //Si se pulsa para seleccionar la base de datos
        if (seleccion == -2) seleccionBBDD()
        println(dbSeleccionada)
        println(databaseAct)
        println("la seleccion2 del menu es $seleccion")

        /* En caso de que exista una opcion valida, realizamos la llamada al fragment pasando
        como parametro el bundle creado con la seleccion, se usa bundle para facilitar posibles
        mejoras en el futuro, garantizando flexibilidad a la hora de pasar los datos */
        if (seleccion != -2) cargarFragment(seleccion, navController)
    }

    /*private fun seleccionBBDD(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccionar base de datos")

        //Creamos el recyclerview y su adaptador
        //val rW =  RecyclerView(this)
        val rW = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = BBDDShowMsgAdapter(obtenerDBNames(this@MainActivity)) { dbSeleccion ->
                dbSeleccionada = dbSeleccion
            }
        }
        builder.setView(rW)

        //Inicializamos layout
        rW.layoutManager = LinearLayoutManager(this)

        //Inicializamos el adaptador, pasando el dato obtenido del onclick del adaptador al propio
        // adaptador como paramtero para realizar su funcion
        val adaptador =  BBDDShowMsgAdapter(obtenerDBNames(this)){dbSeleccion->
            dbSeleccionada = dbSeleccion
        }
        rW.adapter = adaptador

        // Obtiene una instancia del NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        // Obtiene una instancia del fragmento actual
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]

        //Establecemos el recyclerview como vista de dialogo
        builder.setView(rW)
        builder.setPositiveButton("Aceptar"){ _, _ ->
            lifecycleScope.launch(Dispatchers.IO){
                runBlocking{
                    BBDDHandler.actualizarComputoGlobal(databaseAct!!)
                    println("la bbdd selec es $dbSeleccionada")
                }
                withContext(Dispatchers.Main) {
                    if (currentFragment is HomeFragment) {
                        val newFragment = HomeFragment()
                        val fragmentManager = currentFragment.parentFragmentManager
                        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, newFragment).commit()
                    } else {
                        navController.navigate(R.id.action_global_nav_home)
                    }
                }
            }
            /* Estemos donde estemos al cambiar la base de datos debe mostrarse el fragment home,
            para ello navegamos directamente al fragment home, si sencillamente lo recargamos vuelve
            a la version anterior de la pila, con los datos de la anterior base de datos */

        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}*/
    private fun seleccionBBDD() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Seleccionar base de datos")

        val rW = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = BBDDShowMsgAdapter(obtenerDBNames(this@MainActivity)) { dbSeleccion ->
                dbSeleccionada = dbSeleccion // ← Actualiza el String global
            }
        }
        builder.setView(rW)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            lifecycleScope.launch {
                try {
                    // 1. Verificar que se seleccionó algo
                    if (dbSeleccionada.isEmpty()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Por favor selecciona una base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }

                    // 2. Cerrar la BD anterior si existe
                    withContext(Dispatchers.IO) {
                        DatabaseActive.databaseAct?.close()
                    }

                    // 3. Obtener la NUEVA instancia basada en dbSeleccionada
                    //    (obtenerInstancia() lee dbSeleccionada internamente)
                    val nuevaDatabase = withContext(Dispatchers.IO) {
                        DatabaseActive.getDatabase(this@MainActivity)
                    }

                    // 4. Actualizar el cómputo global con la nueva BD
                    withContext(Dispatchers.IO) {
                        BBDDHandler.actualizarComputoGlobal(nuevaDatabase)
                    }

                    // 5. Navegar a Home limpiando el back stack
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    val opts = navOptions {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                    navController.navigate(R.id.nav_home, null, opts)

                    drawerLayout.closeDrawer(GravityCompat.START)
                    dialog.dismiss()

                    Toast.makeText(
                        this@MainActivity,
                        "Base de datos cambiada a: $dbSeleccionada",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (t: Throwable) {
                    android.util.Log.e("DB_SELECT", "Error al cambiar BD", t)
                    Toast.makeText(
                        this@MainActivity,
                        "Error al cambiar BD: ${t.message ?: "desconocido"}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}
