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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.FacturasController;
import com.example.user.comprarcafe.Handler.HttpHandler;
import com.example.user.comprarcafe.Models.Factura;
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
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
import java.util.TimeZone;

public class ReportesActivity extends AppCompatActivity {

    public long idEmpresa;
    public String nombreEmpresa, direccionEmpresa, telefonoEmpresa, nitEmpresa, nombresUsuario, apellidosUsuario, tipo, kilosTotales, valorPago, hora;
    TextView tvPrueba, tvKilosTotales, tvDineroGastado;
    String fecha, nit, fecha1, a, strFormatvalorPagoSeco;


    Button btnFecha;
    TextView tvFechaSeleccionada;
    int dia, mes, año, length;

    public ArrayList<Factura> listFactura;

    FacturasController db_facturas;

    private static final String TAG = HttpHandler.class.getSimpleName();
    private ProgressDialog pDialog;
    private static String url = "http://iot.bitnamiapp.com:3000/factura_nit_fechaventa";
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    Double kilos = 0.0;
    Double valorpago = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarReportes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("REPORTE DE FACTURAS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Conexión al controlador de Facturas
        db_facturas = new FacturasController(this);
        db_facturas.abrirBaseDeDatos();

        //new CargarDatosHistoria().execute("http://iot.bitnamiapp.com:3000/factura");
        btnFecha = (Button) findViewById(R.id.btnFecha);
        tvFechaSeleccionada = (TextView) findViewById(R.id.tvFechaSeleccionada);
        tvPrueba = (TextView) findViewById(R.id.tvPrueba);
        tvKilosTotales = (TextView) findViewById(R.id.tvKilosTotales);
        tvDineroGastado = (TextView) findViewById(R.id.tvDineroGastado);

        nit = getIntent().getExtras().getString("Nit_Empresa");
        //fecha = getIntent().getExtras().getString("fecha");
        //get current date
        Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        año = c.get(Calendar.YEAR);
        //fecha = String.format("%02d/%02d/%04d",dia,mes+1,año);
        //mostrarFecha();

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnlineNet()) {
                    showDialog(111);
                } else {
                    //Se muestra un alert dialog que indica que los datos son erróneos
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


        //tvPrueba = (TextView)findViewById(R.id.tvPrueba);
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        idEmpresa = prefs.getLong("idEmpresa",0);
        nombreEmpresa = prefs.getString("nombreEmpresa","No Nombre");
        direccionEmpresa = prefs.getString("direccionEmpresa","No dirección");
        telefonoEmpresa = prefs.getString("telefonoEmpresa","No teléfono");
        nitEmpresa = prefs.getString("nitEmpresa","No NIT");
        nombresUsuario = prefs.getString("nombres","No nombre Usuario");
        apellidosUsuario = prefs.getString("apellidos","No apellidos Usuario");
        tipo = prefs.getString("tipo","No tipo");
        if(tipo.equals("café seco")){
            kilosTotales = prefs.getString("kilosTotales","No kilos Totales");
            valorPago = prefs.getString("valorPago","No Valor Pago");
        }
        if(tipo.equals("café verde")){
            kilosTotales = prefs.getString("kilosTotales","No kilos Totales");
            valorPago = prefs.getString("valorPago","No Valor Pago");
        }
        if(tipo.equals("café pasilla")){
            kilosTotales = prefs.getString("kilosTotales","No kilos Totales");
            valorPago = prefs.getString("valorPago","No Valor Pago");
        }
        fecha = prefs.getString("fecha","No fecha");
        hora = prefs.getString("hora","No hora");*/

        //Toast.makeText(this,"Id Empresa:"+idEmpresa+"Nombre Empresa:"+nombreEmpresa+"dirección Empresa:"+direccionEmpresa+"telefono Empresa:"+telefonoEmpresa+"Nit Empresa:"+nitEmpresa+"Tipo : "+tipo+"Kilos Totales:"+kilosTotales+"Valor Pago : "+valorPago+"Fecha:"+fecha+"Hora:"+hora,Toast.LENGTH_LONG).show();
    }

