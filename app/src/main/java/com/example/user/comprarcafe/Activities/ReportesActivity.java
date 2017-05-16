package com.example.user.comprarcafe.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.FacturasController;
import com.example.user.comprarcafe.Handler.HttpHandler;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ReportesActivity extends AppCompatActivity {

    //Variables
    public long idEmpresa;
    public String nombreEmpresa, nitEmpresa, hora;
    TextView tvPrueba, tvKilosTotales, tvDineroGastado;
    String fecha, nit, fecha1, strFormatvalorPagoSeco;
    int dia, mes, año, length;

    Button btnFecha;
    TextView tvFechaSeleccionada;

    //Instancia del controlador de facturas
    FacturasController db_facturas;


    //Llamado a String de la clase HttpHandler
    private static final String TAG = HttpHandler.class.getSimpleName();
    private ProgressDialog pDialog;
    //Definición de la Url para el servicio
    private static String url = "http://iot.bitnamiapp.com:3000/factura_nit_fechaventa";
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    //Variables Double
    Double kilos = 0.0;
    Double valorpago = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        //Coloca título al toolbar y habilita el retorno a la actividad anterior
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarReportes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("REPORTE DE FACTURAS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Conexión al controlador de Facturas
        db_facturas = new FacturasController(this);
        db_facturas.abrirBaseDeDatos();

        //Llamado de objetos del layout
        btnFecha = (Button) findViewById(R.id.btnFecha);
        tvFechaSeleccionada = (TextView) findViewById(R.id.tvFechaSeleccionada);
        tvPrueba = (TextView) findViewById(R.id.tvPrueba);
        tvKilosTotales = (TextView) findViewById(R.id.tvKilosTotales);
        tvDineroGastado = (TextView) findViewById(R.id.tvDineroGastado);

        //Obtiene valor del nit de la empresa proveniente del MainActivity
        nit = getIntent().getExtras().getString("Nit_Empresa");

        //Obtiene día , mes , año
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        año = c.get(Calendar.YEAR);

        //Listener del boton de Ingresar fecha
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnlineNet()) {
                    showDialog(111);
                } else {
                    //Se muestra un alert dialog que indica que no hay conexión a internet
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ReportesActivity.this);
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
        });



    }


    //Menú(opción de regreso)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    //Crea Diálogo para el Date Picker para seleccionar fecha
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 111) {
            return new DatePickerDialog(this, dateLPickerListener, año, mes, dia);
        }
        return null;
    }

    //Listener de la fecha seleccionada
    private DatePickerDialog.OnDateSetListener dateLPickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int y, int m, int d) {
            fecha1 = String.format("%02d/%02d/%04d", y, m + 1, d);
            //Da formato mes - día - año
            DateFormat dateFormatFH = new SimpleDateFormat("MM-dd-yyyy");
            Date dateFH = new Date(fecha1);
            fecha = dateFormatFH.format(dateFH);
            mostrarFecha();
        }
    };

    void mostrarFecha() {
        //Asigna a un text view el valor de la fecha seleccionada por el usuario
        tvFechaSeleccionada.setText(fecha + " (Mes-Día-Año)");
        tvPrueba.setText("");
        //Hace llamado al método
        makeServiceCallReportes(url);
        //Limpia el listview de facturas
        if (list.size() > 0) {
            list.clear();
        }
        kilos = 0.0;
        valorpago = 0.0;
        //Ejecuta la tarea asíncrona
        new GetReportes().execute();

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


    //Obtiene el registro de facturas almacenadas en la nube, mediante el método makeServiceCallReportes el cuál hace un POST con nit y fecha. Retorna todos los datos almacenados en base a estos 2 datos.
    private class GetReportes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ReportesActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String jsonStr = makeServiceCallReportes(url);

            if (jsonStr != null) {
                try {
                    //Convierte en JsonArray los datos obtenidos
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    length = jsonArray.length();
                    //Hace un ArrayList del tamaño del JsonArray
                    List<String> listContents = new ArrayList<String>(length);

                    int j = 0;
                    //Obtiene todos los datos de factura que están almacenados en la nube.
                    for (int i = 0; i < length; i++) {
                        listContents.add(jsonArray.getString(i));
                        JSONObject c = jsonArray.getJSONObject(i);
                        j = j + 1;
                        String nombreCompraventa = c.getString("Nombre_Compraventa");
                        String nitCompraVenta = c.getString("Nit_Compraventa");
                        String ciudadCompraVenta = c.getString("Ciudad");
                        String direccionCompraventa = c.getString("Direccion_Compraventa");
                        String telefonoCompaventa = c.getString("Telefono_Compraventa");
                        String nombreUsuario = c.getString("Nombre_Usuario");
                        String apellidoUsuario = c.getString("Apellido_Usuario");
                        String kilosTotales = c.getString("Kilos_Totales");
                        String tipoCafe = c.getString("Tipo_Cafe");
                        String valorPago = c.getString("Valor_Pago");
                        String fechaVenta = c.getString("Fecha_Venta");
                        String horaVenta = c.getString("Hora_Venta");
                        String tipoMovimiento = c.getString("Tipo_Movimiento");
                        String nombresCliente = c.getString("Nombre_Cliente");
                        String cedulaCliente = c.getString("Cedula_Cliente");
                        String telefonoCliente = c.getString("Telefono_Cliente");
                        HashMap<String, String> objetos = new HashMap<>();
                        objetos.put("Numero_Factura", "Factura " + j);
                        objetos.put("Nombre_Compraventa", nombreCompraventa);
                        objetos.put("Nit_Compraventa", nitCompraVenta);
                        objetos.put("Direccion_Compraventa", direccionCompraventa);
                        objetos.put("Telefono_Compraventa", telefonoCompaventa);
                        objetos.put("Ciudad", ciudadCompraVenta);
                        objetos.put("Nombre_Usuario", nombreUsuario);
                        objetos.put("Apellido_Usuario", apellidoUsuario);
                        objetos.put("Tipo_Cafe", tipoCafe);
                        objetos.put("Kilos_Totales", kilosTotales);
                        objetos.put("Valor_Pago", valorPago);
                        objetos.put("Fecha_Venta", fechaVenta);
                        objetos.put("Hora_Venta", horaVenta);
                        objetos.put("Tipo_Movimiento", tipoMovimiento);
                        objetos.put("Nombre_Cliente", nombresCliente);
                        objetos.put("Cedula_Cliente", cedulaCliente);
                        objetos.put("Telefono_Cliente", telefonoCliente);
                        kilos = kilos + Double.parseDouble(kilosTotales.replace(",", ""));
                        valorpago = valorpago + Double.parseDouble(valorPago.replace(",", ""));
                        list.add(objetos);
                    }
                    DecimalFormat formateador = new DecimalFormat("###,###.##");
                    strFormatvalorPagoSeco = formateador.format(valorpago);


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
                                "Error al cargar datos",
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
            //Cierra el progress dialog
            pDialog.dismiss();
            //Si el tamaño del JsonArray es 0 quiere decir que no hay registro de facturas con esa fecha y nit
            if (length == 0) {
                tvPrueba.setText("No hay registro de facturas en esta fecha");
            }
            //Se crea un adaptador para asignar todos los datos obtenidos al listview
            adapter = new SimpleAdapter(
                    getApplicationContext(), list,
                    R.layout.list_view_facturas, new String[]{"Numero_Factura", "Nombre_Compraventa", "Nit_Compraventa", "Ciudad", "Nombre_Usuario", "Apellido_Usuario", "Tipo_Cafe", "Kilos_Totales", "Valor_Pago", "Fecha_Venta", "Hora_Venta", "Nombre_Cliente", "Cedula_Cliente", "Telefono_Cliente"}, new int[]{R.id.numerofactura, R.id.nombrecompraventa, R.id.nitcompraventa, R.id.ciudad, R.id.nombreusuario, R.id.apellidousuario, R.id.tipodecafe, R.id.kilostotales, R.id.valorpago, R.id.fecha, R.id.hora, R.id.nombrescliente, R.id.cedulacliente, R.id.telefonocliente
            });
            ListView listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(adapter);
            tvKilosTotales.setText("Kilos Totales Comprados: " + kilos);
            tvDineroGastado.setText("Dinero total gastado: $" + strFormatvalorPagoSeco);

        }

    }

    //Método que hace petición POST pasando dos parámetros, fecha y nit .
    public String makeServiceCallReportes(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("Fecha_Venta", fecha)
                    .appendQueryParameter("Nit_Compraventa", nit);
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
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    //Convierte un inputStream en un String
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
