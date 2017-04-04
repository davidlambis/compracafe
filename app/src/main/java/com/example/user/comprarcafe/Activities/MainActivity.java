package com.example.user.comprarcafe.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.comprarcafe.Adapters.Adapter_view_pager;
import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Controllers.UsuariosController;
import com.example.user.comprarcafe.R;

public class MainActivity extends AppCompatActivity {

    //Objetos del adaptador de viewpager y el viewpager como tal;
    private Adapter_view_pager adaptador_viewPager;
    private ViewPager viewPager;

    public long id,idEmpresa;
    private long id_usuario_logued;
    public String nombres,apellidos,estado_sesion_logued,nombreEmpresa,direccionEmpresa,telefonoEmpresa,nitEmpresa;

    UsuariosController db_usuarios;
    EmpresasController db_empresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db_empresas = new EmpresasController(this);
        db_empresas.abrirBaseDeDatos();

            id = getIntent().getExtras().getLong("id");
            idEmpresa = getIntent().getExtras().getLong("idEmpresa");
            nombres = getIntent().getExtras().getString("nombres");
            apellidos = getIntent().getExtras().getString("apellidos");
            id_usuario_logued = getIntent().getExtras().getLong("id_usuario_logued");
            estado_sesion_logued = getIntent().getExtras().getString("estado_sesion_logued");

        nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);
        direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
        telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
        nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("idEmpresa",idEmpresa);
        editor.putString("nombreEmpresa",nombreEmpresa);
        editor.putString("direccionEmpresa",direccionEmpresa);
        editor.putString("telefonoEmpresa",telefonoEmpresa);
        editor.putString("nitEmpresa",nitEmpresa);
        editor.putString("nombres",nombres);
        editor.putString("apellidos",apellidos);
        editor.commit();


        //Toast.makeText(this,"idusuario:"+id+"nombresUsuario:"+nombres+"apellidosUsuario:"+apellidos+"idEmpresa:"+idEmpresa+"idUsuarioLogued:"+id_usuario_logued+"EstadoSesion:"+estado_sesion_logued,Toast.LENGTH_LONG).show();

            db_usuarios = new UsuariosController(this);
            db_usuarios.abrirBaseDeDatos();

            Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarPrincipal);
            setSupportActionBar(toolbar);
           // getSupportActionBar().setIcon(R.drawable.ic_action_collection);

            // Iniciamos la barra de tabs
            TabLayout tabLayout = (TabLayout) findViewById(R.id.TabLayoutPrincipal);

            // Se añaden las 3 tabs o pestañas de café
            // Le damos modo "fixed" para que todas las tabs tengan el mismo tamaño
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cerrarSesion:
                String estado_sesion = "0";
                db_usuarios.actualizarUsuario(id_usuario_logued,estado_sesion);
                //Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                //startActivity(intent);
                finish();
                break;
            case R.id.action_reportes:
                Intent i = new Intent(this,ReportesActivity.class);
                i.putExtra("nitEmpresa",nitEmpresa);
                startActivity(i);
                //Toast.makeText(this,"ABRE ACTIVIDAD DE REPORTES",Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
