package com.example.user.comprarcafe.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.List;

public class RegisterActivityUsuario extends AppCompatActivity implements OnClickListener {

    //Variables
    private EditText edtNombresUsuario, edtApellidosUsuario, edtCedulaUsuario, edtDireccionUsuario, edtTelefonoUsuario, edtCorreoUsuario, edtContraseñaUsuario;
    private Button btnRegistroUsuario;
    private String strNombresUsuario,strApellidosUsuario,strCedulaUsuario,strDireccionUsuario,strTelefonoUsuario,strCorreoUsuario,strContraseñaUsuario,estado_sesion,strIdEmpresa,correo;
    private String nombreEmpresa,nitEmpresa,direccionEmpresa,telefonoEmpresa,departamentoEmpresa,ciudadEmpresa;
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

    ArrayList<Empresa> numero_empresas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_usuario);

        //Coloca título al toolbar y habilita el retorno a la actividad anterior
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarRegistroUsuario);
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

        numero_empresas = db_empresas.findAllEmpresas();

        //Llamado a objetos del layout
        edtNombresUsuario = (EditText) findViewById(R.id.edtNombresUsuario);
        edtApellidosUsuario = (EditText) findViewById(R.id.edtApellidosUsuario);
        edtCedulaUsuario = (EditText) findViewById(R.id.edtCedulaUsuario);
        edtDireccionUsuario = (EditText) findViewById(R.id.edtDireccionUsuario);
        edtTelefonoUsuario = (EditText) findViewById(R.id.edtTelefonoUsuario);
        edtCorreoUsuario = (EditText) findViewById(R.id.edtCorreoUsuario);
        edtContraseñaUsuario = (EditText) findViewById(R.id.edtContraseñaUsuario);
        btnRegistroUsuario = (Button) findViewById(R.id.btnRegistroUsuario);
        spinner = (Spinner) findViewById(R.id.spinnerU);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Obtiene el id de empresa que se muestra en el spinner
                idEmpresa = listEmpresa.get(position).getIdEmpresa();
                usuario = new Usuario();
                usuario.setIdEmpresa(idEmpresa);
                strIdEmpresa = Long.toString(idEmpresa);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerEmpresas = new ArrayList<>();
        refresh();

        //Si el usuario da click en registrar usuario sin haber registrado una empresa, redirecciona a la pantalla de registro de empresa
        adaptersEmpresas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerEmpresas);
        if (adaptersEmpresas.getCount() == 0) {
            Toast.makeText(this, "No se ha registrado ninguna empresa,regístra tu compraventa para crear usuario", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, RegisterActivityEmpresa.class);
            startActivity(i);
            finish();
        }

        spinner.setAdapter(adaptersEmpresas);
        //Listener del boton de registrar usuario
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
            spinnerEmpresas.add(empresa.getNombreEmpresa());
        }
    }

    @Override
    public void onClick(View v) {
        //Valida que todos los campos estén diligenciados correctamente
        strNombresUsuario = edtNombresUsuario.getText().toString();
        if (TextUtils.isEmpty(strNombresUsuario)) {
            edtNombresUsuario.setError("Llena este campo");
            return;
        }
        strApellidosUsuario = edtApellidosUsuario.getText().toString();
        if (TextUtils.isEmpty(strApellidosUsuario)) {
            edtApellidosUsuario.setError("Llena este campo");
            return;
        }
        strCedulaUsuario = edtCedulaUsuario.getText().toString();
        if (TextUtils.isEmpty(strCedulaUsuario)) {
            edtCedulaUsuario.setError("Llena este campo");
            return;
        }
        strDireccionUsuario = edtDireccionUsuario.getText().toString();
        if (TextUtils.isEmpty(strDireccionUsuario)) {
            edtDireccionUsuario.setError("Llena este campo");
            return;
        }
        strTelefonoUsuario = edtTelefonoUsuario.getText().toString();
        if (TextUtils.isEmpty(strTelefonoUsuario)) {
            edtTelefonoUsuario.setError("Llena este campo");
            return;
        }
        strCorreoUsuario = edtCorreoUsuario.getText().toString();
        if (TextUtils.isEmpty(strCorreoUsuario)) {
            edtCorreoUsuario.setError("Llena este campo");
            return;
        }

        strContraseñaUsuario = edtContraseñaUsuario.getText().toString();
        if (TextUtils.isEmpty(strContraseñaUsuario)) {
            edtContraseñaUsuario.setError("Llena este campo");
            return;
        }
        //Validar si existe el correo
        if(numero_empresas.size() > 0) {
            nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);
            nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);
            direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
            telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
            departamentoEmpresa = db_empresas.findDepartamentoEmpresaById(idEmpresa);
            ciudadEmpresa = db_empresas.findCiudadEmpresaById(idEmpresa);
        }

        estado_sesion = "0";
        //Ejecuta la tarea asíncrona que valida que el correo esté registrado.
        new validaCorreoUsuario().execute();

    }

    //Valida si el correo ya está registrado
    private class validaCorreoUsuario extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegisterActivityUsuario.this,
                    "Cargando...", "");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... urls) {
            String jsonStr = makeServiceCallUsuariosCorreo("http://iot.bitnamiapp.com:3000/usuario_correo");
            if(jsonStr != null){
                try{
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    int length = jsonArray.length();
                    List<String> listContents = new ArrayList<String>(length);
                    for (int i = 0; i < length; i++) {
                        listContents.add(jsonArray.getString(i));
                        JSONObject c = jsonArray.getJSONObject(i);
                        correo = c.getString("Correo_Usuario");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            //Si correo es null quiere decir que no ha sido registrado
            if(correo == null){
                String estado_sesion = "0";
                //Insertar datos en la base de datos de usuarios, llamando al método InsertDataUsuarios(creado manualmente)
                new posteaUsuario().execute();
                db_usuarios.InsertDataUsuarios(idEmpresa, strNombresUsuario, strApellidosUsuario, strCedulaUsuario, strDireccionUsuario, strTelefonoUsuario, strCorreoUsuario, strContraseñaUsuario, estado_sesion);
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

            if(correo != null) {
                //Si el correo es distinto de null, quiere decir que ya ha sido registrado
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"No se puede registrar el usuario porque ya está registrado",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Ingresa de nuevo los datos",Toast.LENGTH_SHORT).show();
            }


        }


    }

    //Hace un POST con el dato de correo ingresado por el usuario
     public String makeServiceCallUsuariosCorreo(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Correo_Usuario", strCorreoUsuario);
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

    //POSTEO DE DATOS DE USUARIOS
    // Tare asíncrona de POSTEO o subida
    private class posteaUsuario extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            return makeServiceCallUsuarios("http://iot.bitnamiapp.com:3000/usuario");
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //progressDialog.dismiss();
        }

    }

    // Método que pasa todos los parámetros obtenidos del usuario a la nube
    public String makeServiceCallUsuarios(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Id_Empresa", strIdEmpresa)
                    .appendQueryParameter("Nombres_Usuario", strNombresUsuario)
                    .appendQueryParameter("Apellidos_Usuario", strApellidosUsuario)
                    .appendQueryParameter("Cedula_Usuario", strCedulaUsuario)
                    .appendQueryParameter("Direccion_Usuario", strDireccionUsuario)
                    .appendQueryParameter("Telefono_Usuario", strTelefonoUsuario)
                    .appendQueryParameter("Correo_Usuario", strCorreoUsuario)
                    .appendQueryParameter("Contrasena_Usuario", strContraseñaUsuario)
                    .appendQueryParameter("Estado_Sesion", estado_sesion)
                    .appendQueryParameter("Nombre_Empresa",nombreEmpresa)
                    .appendQueryParameter("Nit_Empresa",nitEmpresa)
                    .appendQueryParameter("Direccion_Empresa",direccionEmpresa)
                    .appendQueryParameter("Telefono_Empresa",telefonoEmpresa)
                    .appendQueryParameter("Departamento_Empresa",departamentoEmpresa)
                    .appendQueryParameter("Ciudad_Empresa",ciudadEmpresa);

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
