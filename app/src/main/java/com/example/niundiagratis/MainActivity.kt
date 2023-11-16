package com.example.niundiagratis

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.niundiagratis.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

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
            else -> -1
        }

        //Cerramos el menu lateral al realizar la seleccion
        drawerLayout.closeDrawer(GravityCompat.START)

        //Salimos de la aplicacion al seleccionar salir en el menu lateral
        if (seleccion == 4) finish()

        /* En caso de que exista una opcion valida, realizamos la llamada al fragment pasando
        como parametro el bundle creado con la seleccion, se usa bundle para facilitar posibles
        mejoras en el futuro, garantizando flexibilidad a la hora de pasar los datos */

        cargarFragment(seleccion, navController)
    }
}
