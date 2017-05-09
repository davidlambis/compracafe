package com.example.user.comprarcafe.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Handler.HttpHandler;
import com.example.user.comprarcafe.Models.Empresa;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivityEmpresa extends AppCompatActivity implements OnClickListener{

    private EditText edtNombreCompraVenta, edtNit, edtDireccionCompraVenta, edtTelefonoCompraVenta,edtCiudadCompraVenta;
    private Button btnRegistroEmpresa;
    String strNit,strNombreCompraventa,strDireccionCompraVenta,strTelefonoCompraVenta,strCiudadCompraVenta,nit,estadoEmpresa;

    //Instancia del controlador de empresas
    EmpresasController db_empresas;

   /////// ****** /////
   private ProgressDialog pDialog;

    private String departamentoSeleccionado,ciudadSeleccionada;
    Spinner departamentos, ciudades;
    public ArrayAdapter<String> adaptersDepartamentos , adaptersCiudades;
    public ArrayList<String> spinnerDepartamentos, spinnerCiudades;
    ArrayList<String> listdata;

    // URL JSON
    private static String url = "http://iot.bitnamiapp.com/iot/colombia.json";

    ///////// ****** //////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empresa);

        ////
        departamentos = (Spinner)findViewById(R.id.departamentos);
        ciudades = (Spinner)findViewById(R.id.ciudades);
        spinnerDepartamentos = new ArrayList<>();
        new GetDepartamentos().execute();
        ////

        Toolbar toolbar = (Toolbar)findViewById(R.id.ToolbarRegistroEmpresa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("REGISTRO EMPRESA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Conexión al controlador de Empresa
        db_empresas = new EmpresasController(this);
        db_empresas.abrirBaseDeDatos();


        edtNombreCompraVenta = (EditText) findViewById(R.id.edtNombreCompraventa);
        edtNit = (EditText) findViewById(R.id.edtNit);
        edtDireccionCompraVenta = (EditText) findViewById(R.id.edtDireccionCompraVenta);
        edtTelefonoCompraVenta = (EditText) findViewById(R.id.edtTelefonoCompraventa);
        btnRegistroEmpresa = (Button) findViewById(R.id.btnRegistroEmpresa);

        btnRegistroEmpresa.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
     if(isOnlineNet()) {
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

         //Validar si existe la empresa en sqlite
        /*ArrayList empresa = db_empresas.findEmpresa(strNombreCompraventa);
        if(empresa.size() != 0){
            edtNombreCompraVenta.setError("Ya está registrada");
        }else {*/
         //Insertar datos en la base de datos de Empresa, llamando al método InsertDataEmpresas
         //if(isOnlineNet()) {
         new CargarDatosHistoria().execute("http://iot.bitnamiapp.com:3000/unidad_productiva_nit");

         //
         //}else{
         //estadoEmpresa="0";
         //db_empresas.InsertDataEmpresas(strNombreCompraventa, strNit, strDireccionCompraVenta, strTelefonoCompraVenta);
         //Toast.makeText(this, "Datos de empresa registrados correctamente, registra tus datos de usuario para poder iniciar sesión", Toast.LENGTH_LONG).show();
         //Toast.makeText(this, "No se han podido registrar los datos en la nube porque no hay acceso a internet", Toast.LENGTH_SHORT).show();
         //Intent i = new Intent(this, RegisterActivityUsuario.class);
         //startActivity(i);

         //db_empresas.InsertDataEmpresas(strNombreCompraventa, strNit, strDireccionCompraVenta, strTelefonoCompraVenta);
            /*Toast.makeText(this, "Datos de empresa registrados correctamente, registra tus datos de usuario para poder iniciar sesión", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this,RegisterActivityUsuario .class);
            startActivity(i);*/
         //}
         //}
     }else{
         //Se muestra un alert dialog que indica que los datos son erróneos
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

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    //VALIDACIÖN
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

            //POSTEA
            conn.setRequestMethod("POST");
            //conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nit_unidad_productiva", strNit)
                    /*.appendQueryParameter("direccion_unidad_productiva", strDireccionCompraVenta)
                    .appendQueryParameter("telefono_unidad_productiva", strTelefonoCompraVenta)
                    .appendQueryParameter("nombre_unidad_productiva", strNombreCompraventa)*/;
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

    //POSTEO
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
            //conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nit_unidad_productiva", strNit)
                    .appendQueryParameter("direccion_unidad_productiva", strDireccionCompraVenta)
                    .appendQueryParameter("telefono_unidad_productiva", strTelefonoCompraVenta)
                    .appendQueryParameter("nombre_unidad_productiva", strNombreCompraventa)
                    .appendQueryParameter("ciudad",ciudadSeleccionada)
                    .appendQueryParameter("departamento",departamentoSeleccionado)
                    .appendQueryParameter("proyecto_id","58d423a18681e2334bdb5f99");
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


    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private class CargarDatosHistoria extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegisterActivityEmpresa.this,
                    "Cargando...","");
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

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(new String(result));
                int length = jsonArray.length();
                List<String> listContents = new ArrayList<String>(length);
                ArrayList<HashMap<String, String>> list = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    listContents.add(jsonArray.getString(i));
                    JSONObject c = jsonArray.getJSONObject(i);
                    nit = c.getString("nit_unidad_productiva");
                }
                if(nit != null){
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"No se puede registrar la empresa porque ya aparece registrada",Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"Ingresa de nuevo los datos",Toast.LENGTH_SHORT).show();
                }
                if(nit == null){
                    //estadoEmpresa = "1"; // Ya se han subido los datos a la nube.
                    db_empresas.InsertDataEmpresas(strNombreCompraventa,strNit,strDireccionCompraVenta,strTelefonoCompraVenta,departamentoSeleccionado,ciudadSeleccionada);
                    new CargarDatosHistoriaP().execute("http://iot.bitnamiapp.com:3000/unidad_productiva");
                    //Toast.makeText(getApplicationContext(),"Ciudad:"+strCiudadCompraVenta,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Datos de empresa registrados correctamente, registra tus datos de usuario para poder iniciar sesión", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(),RegisterActivityUsuario .class);
                    i.putExtra("Nombre_Empresa",strNombreCompraventa);
                    i.putExtra("Nit_Empresa",strNit);
                    i.putExtra("Direccion_Empresa",strDireccionCompraVenta);
                    i.putExtra("Telefono_Empresa",strTelefonoCompraVenta);
                    i.putExtra("Departamento_Empresa",departamentoSeleccionado);
                    i.putExtra("Ciudad_Empresa",ciudadSeleccionada);
                    startActivity(i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //POSTEO
    private class CargarDatosHistoriaP extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegisterActivityEmpresa.this,
                    "Cargando...","");
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
                }catch (final JSONException e) {
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

            adaptersDepartamentos = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item,spinnerDepartamentos);
            departamentos.setAdapter(adaptersDepartamentos);

            departamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    departamentoSeleccionado = spinnerDepartamentos.get(position);
                    new GetCiudadesByDepartamento().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

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
                        if(c.getString("departamento").equals(departamentoSeleccionado)){
                            JSONArray ciudades = c.getJSONArray("ciudades");
                            listdata = new ArrayList<String>();
                            for (int j = 0; j < ciudades.length();j++) {
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

            adaptersCiudades = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_item,listdata);
            ciudades.setAdapter(adaptersCiudades);

            ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ciudadSeleccionada = listdata.get(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

        }

    }



    //////////

}

