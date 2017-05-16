package com.example.user.comprarcafe.Activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.ClientesController;
import com.example.user.comprarcafe.Controllers.FacturasController;
import com.example.user.comprarcafe.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FacturaActivity extends AppCompatActivity implements OnClickListener {

    //Variables
    public long idUsuario, idVenta, idEmpresa, idCliente;
    public String nombresUsuario, apellidosUsuario, tipo, kilosTotalesSeco, valorPagoSeco, nombreEmpresa, nombresCliente, cedulaCliente, telefonoCliente, direccionCliente, fecha, hora, fechahora, direccionEmpresa, telefonoEmpresa, kilosTotalesVerde, valorPagoVerde, kilosTotalesPasilla, valorPagoPasilla, strValorPagoSecoIva, strValorPagoVerdeIva, strValorPagoPasillaIva, kilosTotales, valorPago, nitEmpresa, VoC, fecha1, ciudadEmpresa, departamentoEmpresa;
    private TextView tvVoC, factura, facturaCompraventa, facturaVendedor, facturaTipoCafe, facturaKilosTotales, facturaValorTotal, facturaNombreCliente, facturaCedulaCliente, facturaTelefonoCliente, facturaDireccionCliente, facturaCiudad, facturaDepartamento;
    private Button btnFacturaImprimir;
    private double valorPagoSecoIva, valorPagoVerdeIva, valorPagoPasillaIva, doubleValorPagoSeco, doubleValorPagoVerde, doubleValorPagoPasilla;


    // Permisos para Api 23
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Instancia del controlador de clientes
    ClientesController db_clientes;

    //Instancia del controlador de facturas
    FacturasController db_facturas;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        //Coloca título al toolbar y habilita el retorno a la actividad anterior
        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolbarFactura);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("DATOS DE LA FACTURA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Si debe pedir permisos, para el caso de API 23 en adelante
        if (shouldAskPermissions()) {
            askPermissions();
        }

        //Conexión al controlador de clientes
        db_clientes = new ClientesController(this);
        db_clientes.abrirBaseDeDatos();
        //Conexión al controlador de Facturas
        db_facturas = new FacturasController(this);
        db_facturas.abrirBaseDeDatos();


        //Llamado a objetos del layout
        facturaNombreCliente = (TextView) findViewById(R.id.tvNombreCliente);
        facturaCedulaCliente = (TextView) findViewById(R.id.tvCedulaCliente);
        facturaTelefonoCliente = (TextView) findViewById(R.id.tvTelefonoCliente);
        facturaCompraventa = (TextView) findViewById(R.id.facturaCompraventa);
        facturaVendedor = (TextView) findViewById(R.id.facturaVendedor);
        facturaTipoCafe = (TextView) findViewById(R.id.facturaTipoCafe);
        facturaKilosTotales = (TextView) findViewById(R.id.facturaKilosTotales);
        facturaValorTotal = (TextView) findViewById(R.id.facturaValorTotal);
        facturaCiudad = (TextView) findViewById(R.id.facturaCiudad);
        facturaDepartamento = (TextView) findViewById(R.id.facturaDepartamento);
        btnFacturaImprimir = (Button) findViewById(R.id.btnFacturaImprimir);

        //Listener del botón de imprimir factura
        btnFacturaImprimir.setOnClickListener(this);

        //Obtiene los datos que provienen de alguno de los fragmentos
        idUsuario = getIntent().getExtras().getLong("idUsuario");
        idVenta = getIntent().getExtras().getLong("idVenta");
        nombresUsuario = getIntent().getExtras().getString("nombresUsuario");
        apellidosUsuario = getIntent().getExtras().getString("apellidosUsuario");
        tipo = getIntent().getExtras().getString("tipo");
        idEmpresa = getIntent().getExtras().getLong("idEmpresa");
        nombreEmpresa = getIntent().getExtras().getString("nombreEmpresa");
        direccionEmpresa = getIntent().getExtras().getString("direccionEmpresa");
        telefonoEmpresa = getIntent().getExtras().getString("telefonoEmpresa");
        nitEmpresa = getIntent().getExtras().getString("nitEmpresa");
        ciudadEmpresa = getIntent().getExtras().getString("ciudadEmpresa");
        departamentoEmpresa = getIntent().getExtras().getString("departamentoEmpresa");
        nombresCliente = getIntent().getExtras().getString("nombresCliente");
        cedulaCliente = getIntent().getExtras().getString("cedulaCliente");
        telefonoCliente = getIntent().getExtras().getString("telefonoCliente");


        //Obtención del dato de la fecha y hora que proviene del proceso de compra
        fecha1 = getIntent().getExtras().getString("fecha");
        DateFormat dateFormatFH = new SimpleDateFormat("MM-dd-yyyy");
        Date dateFH = new Date(fecha1);
        fecha = dateFormatFH.format(dateFH);
        hora = getIntent().getExtras().getString("hora");
        fechahora = getIntent().getExtras().getString("fechahora");

        //Datos de café Seco
        kilosTotalesSeco = getIntent().getExtras().getString("kilosTotalesSeco");
        valorPagoSeco = getIntent().getExtras().getString("valorPagoSeco");
        doubleValorPagoSeco = getIntent().getExtras().getDouble("doubleValorPagoSeco");
        //Datos de café Verde
        kilosTotalesVerde = getIntent().getExtras().getString("kilosTotalesVerde");
        valorPagoVerde = getIntent().getExtras().getString("valorPagoVerde");
        doubleValorPagoVerde = getIntent().getExtras().getDouble("doubleValorPagoVerde");
        //Datos de café Pasilla
        kilosTotalesPasilla = getIntent().getExtras().getString("kilosTotalesPasilla");
        valorPagoPasilla = getIntent().getExtras().getString("valorPagoPasilla");
        doubleValorPagoPasilla = getIntent().getExtras().getDouble("doubleValorPagoPasilla");

        //Asigna el texto de los datos a cada textView
        facturaNombreCliente.setText(nombresCliente);
        facturaCedulaCliente.setText(cedulaCliente);
        facturaTelefonoCliente.setText(telefonoCliente);
        facturaCompraventa.setText(nombreEmpresa);
        facturaDepartamento.setText(departamentoEmpresa);
        facturaCiudad.setText(ciudadEmpresa);
        facturaVendedor.setText(nombresUsuario + " " + apellidosUsuario);
        facturaTipoCafe.setText(tipo);

        Toast.makeText(this, "Datos de la Factura", Toast.LENGTH_SHORT).show();

        switch (tipo) {
            case "café seco":
                facturaKilosTotales.setText(kilosTotalesSeco);
                facturaValorTotal.setText(valorPagoSeco);
                valorPagoSecoIva = doubleValorPagoSeco + doubleValorPagoSeco * 0.19;
                DecimalFormat formateadorSeco = new DecimalFormat("###,###.##");
                strValorPagoSecoIva = formateadorSeco.format(valorPagoSecoIva);
                break;
            case "café verde":
                facturaKilosTotales.setText(kilosTotalesVerde);
                facturaValorTotal.setText(valorPagoVerde);
                valorPagoVerdeIva = doubleValorPagoVerde + doubleValorPagoVerde * 0.19;
                DecimalFormat formateadorVerde = new DecimalFormat("###,###.##");
                strValorPagoVerdeIva = formateadorVerde.format(valorPagoVerdeIva);
                break;
            case "café pasilla":
                facturaKilosTotales.setText(kilosTotalesPasilla);
                facturaValorTotal.setText(valorPagoPasilla);
                valorPagoPasillaIva = doubleValorPagoPasilla + doubleValorPagoPasilla * 0.19;
                DecimalFormat formateadorPasilla = new DecimalFormat("###,###.##");
                strValorPagoPasillaIva = formateadorPasilla.format(valorPagoPasillaIva);
        }
        kilosTotales = facturaKilosTotales.getText().toString();
        valorPago = facturaValorTotal.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Para el botón de retorno a la atividad anterior
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (isOnlineNet()) {
            String strFacturaKilosTotales = facturaKilosTotales.getText().toString();
            String strFacturaValorTotal = facturaValorTotal.getText().toString();


            idCliente = db_clientes.idClienteByNombre(cedulaCliente);

            //Inserta datos en la tabla de facturas
            db_facturas.insertDataFacturas(idVenta, idCliente, null, nombreEmpresa, nombresUsuario, apellidosUsuario, tipo, strFacturaKilosTotales, strFacturaValorTotal, fecha, nitEmpresa, nombresCliente, cedulaCliente, telefonoCliente, hora, departamentoEmpresa, ciudadEmpresa); //VOC NULL
            //Ejecuta la tarea asíncrona
            new CargarDatosHistoria().execute("http://iot.bitnamiapp.com:3000/factura");
            //Revisa si el almacenamiento externo tiene espacio
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Toast.makeText(this, "Activar almacenamiento externo", Toast.LENGTH_SHORT).show();
            }

            //Crea un directorio para el pdf
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "FACTURAS");
            if (!pdfDir.exists()) {
                pdfDir.mkdir();
            }


            File pdfFile = new File(pdfDir, "Factura.pdf");

            try {
                //Crea un documento
                Document document = new Document();
                //Obtiene la instancia del PDF
                PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
                //abre el documento
                document.open();

                addMetaData(document);
                addTitlePage(document);
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Abre el pdf y lo guarda en el directorio especificado
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File(pdfDir, "Factura.pdf"));
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

            //Despliega las formas de almacenamiento(Drive,correo,..)
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, "");
            email.putExtra(Intent.EXTRA_SUBJECT, "Factura");
            email.putExtra(Intent.EXTRA_TEXT, "");
            Uri uriEmail = Uri.fromFile(new File(pdfDir, "Factura.pdf"));
            email.putExtra(Intent.EXTRA_STREAM, uriEmail);
            email.setType("application/pdf");
            email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(email);

            finish();
        } else {
            //Se muestra un alert dialog que indica que no hay acceso a internet
            final AlertDialog.Builder builder = new AlertDialog.Builder(FacturaActivity.this);
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

    //Hace un POST pasando todos los datos obtenidos para la factura
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
                    .appendQueryParameter("Nombre_Compraventa", nombreEmpresa)
                    .appendQueryParameter("Nit_Compraventa", nitEmpresa)
                    .appendQueryParameter("Direccion_Compraventa", direccionEmpresa)
                    .appendQueryParameter("Telefono_Compraventa", telefonoEmpresa)
                    .appendQueryParameter("Nombre_Usuario", nombresUsuario)
                    .appendQueryParameter("Apellido_Usuario", apellidosUsuario)
                    .appendQueryParameter("Tipo_Cafe", tipo)
                    .appendQueryParameter("Kilos_Totales", kilosTotales)
                    .appendQueryParameter("Valor_Pago", valorPago)
                    .appendQueryParameter("Fecha_Venta", fecha)
                    .appendQueryParameter("Hora_Venta", hora)
                    .appendQueryParameter("Fecha_Hora_Venta", fechahora)
                    .appendQueryParameter("Tipo_Movimiento", VoC)
                    .appendQueryParameter("Nombre_Cliente", nombresCliente)
                    .appendQueryParameter("Cedula_Cliente", cedulaCliente)
                    .appendQueryParameter("Telefono_Cliente", telefonoCliente)
                    .appendQueryParameter("Ciudad", ciudadEmpresa)
                    .appendQueryParameter("Departamento", departamentoEmpresa);

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

    // Lee un inputStream y lo convierte en String
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    //La tarea asíncrona ejecuta la subida de todos los datos a la nube.
    private class CargarDatosHistoria extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(FacturaActivity.this,
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

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
        }
    }


    //CREACIÓN DEL PDF
    // Asigna propiedades del PDF
    public void addMetaData(Document document) {
        document.addTitle("FACTURA");
        document.addSubject("Info factura");
        document.addKeywords("factura,compras");
        document.addAuthor("interedes");
        document.addCreator("interedes");
    }

    public void addTitlePage(Document document) throws DocumentException {
        // Estilo de la fuente para el documento
        Font catFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font normal1 = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font MediumBold = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normal2 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        // Inicia un nuevo párrafo
        Paragraph prHead = new Paragraph();
        // Asigna la fuente para este párrafo
        prHead.setFont(titleFont);
        // Añade un item en el párrafo
        prHead.add("FACTURA\n");
        float[] columnWidths = new float[]{200f, 200f};
        // Crea una nueva celda dentro de la tabla
        prHead.setFont(normal1);
        prHead.add("\nFecha: " + fecha + " " + hora + "\n");
        //prHead.add("\nProceso: " + VoC + "\n");
        prHead.add("" + nombreEmpresa + "\n");
        prHead.add("" + direccionEmpresa + "\n");
        prHead.add("" + telefonoEmpresa + "\n");
        prHead.add("" + departamentoEmpresa + "\n");
        prHead.add("" + ciudadEmpresa + "\n\n");

        //Datos Cliente
        prHead.setFont(MediumBold);
        prHead.add("Datos Cliente \n");
        prHead.setFont(normal1);
        prHead.add("Nombre del cliente: " + nombresCliente + "\n");
        prHead.add("Cédula del cliente: " + cedulaCliente + "\n");
        prHead.add("Teléfono del cliente: " + telefonoCliente + "\n\n");
        //prHead.add("Dirección del cliente: "+direccionCliente+"\n\n");
        prHead.setAlignment(Element.ALIGN_LEFT);
        // Add all above details into Document
        document.add(prHead);
        /////TABLA/////
        // Crea tabla en el documento con 2 filas
        PdfPTable table = new PdfPTable(2);
        table.setWidths(columnWidths);
        // 100.0f significa que el ancho de la tabla es igual al del documento
        table.setWidthPercentage(100.0f);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        Paragraph prDatos = new Paragraph("Datos");
        prDatos.setFont(smallBold);
        PdfPCell cell = new PdfPCell(prDatos);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        Paragraph prValores = new Paragraph("Valores");
        prValores.setFont(smallBold);
        cell = new PdfPCell(prValores);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Tipo de café : "));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("" + tipo));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        if (tipo.equals("café seco")) {
            cell = new PdfPCell(new Paragraph("Kilos Totales de la Carga: "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + kilosTotalesSeco));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Subtotal: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + valorPagoSeco));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("IVA: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("19%"));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Valor Total: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + strValorPagoSecoIva));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        }
        if (tipo.equals("café verde")) {
            cell = new PdfPCell(new Paragraph("Kilos Totales de la Carga: "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + kilosTotalesVerde));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Subtotal: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + valorPagoVerde));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("IVA: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("19%"));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Valor Total: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + strValorPagoVerdeIva));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        }
        if (tipo.equals("café pasilla")) {
            cell = new PdfPCell(new Paragraph("Kilos Totales de la Carga: "));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + kilosTotalesPasilla));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Subtotal: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + valorPagoPasilla));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("IVA: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("19%"));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Valor Total: "));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + strValorPagoPasillaIva));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
        }
        document.add(table);
        document.newPage();
    }

    //Métodos para solicitar permisos para Api 23 en adelante
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


    //Restringe el retorno a la actividad anterior mediante el botón de retorno del móvil
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}


