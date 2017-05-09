package com.example.user.comprarcafe.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.user.comprarcafe.Models.Empresa;
import com.example.user.comprarcafe.Models.Usuario;
import com.example.user.comprarcafe.R;
import com.example.user.comprarcafe.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

    ///
    ProgressDialog pDialog;
    int length;
    String strCorreo,strContraseña;
    String Id_Empresa,Nombres_Usuario,Apellidos_Usuario,Cedula_Usuario,Direccion_Usuario,Telefono_Usuario,Correo_Usuario,Contrasena_Usuario,tipoCafe,Estado_Sesion,Nombre_Empresa,Nit_Empresa,Direccion_Empresa,Telefono_Empresa,Departamento_Empresa,Ciudad_Empresa;
    ArrayList<Empresa> numero_empresas;

    //SESSION MANAGER
    //SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Session Manager
        /*session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status login: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        session.checkLoginl();*/


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

      /*  usuarios= db_usuarios.loadUsuarios();
        if(usuarios.size()!= 0) {
            verificar_estado_sesion();
        } */
    }

    /* private void verificar_estado_sesion() {
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
    } */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIniciarSesion:
                    strCorreo = edtCorreo.getText().toString();
                    if (TextUtils.isEmpty(strCorreo)) {
                        edtCorreo.setError("Llena este campo");
                        return;
                    }
                    strContraseña = edtContraseña.getText().toString();
                    if (TextUtils.isEmpty(strContraseña)) {
                        edtContraseña.setError("Llena este campo");
                        return;
                    }


                    //Hacer llamado al cursor para hacer la consulta en la base de datos

                    //TODO VALIDAR LOGIN EN LA NUBE
                    /*cursor = db_usuarios.validarLogin(strCorreo,strContraseña);
                    if (cursor != null) {
                        //Si el cursor retorna más de cero, ha encontrado respuesta
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                                _id = db_usuarios.idUsuarioByCorreo(strCorreo);
                                _nombres= db_usuarios.nombresUsuarioByCorreo(strCorreo);
                                _apellidos=db_usuarios.apellidosUsuarioByCorreo(strCorreo);
                                idEmpresa = db_usuarios.idEmpresaByCorreo(strCorreo);
                                nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);
                                direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
                                telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
                                nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);

                                estadoEmpresa = db_empresas.findEstadoEmpresaById(idEmpresa);
                                if(estadoEmpresa.equals("0")){
                                    if(isOnlineNet()){
                                        new CargarDatosHistoria().execute("http://iot.bitnamiapp.com:3000/unidad_productiva_nit");
                                        new CargarDatosHistoriaP().execute("http://iot.bitnamiapp.com:3000/unidad_productiva");
                                    }
                                }
                                    //Toast.makeText(this,"idusuario:"+_id+"nombresUsuario:"+_nombres+"apellidosUsuario:"+_apellidos+"idEmpresa:"+idEmpresa,Toast.LENGTH_LONG).show();
                                Toast.makeText(this, "Inicio de sesión exitoso por sqlite", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                i.putExtra("id",_id);
                                i.putExtra("nombres",_nombres);
                                i.putExtra("apellidos",_apellidos);
                                i.putExtra("idEmpresa",idEmpresa);
                                startActivity(i);
                                /* String Estado_Sesion="1";
                                db_usuarios.actualizarUsuario(id_usuario_logued, Estado_Sesion);
                                enviardatos();


                        }else{ */
                            new GetLogin().execute();
                        //} // Si el cursor no encuentra respuesta
                        /*else {
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
                        }*/

                    break;

            case R.id.btnRegistrarEmpresa :
                if(isOnlineNet()) {
                    ArrayList<Empresa> numero_empresas  = db_empresas.findAllEmpresas();
                    if(numero_empresas.size() > 0){
                        Toast.makeText(this,"Ya hay una empresa registrada en este dispositivo",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent i = new Intent(this, RegisterActivityEmpresa.class);
                        startActivity(i);
                        Toast.makeText(this, "Ingresa datos de la compraventa", Toast.LENGTH_SHORT).show();
                    }
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
                ArrayList<Empresa> numero_empresas  = db_empresas.findAllEmpresas();
                if(numero_empresas.size() == 0) {
                    Toast.makeText(this, "Primero, registra una compraventa", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, RegisterActivityEmpresa.class);
                    startActivity(i);
                }else {
                    ArrayList<Usuario> numero_usuarios = db_usuarios.findAllUsuarios();
                    if (numero_usuarios.size() > 0) {
                        Toast.makeText(this, "Ya hay un usuario registrado en este dispositivo", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent j = new Intent(this, RegisterActivityUsuario.class);
                        startActivity(j);
                        Toast.makeText(this, "Ingresa tus datos de usuario", Toast.LENGTH_SHORT).show();
                    }
                }
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


/////////
    private class GetLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String jsonStr = makeServiceCallLogin("http://iot.bitnamiapp.com:3000/usuario_correo_contrasena");

            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    length = jsonArray.length();
                    for(int i=0; i<length; i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        Id_Empresa = c.getString("Id_Empresa");
                        Nombres_Usuario = c.getString("Nombres_Usuario");
                        Apellidos_Usuario = c.getString("Apellidos_Usuario");
                        Cedula_Usuario = c.getString("Cedula_Usuario");
                        Direccion_Usuario = c.getString("Direccion_Usuario");
                        Telefono_Usuario = c.getString("Telefono_Usuario");
                        Correo_Usuario = c.getString("Correo_Usuario");
                        Contrasena_Usuario = c.getString("Contrasena_Usuario");
                        //tipoCafe = c.getString("Tipo_Cafe");
                        Estado_Sesion = c.getString("Estado_Sesion");
                        Nombre_Empresa = c.getString("Nombre_Empresa");
                        Nit_Empresa = c.getString("Nit_Empresa");
                        Direccion_Empresa = c.getString("Direccion_Empresa");
                        Telefono_Empresa = c.getString("Telefono_Empresa");
                        Departamento_Empresa = c.getString("Departamento_Empresa");
                        Ciudad_Empresa = c.getString("Ciudad_Empresa");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        pDialog.dismiss();
        if (length == 0) {
            Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
        }else {
            //session.createLoginSession(Correo_Usuario,Contrasena_Usuario);
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            Toast.makeText(getApplicationContext(),"Inicio de sesión exitoso",Toast.LENGTH_SHORT).show();
            db_usuarios.InsertDataUsuarios(idEmpresa, Nombres_Usuario, Apellidos_Usuario, Cedula_Usuario, Direccion_Usuario, Telefono_Usuario, Correo_Usuario, Contrasena_Usuario, estado_sesion);
            //long id_Empresa = Long.valueOf(Id_Empresa);
            //i.putExtra("idEmpresa", id_Empresa);
            i.putExtra("nombres", Nombres_Usuario);
            i.putExtra("apellidos", Apellidos_Usuario);
            i.putExtra("Cedula_Usuario", Cedula_Usuario);
            i.putExtra("Direccion_Usuario", Direccion_Usuario);
            i.putExtra("Telefono_Usuario", Telefono_Usuario);
            i.putExtra("Correo_Usuario", Correo_Usuario);
            i.putExtra("Contrasena_Usuario", Contrasena_Usuario);
            i.putExtra("tipoCafe", tipoCafe);
            i.putExtra("Nombre_Empresa", Nombre_Empresa);
            i.putExtra("Nit_Empresa", Nit_Empresa);
            i.putExtra("Direccion_Empresa", Direccion_Empresa);
            i.putExtra("Telefono_Empresa", Telefono_Empresa);
            i.putExtra("Departamento_Empresa", Departamento_Empresa);
            i.putExtra("Ciudad_Empresa", Ciudad_Empresa);
            startActivity(i);
            finish();
        }
    }

}
    public String makeServiceCallLogin(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Correo_Usuario", strCorreo)
                    .appendQueryParameter("Contrasena_Usuario", strContraseña);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            //Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            //Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            //Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            //Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }




}