    /*void displayDate(int d, int m, int y){
        tvFechaSeleccionada.setText("Date: " + d +"/" + m +"/" + y);
    }*/


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

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 111) {
            return new DatePickerDialog(this, dateLPickerListener, año, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateLPickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int y, int m, int d) {
            fecha1 = String.format("%02d/%02d/%04d", y, m + 1, d);
            DateFormat dateFormatFH = new SimpleDateFormat("MM-dd-yyyy");
            Date dateFH = new Date(fecha1);
            fecha = dateFormatFH.format(dateFH);
            mostrarFecha();
        }
    };

    void mostrarFecha() {
        tvFechaSeleccionada.setText(fecha + " (Mes-Día-Año)");
        tvPrueba.setText("");
        //imprimirDatos();
        makeServiceCallReportes(url);
        if (list.size() > 0) {
            list.clear();
        }
        kilos = 0.0;
        valorpago = 0.0;
        new GetReportes().execute();
        //new CargarDatosHistoria().execute("http://iot.bitnamiapp.com:3000/factura_nit_fechaventa");
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

    //SQLITE
    /*public void imprimirDatos(){
        Double kilos = 0.0;
        Double valorpago = 0.0;
        listFactura = new ArrayList<Factura>();
        ArrayList<Factura> listadoFacturas = db_facturas.findFacturasByFechaAndNit(nit,fecha);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        if(listadoFacturas.size() == 0){
            tvPrueba.setText("No hay registro de facturas en esta fecha");
        }
        int i = 0;
        for(Factura factura:listadoFacturas){
            Factura factura1 = new Factura();
            factura1.setNombreEmpresa(factura.getNombreEmpresa());
            HashMap<String,String> objetos = new HashMap<>();
            i = i +1;
            objetos.put("Numero_Factura","Factura "+i);
            objetos.put("Nombre_Compraventa",factura.getNombreEmpresa());
            objetos.put("Nit_Compraventa",factura.getNitEmpresa());
            objetos.put("Nombre_Usuario",factura.getNombresUsuario());
            objetos.put("Apellido_Usuario",factura.getApellidosUsuario());
            objetos.put("Tipo_Cafe",factura.getTipoCafe());
            objetos.put("Kilos_Totales",factura.getKilosTotales());
            objetos.put("Valor_Pago",factura.getValorPago());
            objetos.put("Fecha_Venta",factura.getFecha());
            objetos.put("Nombres_Cliente",factura.getNombresCliente());
            objetos.put("Cedula_Cliente",factura.getCedulaCliente());
            objetos.put("Telefono_Cliente",factura.getTelefonoCliente());
            objetos.put("Hora",factura.getHora());
            objetos.put("Ciudad",factura.getCiudad());
            kilos = kilos + Double.parseDouble(factura.getKilosTotales().replace(",",""));
            valorpago = valorpago + Double.parseDouble(factura.getValorPago().replace(",",""));
            //listFactura.add(factura1);
            list.add(objetos);
        }

        DecimalFormat formateador = new DecimalFormat("###,###.##");
        String strFormatvalorPagoSeco = formateador.format(valorpago);
        tvKilosTotales.setText("Kilos Totales Comprados: " +kilos);
        tvDineroGastado.setText("Dinero total gastado: $" +strFormatvalorPagoSeco);

        ListAdapter adapter = new SimpleAdapter(
                ReportesActivity.this, list,
                R.layout.list_view_facturas, new String[]{"Numero_Factura","Nombre_Compraventa","Nit_Compraventa","Nombre_Usuario","Apellido_Usuario","Tipo_Cafe","Kilos_Totales","Valor_Pago","Fecha_Venta","Nombres_Cliente","Cedula_Cliente","Telefono_Cliente","Hora","Ciudad"}, new int[]{R.id.numerofactura,R.id.nombrecompraventa,R.id.nitcompraventa,R.id.nombreusuario,R.id.apellidousuario,R.id.tipodecafe,R.id.kilostotales,R.id.valorpago,R.id.fecha,R.id.nombrescliente,R.id.cedulacliente,R.id.telefonocliente,R.id.hora,R.id.ciudad
        });
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);


    } */

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
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    length = jsonArray.length();
                    List<String> listContents = new ArrayList<String>(length);

                    int j = 0;
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
            pDialog.dismiss();
            if (length == 0) {
                tvPrueba.setText("No hay registro de facturas en esta fecha");
            }
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
