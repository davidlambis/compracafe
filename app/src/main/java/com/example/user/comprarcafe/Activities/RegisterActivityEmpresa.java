package com.example.user.comprarcafe.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Handler.HttpHandler;
import com.example.user.comprarcafe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivityEmpresa extends AppCompatActivity implements OnClickListener {

    private EditText edtNombreCompraVenta, edtNit, edtDireccionCompraVenta, edtTelefonoCompraVenta, edtCiudadCompraVenta;
    private Button btnRegistroEmpresa;
    String strNit, strNombreCompraventa, strDireccionCompraVenta, strTelefonoCompraVenta, strCiudadCompraVenta, nit, estadoEmpresa;

    //Instancia del controlador de empresas
    EmpresasController db_empresas;

    /////// ****** /////
    //Variables
    private ProgressDialog pDialog;
    private String departamentoSeleccionado, ciudadSeleccionada;
    Spinner departamentos, ciudades;
    public ArrayAdapter<String> adaptersDepartamentos, adaptersCiudades;
    public ArrayList<String> spinnerDepartamentos, spinnerCiudades;
    ArrayList<String> listdata;

    // URL JSON con datos de departamentos y municipios de Colombia
    private static String url = "http://iot.bitnamiapp.com/iot/colombia.json";

    ///////// ****** //////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empresa);

        ////
        departamentos = (Spinner) findViewById(R.id.departamentos);
        ciudades = (Spinner) findViewById(R.id.ciudades);
        spinnerDepartamentos = new ArrayList<>();
        //Ejecución tarea asíncrona para obtener departamentos de Colombia
        new GetDepartamentos().execute();
        ////

        //Coloca título al toolbar y habilita el retorno a la actividad anterior
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarRegistroEmpresa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("REGISTRO EMPRESA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Conexión al controlador de Empresa
        db_empresas = new EmpresasController(this);
        db_empresas.abrirBaseDeDatos();

        //Llamado a objetos del layout
        edtNombreCompraVenta = (EditText) findViewById(R.id.edtNombreCompraventa);
        edtNit = (EditText) findViewById(R.id.edtNit);
        edtDireccionCompraVenta = (EditText) findViewById(R.id.edtDireccionCompraVenta);
        edtTelefonoCompraVenta = (EditText) findViewById(R.id.edtTelefonoCompraventa);
        btnRegistroEmpresa = (Button) findViewById(R.id.btnRegistroEmpresa);

        //Listener para el botón de registrar empresa
        btnRegistroEmpresa.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (isOnlineNet()) {
            //Valida que haya internet y que todos los campos estén diligenciados correctamente
            strNombreCompraventa = edtNombreCompraVenta.getText().toString();
            if (TextUtils.isEmpty(strNombreCompraventa)) {
                edtNombreCompraVenta.setError("Llena este campo");
                return;
            }
            strNit = edtNit.getText().toString();
            if (TextUtils.isEmpty(strNit)) {
                edtNit.setError("Llena este campo");
                return;
            }
            strDireccionCompraVenta = edtDireccionCompraVenta.getText().toString();
            if (TextUtils.isEmpty(strDireccionCompraVenta)) {
                edtDireccionCompraVenta.setError("Llena este campo");
                return;
            }
            strTelefonoCompraVenta = edtTelefonoCompraVenta.getText().toString();
            if (TextUtils.isEmpty(strTelefonoCompraVenta)) {
                edtTelefonoCompraVenta.setError("Llena este campo");
                return;
            }

            //Ejecuta la tarea asíncrona que valida que una empresa no esté ya registrada en la nube
            new CargarDatosHistoria().execute("http://iot.bitnamiapp.com:3000/unidad_productiva_nit");


        } else {
            //Se muestra un diálogo en caso de que no haya internet
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivityEmpresa.this);
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
    }


    //Método de validación de conexión a internet
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //VALIDACIÓN mediante petición POST que pasa el dato del nit de la unidad productiva y valida en la nube que esté o no registrada
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        Log.i("URl", "" + myurl);
        myurl = myurl.replace(" ", "%20");
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 10000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(90000 /* milliseconds */);
            conn.setConnectTimeout(95000 /* milliseconds */);

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nit_unidad_productiva", strNit);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Respuesta", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    //Mediante método POST, ingresa todos los datos de la unidad productiva en la nube .
    private String downloadUrlP(String myurl) throws IOException {
        InputStream is = null;
        Log.i("URl", "" + myurl);
        myurl = myurl.replace(" ", "%20");
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 20000;

        try {
            URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(90000 /* milliseconds */);
            conn.setConnectTimeout(95000 /* milliseconds */);

            //POSTEA
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nit_unidad_productiva", strNit)
                    .appendQueryParameter("direccion_unidad_productiva", strDireccionCompraVenta)
                    .appendQueryParameter("telefono_unidad_productiva", strTelefonoCompraVenta)
                    .appendQueryParameter("nombre_unidad_productiva", strNombreCompraventa)
                    .appendQueryParameter("ciudad", ciudadSeleccionada)
                    .appendQueryParameter("departamento", departamentoSeleccionado)
                    .appendQueryParameter("proyecto_id", "58d423a18681e2334bdb5f99");
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Respuesta", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    // Lee un inputStream y lo convierte en un String .
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    //Tarea asíncrona para la validación por Nit de que la empresa esté registrada y para ingresarla en caso de que no lo esté
    private class CargarDatosHistoria extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegisterActivityEmpresa.this,
                    "Cargando...", "");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        //
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(new String(result));
                int length = jsonArray.length();
                List<String> listContents = new ArrayList<String>(length);
                ArrayList<HashMap<String, String>> list = new ArrayList<>();
                //Obtiene el nit de la unidad productiva
                for (int i = 0; i < length; i++) {
                    listContents.add(jsonArray.getString(i));
                    JSONObject c = jsonArray.getJSONObject(i);
                    nit = c.getString("nit_unidad_productiva");
                }
                //Si ese nit es distinto de nulo, quiere decir que ya esa empresa está registrada
                if (nit != null) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "No se puede registrar la empresa porque ya aparece registrada", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Ingresa de nuevo los datos", Toast.LENGTH_SHORT).show();
                }
                //Si el nit es nulo, ingresa los datos de la empresa en sqlite, hace llamado a la otra tarea asíncrona para hacer el post de todos los datos de la unidad productiva y pasa a la actividad de registro de usuario, pasando todos los datos a través del intent.
                if (nit == null) {
                    db_empresas.InsertDataEmpresas(strNombreCompraventa, strNit, strDireccionCompraVenta, strTelefonoCompraVenta, departamentoSeleccionado, ciudadSeleccionada);
                    new CargarDatosHistoriaP().execute("http://iot.bitnamiapp.com:3000/unidad_productiva");
                    Toast.makeText(getApplicationContext(), "Datos de empresa registrados correctamente, registra tus datos de usuario para poder iniciar sesión", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), RegisterActivityUsuario.class);
                    i.putExtra("Nombre_Empresa", strNombreCompraventa);
                    i.putExtra("Nit_Empresa", strNit);
                    i.putExtra("Direccion_Empresa", strDireccionCompraVenta);
                    i.putExtra("Telefono_Empresa", strTelefonoCompraVenta);
                    i.putExtra("Departamento_Empresa", departamentoSeleccionado);
                    i.putExtra("Ciudad_Empresa", ciudadSeleccionada);
                    startActivity(i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Tarea asíncrona para el POST de los datos de la unidad productiva.
    private class CargarDatosHistoriaP extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegisterActivityEmpresa.this,
                    "Cargando...", "");
            progressDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrlP(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }

    }


    //////////
    //Tarea asíncrona para obtener el listado de departamentos de Colombia en base a una URL con los datos en formato JSON
    private class GetDepartamentos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterActivityEmpresa.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String departamento = c.getString("departamento");
                        spinnerDepartamentos.add(departamento);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            // Pone los departamentos obtenidos en el spinner de departamentos de la actividad
            adaptersDepartamentos = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, spinnerDepartamentos);
            departamentos.setAdapter(adaptersDepartamentos);

            departamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //De acuerdo al departamento que seleccione el usuario ejecutará la tarea asíncrona para obtener los municipios del mismo.
                    departamentoSeleccionado = spinnerDepartamentos.get(position);
                    new GetCiudadesByDepartamento().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    //Tarea asíncrona para obtener los municipios del departamento seleccionado por el usuario y asignarlos en el spinner de municipios.
    private class GetCiudadesByDepartamento extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisterActivityEmpresa.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        if (c.getString("departamento").equals(departamentoSeleccionado)) {
                            JSONArray ciudades = c.getJSONArray("ciudades");
                            listdata = new ArrayList<String>();
                            for (int j = 0; j < ciudades.length(); j++) {
                                listdata.add(ciudades.getString(j));
                            }
                        }
                    }


                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Los municipios del departamento seleccionado son asignados al spinner de municipios
            adaptersCiudades = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, listdata);
            ciudades.setAdapter(adaptersCiudades);

            ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Obtiene el dato del municipio seleccionado
                    ciudadSeleccionada = listdata.get(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        }

    }


    //////////

}

