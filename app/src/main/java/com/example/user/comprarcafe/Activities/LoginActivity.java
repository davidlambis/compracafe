package com.example.user.comprarcafe.Activities;

import android.app.ProgressDialog;
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

    private String estado_sesion;
    private long idEmpresa;

    //Llamado de controladores
    UsuariosController db_usuarios;
    EmpresasController db_empresas;


    ///Variables
    ProgressDialog pDialog;
    int length;
    String strCorreo,strContraseña;
    String Id_Empresa,Nombres_Usuario,Apellidos_Usuario,Cedula_Usuario,Direccion_Usuario,Telefono_Usuario,Correo_Usuario,Contrasena_Usuario,tipoCafe,Estado_Sesion,Nombre_Empresa,Nit_Empresa,Direccion_Empresa,Telefono_Empresa,Departamento_Empresa,Ciudad_Empresa;
    ArrayList<Empresa> numero_empresas;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Llamado de objetos del layout
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


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //En caso de que el usuario haga click en botón de iniciar sesión.
            case R.id.btnIniciarSesion:
                    //Valida que los campos estén diligenciados
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
                    // Ejecuta la tarea asíncrona
                            new GetLogin().execute();

                    break;
            //En caso de que el usuario haga click en el botón de registrar empresa.
            case R.id.btnRegistrarEmpresa :
                //Llamado a función que valida que haya internet
                if(isOnlineNet()) {
                    //Almacena en un arraylist el listado de empresas que encuentre en la base de datos sqlite
                    ArrayList<Empresa> numero_empresas  = db_empresas.findAllEmpresas();
                    //Si el tamaño del array es mayor que 0 quiere decir que ya hay una empresa registrada(solo se permite una por dispositivo)
                    if(numero_empresas.size() > 0){
                        Toast.makeText(this,"Ya hay una empresa registrada en este dispositivo",Toast.LENGTH_SHORT).show();
                    }else {
                        //De lo contrario ingresa a la actividad RegisterActivityEmpresa
                        Intent i = new Intent(this, RegisterActivityEmpresa.class);
                        startActivity(i);
                        Toast.makeText(this, "Ingresa datos de la compraventa", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //Si no hay internet, aparece un diálogo
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
            //En caso de que el usuario haga click en el botón de registrar usuario.
            case R.id.btnRegistrarUsuario :
                ArrayList<Empresa> numero_empresas  = db_empresas.findAllEmpresas();
                //Si no hay una empresa registrada , redirige a la pantalla de registro de empresa
                if(numero_empresas.size() == 0) {
                    Toast.makeText(this, "Primero, registra una compraventa", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, RegisterActivityEmpresa.class);
                    startActivity(i);
                }else {
                    ArrayList<Usuario> numero_usuarios = db_usuarios.findAllUsuarios();
                    // Si hay un usuario registrado en el dispositivo no permite registrar otro
                    if (numero_usuarios.size() > 0) {
                        Toast.makeText(this, "Ya hay un usuario registrado en este dispositivo", Toast.LENGTH_SHORT).show();
                    }else {
                        //De lo contrario, procede a la pantalla de registro de datos de usuario
                        Intent j = new Intent(this, RegisterActivityUsuario.class);
                        startActivity(j);
                        Toast.makeText(this, "Ingresa tus datos de usuario", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    //Método para validar la conexión a internet
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


/////////
    //Tarea asíncrona para traer datos del usuario de la nube.
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
            //Dirección Url del servicio
            String jsonStr = makeServiceCallLogin("http://iot.bitnamiapp.com:3000/usuario_correo_contrasena");

            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    //Tamaño del array
                    length = jsonArray.length();
                    //Trae todos los datos almacenados en la nube, de acuerdo al correo y contraseña ingresado
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
        //Si el tamaño del array es cero quiere decir que el correo o contraseña ingresados están incorrectos
        if (length == 0) {
            Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
        }else {
            //Inicia la MainActivity , guarda los datos en sqlite y pasa todos ellos a la siguiente actividad
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            Toast.makeText(getApplicationContext(),"Inicio de sesión exitoso",Toast.LENGTH_SHORT).show();
            db_usuarios.InsertDataUsuarios(idEmpresa, Nombres_Usuario, Apellidos_Usuario, Cedula_Usuario, Direccion_Usuario, Telefono_Usuario, Correo_Usuario, Contrasena_Usuario, estado_sesion);
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
    //Método para hacer una petición POST que permita mediante el correo y contraseña ingresados por el usuario , traer la información almacenada en la nube, mediante el servicio que ha sido creado.
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


    //Método llamado en la petición POST
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
