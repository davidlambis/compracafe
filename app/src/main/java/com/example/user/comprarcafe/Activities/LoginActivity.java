package com.example.user.comprarcafe.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Controllers.UsuariosController;
import com.example.user.comprarcafe.Models.Usuario;
import com.example.user.comprarcafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements OnClickListener{

    //Llamados de elementos del layout
    private Button btnIniciarSesion,btnRegistrarEmpresa,btnRegistrarUsuario;
    private EditText edtCorreo;
    private EditText edtContraseña;
    ArrayList<Usuario> usuarios;

    //Llamados para hacer uso de la base de datos
    SQLiteOpenHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    private String estado_sesion_logued,nombres_usuario_logued, apellidos_usuario_logued,direccion_usuario_logued,correo_usuario_logued,contraseña_usuario_logued,estado_sesion, _nombres,_apellidos,nombreEmpresa,direccionEmpresa,telefonoEmpresa,nitEmpresa,estadoEmpresa,nit;
    public int cedula_usuario_logued,telefono_usuario_logued;
    private long id_usuario_logued,_id,idEmpresa;

    UsuariosController db_usuarios;
    EmpresasController db_empresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtCorreo = (EditText)findViewById(R.id.edtCorreo);
        edtContraseña = (EditText)findViewById(R.id.edtContraseña);
        btnIniciarSesion = (Button)findViewById(R.id.btnIniciarSesion);
        btnRegistrarEmpresa = (Button)findViewById(R.id.btnRegistrarEmpresa);
        btnRegistrarUsuario = (Button)findViewById(R.id.btnRegistrarUsuario);

        //Listener del botón de iniciar Sesión
        btnIniciarSesion.setOnClickListener(this);
        //Listener del botón de registrar empresa
        btnRegistrarEmpresa.setOnClickListener(this);
        //Listener del botón de registrar usuario
        btnRegistrarUsuario.setOnClickListener(this);

        //Abrir acceso a controlador de usuarios
        db_usuarios = new UsuariosController(this);
        db_usuarios.abrirBaseDeDatos();

        //Abrir acceso al controlador de empresas
        db_empresas = new EmpresasController(this);
        db_empresas.abrirBaseDeDatos();

        usuarios= db_usuarios.loadUsuarios();
        if(usuarios.size()!= 0) {
            verificar_estado_sesion();
        }
    }

    private void verificar_estado_sesion() {
        listToDo();
        if(estado_sesion_logued.equals("1")) {
                enviardatos();
        }
    }

    private void enviardatos(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", _id);
        intent.putExtra("nombres", _nombres);
        intent.putExtra("apellidos", _apellidos);
        intent.putExtra("idEmpresa", idEmpresa);
        intent.putExtra("id_usuario_logued",id_usuario_logued);
        intent.putExtra("nombres_usuario_logued",nombres_usuario_logued);
        intent.putExtra("apellidos_usuario_logued",apellidos_usuario_logued);
        intent.putExtra("cedula_usuario_logued",cedula_usuario_logued);
        intent.putExtra("direccion_usuario_logued",direccion_usuario_logued);
        intent.putExtra("telefono_usuario_logued",telefono_usuario_logued);
        intent.putExtra("correo_usuario_logued",correo_usuario_logued);
        intent.putExtra("contraseña_usuario_logued",contraseña_usuario_logued);
        intent.putExtra("estado_sesion_logued",estado_sesion_logued);
        id_usuario_logued = 0;
        nombres_usuario_logued = "";
        apellidos_usuario_logued = "";
        cedula_usuario_logued = 0;
        direccion_usuario_logued = "";
        telefono_usuario_logued = 0;
        correo_usuario_logued= "";
        contraseña_usuario_logued = "";
        estado_sesion = "";
        edtCorreo.setText("");
        edtContraseña.setText("");
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIniciarSesion:
                    String strCorreo = edtCorreo.getText().toString();
                    if (TextUtils.isEmpty(strCorreo)) {
                        edtCorreo.setError("Llena este campo");
                        return;
                    }
                    String strContraseña = edtContraseña.getText().toString();
                    if (TextUtils.isEmpty(strContraseña)) {
                        edtContraseña.setError("Llena este campo");
                        return;
                    }
                    //Hacer llamado al cursor para hacer la consulta en la base de datos
                    cursor = db_usuarios.validarLogin(strCorreo,strContraseña);
                    if (cursor != null) {
                        //Si el cursor retorna más de cero, ha encontrado respuesta
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                                _id = db_usuarios.idUsuarioByCorreo(strCorreo);
                                _nombres= db_usuarios.nombresUsuarioByCorreo(strCorreo);
                                _apellidos=db_usuarios.apellidosUsuarioByCorreo(strCorreo);
                                idEmpresa = db_usuarios.idEmpresaByCorreo(strCorreo);
                                /*nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);
                                direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
                                telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
                                nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);

                                estadoEmpresa = db_empresas.findEstadoEmpresaById(idEmpresa);
                                if(estadoEmpresa.equals("0")){
                                    if(isOnlineNet()){
                                        new CargarDatosHistoria().execute("http://iot.bitnamiapp.com:3000/unidad_productiva_nit");
                                        new CargarDatosHistoriaP().execute("http://iot.bitnamiapp.com:3000/unidad_productiva");
                                    }
                                }*/
                                    //Toast.makeText(this,"idusuario:"+_id+"nombresUsuario:"+_nombres+"apellidosUsuario:"+_apellidos+"idEmpresa:"+idEmpresa,Toast.LENGTH_LONG).show();
                                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                String Estado_Sesion="1";
                                db_usuarios.actualizarUsuario(id_usuario_logued, Estado_Sesion);
                                enviardatos();


                        } // Si el cursor no encuentra respuesta
                        else {
                            //Se muestra un alert dialog que indica que los datos son erróneos
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Alerta");
                            builder.setMessage("Usuario o Contraseña incorrectos");
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    break;

            case R.id.btnRegistrarEmpresa :
                if(isOnlineNet()) {
                    Intent i = new Intent(this, RegisterActivityEmpresa.class);
                    startActivity(i);
                    Toast.makeText(this, "Ingresa datos de la compraventa", Toast.LENGTH_SHORT).show();
                }else{
                    //Se muestra un alert dialog que indica que los datos son erróneos
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Alerta");
                    builder.setMessage("No hay conexión a internet, por favor busca un acceso a internet");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.btnRegistrarUsuario :
                Intent j = new Intent(this, RegisterActivityUsuario.class);
                startActivity(j);
                Toast.makeText(this,"Ingresa tus datos de usuario",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void listToDo(){
        for (Usuario usuario : usuarios){
            Usuario usuario1 = new Usuario();
            usuario1.setIdUsuario(usuario.getIdUsuario());
            usuario1.setNombres(usuario.getNombres());
            usuario1.setApellidos(usuario.getApellidos());
            usuario1.setIdEmpresa(usuario.getIdEmpresa());
            usuario1.setCedula(usuario.getCedula());
            usuario1.setDireccion(usuario.getDireccion());
            usuario1.setTelefono(usuario.getTelefono());
            usuario1.setCorreo(usuario.getCorreo());
            usuario1.setContraseña(usuario.getContraseña());
            usuario1.setEstadoSesion(usuario.getEstadoSesion());

            _id = usuario.getIdUsuario();
            _nombres = usuario.getNombres();
            _apellidos = usuario.getApellidos();
            idEmpresa = usuario.getIdEmpresa();
            id_usuario_logued = usuario.getIdUsuario();
            nombres_usuario_logued = usuario.getNombres();
            apellidos_usuario_logued = usuario.getApellidos();
            cedula_usuario_logued = usuario.getCedula();
            direccion_usuario_logued = usuario.getDireccion();
            telefono_usuario_logued = usuario.getTelefono();
            correo_usuario_logued = usuario.getCorreo();
            contraseña_usuario_logued = usuario.getContraseña();
            estado_sesion_logued = usuario.getEstadoSesion();

        }
    }

}
