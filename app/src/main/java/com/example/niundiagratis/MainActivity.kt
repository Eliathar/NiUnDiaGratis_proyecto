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
import android.app.DatePickerDialog
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.niundiagratis.data.db.NiUnDiaGratisBBDD
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    //Usamos lateinit para indicaral compilador que la variable sera inicializada antes de ser usada
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    //Declaracion de navController
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var database: RoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Usamos el contexto de la actividad para crear la base de datos
        val context = this
        //Usamos el año actual para definir el nombre de la base de datos, de este modo se creara una base de datos nueva el 1 de enero
        val periodo = SimpleDateFormat("yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
        //Creamos el nombre de la base de datos usando el año
        val nombreBD = "NiUnDiaGratis_$periodo"
        if (context.getDatabasePath(nombreBD).exists()){
            database = Room.databaseBuilder(context.applicationContext, NiUnDiaGratisBBDD::class.java, nombreBD)
            .fallbackToDestructiveMigration()//Evita que se destruyan los datos existentes
            .build()
            println ("bbdd abierta")
        }else{
            database = Room.databaseBuilder(context.applicationContext, NiUnDiaGratisBBDD::class.java, nombreBD).build()
            println ("bbdd no existe")
        }
            database.openHelper.writableDatabase


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.mainToolbar)


        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        /* TODO, Agregar aqui las ids de los fragments para que aparezca el menu en lugar de la
                flecha para ir atras */
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.submenu01Fragment,
                R.id.nav_gest_act, R.id.nav_gest_perm, R.id.nav_config, R.id.addActividadFragment,
                R.id.modActividadFragment, R.id.modActividadSeleccionadaFragment, R.id.addPermisoFragment,
                R.id.modPermisoFragment, R.id.modPermisoSeleccionadoFragment, R.id.addTipoActividadFragment, R.id.addTipoDiaFragment,
                R.id.modTipoActividadFragment, R.id.modTipoActividadSelecFragment,
                R.id.modTipoDiaFragment, R.id.modTipoDiaSelecFragment
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

    fun onMenuItemSelected(item: MenuItem) {
        // Lógica para manejar la selección del menú aquí
        /* Controlamos que elemento se selecciona del menu y le asignamos un valor para pasar al
        fragment de submenu01 y, con el, controlar el texto mostrado en los botones */
        val seleccion = when (item.itemId){
            R.id.nav_home -> 0
            R.id.nav_gest_act -> 1
            R.id.nav_gest_perm -> 2
            R.id.nav_config -> 3
            else -> -1
        }
        println("seleccion $seleccion")

        //Cerramos el menu lateral al realizar la seleccion
        drawerLayout.closeDrawer(GravityCompat.START)

        /* En caso de que exista una opcion valida, realizamos la llamada al fragment pasando
        como parametro el bundle creado con la seleccion, se usa bundle para facilitar posibles
        mejoras en el futuro, garantizando flexibilidad a la hora de pasar los datos */
        val bundle = Bundle()
        bundle.putInt("opcion_submenu_1", seleccion)
        bundle.putBundle("navController", navController.saveState())
        cargarFragment(seleccion, navController)
    }



}
