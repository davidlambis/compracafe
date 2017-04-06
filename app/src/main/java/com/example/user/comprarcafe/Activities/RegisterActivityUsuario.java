package com.example.user.comprarcafe.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Controllers.UsuariosController;
import com.example.user.comprarcafe.Models.Empresa;
import com.example.user.comprarcafe.Models.Usuario;
import com.example.user.comprarcafe.R;

import java.util.ArrayList;

public class RegisterActivityUsuario extends AppCompatActivity implements OnClickListener {

    private EditText edtNombresUsuario, edtApellidosUsuario, edtCedulaUsuario,edtDireccionUsuario, edtTelefonoUsuario, edtCorreoUsuario, edtContraseñaUsuario;
    private Button btnRegistroUsuario;
    private String estadoEmpresa;

    Usuario usuario;

    //Instancia del controlador de usuarios
    UsuariosController db_usuarios;

    //Instancia del controlador de empresas
    EmpresasController db_empresas;

    private Spinner spinner;
    public ArrayAdapter<String> adaptersEmpresas;
    public ArrayList<String> spinnerEmpresas;
    public ArrayList<Empresa> listEmpresa;
    long idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_usuario);

        Toolbar toolbar = (Toolbar)findViewById(R.id.ToolbarRegistroUsuario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("REGISTRO USUARIO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Conexión al controlador de Empresa
        db_empresas = new EmpresasController(this);
        db_empresas.abrirBaseDeDatos();

        //Conexión al controlador de Usuario
        db_usuarios = new UsuariosController(this);
        db_usuarios.abrirBaseDeDatos();

        edtNombresUsuario = (EditText)findViewById(R.id.edtNombresUsuario);
        edtApellidosUsuario = (EditText)findViewById(R.id.edtApellidosUsuario);
        edtCedulaUsuario = (EditText)findViewById(R.id.edtCedulaUsuario);
        edtDireccionUsuario = (EditText)findViewById(R.id.edtDireccionUsuario);
        edtTelefonoUsuario = (EditText)findViewById(R.id.edtTelefonoUsuario);
        edtCorreoUsuario = (EditText)findViewById(R.id.edtCorreoUsuario);
        edtContraseñaUsuario = (EditText)findViewById(R.id.edtContraseñaUsuario);
        btnRegistroUsuario = (Button)findViewById(R.id.btnRegistroUsuario);
        spinner = (Spinner)findViewById(R.id.spinnerU);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idEmpresa = listEmpresa.get(position).getIdEmpresa();
                    usuario = new Usuario();
                    usuario.setIdEmpresa(idEmpresa);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        spinnerEmpresas = new ArrayList<>();
        refresh();

        adaptersEmpresas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerEmpresas);
        if(adaptersEmpresas.getCount() == 0){
            Toast.makeText(this,"No se ha registrado ninguna empresa,regístra tu compraventa para crear usuario",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this,RegisterActivityEmpresa.class);
            startActivity(i);
            finish();
        }

        ///ListLecturas.setAdapter(adapters);
        spinner.setAdapter(adaptersEmpresas);

        btnRegistroUsuario.setOnClickListener(this);

    }

    //Cargar Listas
    private void refresh() {
        listEmpresa = new ArrayList<Empresa>();
        ///Array list para Sectores
        ArrayList<Empresa> listadoEmpresas = db_empresas.findAllEmpresas();
        spinnerEmpresas.clear();

        //
        ////For del listado para Empresa
        for (Empresa empresa : listadoEmpresas) {
            Empresa empresa1 = new Empresa();
            empresa1.setIdEmpresa(empresa.getIdEmpresa());
            empresa1.setNombreEmpresa(empresa.getNombreEmpresa());
            listEmpresa.add(empresa1);
            spinnerEmpresas.add(empresa.getIdEmpresa()+" ("+ empresa.getNombreEmpresa()+")");
        }
    }

    @Override
    public void onClick(View v) {
        String strNombresUsuario = edtNombresUsuario.getText().toString();
        if (TextUtils.isEmpty(strNombresUsuario)) {
            edtNombresUsuario.setError("Llena este campo");
            return;
        }
        String strApellidosUsuario = edtApellidosUsuario.getText().toString();
        if (TextUtils.isEmpty(strApellidosUsuario)) {
            edtApellidosUsuario.setError("Llena este campo");
            return;
        }
        String strCedulaUsuario = edtCedulaUsuario.getText().toString();
        if (TextUtils.isEmpty(strCedulaUsuario)) {
            edtCedulaUsuario.setError("Llena este campo");
            return;
        }
        String strDireccionUsuario = edtDireccionUsuario.getText().toString();
        if (TextUtils.isEmpty(strDireccionUsuario)) {
            edtDireccionUsuario.setError("Llena este campo");
            return;
        }
        String strTelefonoUsuario = edtTelefonoUsuario.getText().toString();
        if (TextUtils.isEmpty(strTelefonoUsuario)) {
            edtTelefonoUsuario.setError("Llena este campo");
            return;
        }
        String strCorreoUsuario = edtCorreoUsuario.getText().toString();
        if (TextUtils.isEmpty(strCorreoUsuario)) {
            edtCorreoUsuario.setError("Llena este campo");
            return;
        }
        String strContraseñaUsuario = edtContraseñaUsuario.getText().toString();
        if (TextUtils.isEmpty(strContraseñaUsuario)) {
            edtContraseñaUsuario.setError("Llena este campo");
            return;
        }
        //Validar si existe el correo
        ArrayList correo = db_usuarios.findUsuario(strCorreoUsuario);
        if(correo.size() != 0) {
            edtCorreoUsuario.setError("Este correo ya está registrado");
        }else {
            String estado_sesion = "0";
            //Insertar datos en la base de datos de usuarios, llamando al método InsertDataUsuarios(creado manualmente)
            db_usuarios.InsertDataUsuarios(idEmpresa, strNombresUsuario, strApellidosUsuario, strCedulaUsuario, strDireccionUsuario, strTelefonoUsuario, strCorreoUsuario, strContraseñaUsuario,estado_sesion);
            //Dialogo de alerta estableciendo que se ha registrado correctamente
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivityUsuario.this);
            builder.setTitle("Bien");
            builder.setMessage("Te has registrado correctamente, puedes iniciar sesión");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(RegisterActivityUsuario.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
