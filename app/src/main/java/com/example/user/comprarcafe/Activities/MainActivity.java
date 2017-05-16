package com.example.user.comprarcafe.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.user.comprarcafe.Adapters.Adapter_view_pager;
import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Controllers.UsuariosController;
import com.example.user.comprarcafe.R;


public class MainActivity extends AppCompatActivity {

    //Objetos del adaptador de viewpager y el viewpager como tal;
    private Adapter_view_pager adaptador_viewPager;
    private ViewPager viewPager;

    //Variables
    public long id, idEmpresa;
    public String nombres, apellidos, nombreEmpresa, direccionEmpresa, telefonoEmpresa, nitEmpresa;
    private String Nombre_Empresa,Nit_Empresa,Direccion_Empresa,Telefono_Empresa,Departamento_Empresa,Ciudad_Empresa;

    //Instancia del controlador de usuarios
    UsuariosController db_usuarios;

    //Instancia del controlador de empresas.
    EmpresasController db_empresas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Abre conexión a base de datos mediante controlador de empresas
        db_empresas = new EmpresasController(this);
        db_empresas.abrirBaseDeDatos();


        //Obtención de datos que provienen de LoginActivity
        id = getIntent().getExtras().getLong("id");
        idEmpresa = getIntent().getExtras().getLong("idEmpresa");
        nombres = getIntent().getExtras().getString("nombres");
        apellidos = getIntent().getExtras().getString("apellidos");
        Nombre_Empresa = getIntent().getExtras().getString("Nombre_Empresa");
        Nit_Empresa = getIntent().getExtras().getString("Nit_Empresa");
        Direccion_Empresa = getIntent().getExtras().getString("Direccion_Empresa");
        Telefono_Empresa = getIntent().getExtras().getString("Telefono_Empresa");
        Departamento_Empresa = getIntent().getExtras().getString("Departamento_Empresa");
        Ciudad_Empresa = getIntent().getExtras().getString("Ciudad_Empresa");

        //Llamado a métodos del controlador de empresas
        nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);
        direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
        telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
        nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);

        //Abre conexión a base de datos mediante controlador de usuarios
        db_usuarios = new UsuariosController(this);
        db_usuarios.abrirBaseDeDatos();

        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarPrincipal);
        setSupportActionBar(toolbar);

        // Iniciamos la barra de tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.TabLayoutPrincipal);

        // Se añaden las 3 tabs o pestañas de café
        // Le damos modo "fixed" para que todas las tabs tengan el mismo tamaño
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //Se va añadiendo cada tab
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        // Iniciamos el viewPager.
        viewPager = (ViewPager) findViewById(R.id.ViewPagerPrincipal);

        // Al adaptador se le pasa el gestor de fragmentos y el número de tabs que se utilizan .
        adaptador_viewPager = new Adapter_view_pager(getSupportFragmentManager(), tabLayout.getTabCount());

        // Se vincula el viewpager con su adaptador
        viewPager.setAdapter(adaptador_viewPager);

        //Esto es para que las pestañas respondan cuando se les pulse
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Asigna el menú que tiene una opción de cerrar sesión y un ícono para acceder a la actividad de reportes ReportesActivity
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                //Cuando se presiona cerrar sesión retorna al LoginActivity
            case R.id.cerrarSesion:
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.action_reportes:
                //Cuando se presiona el ícono de reportes accede a la actividad de reportes ReportesActivity y pasa el dato del nit de la empresa.
                Intent i = new Intent(this, ReportesActivity.class);
                i.putExtra("Nit_Empresa", Nit_Empresa);
                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    //Método que restringe el retorno a la actividad anterior presionando el botón de regresar del móvil
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
