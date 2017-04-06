package com.example.user.comprarcafe.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.comprarcafe.Controllers.CafeSecoController;
import com.example.user.comprarcafe.Controllers.ClientesController;
import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Controllers.UsuariosController;
import com.example.user.comprarcafe.Controllers.VentasController;
import com.example.user.comprarcafe.Activities.FacturaActivity;
import com.example.user.comprarcafe.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Fragment_cafe_seco extends Fragment {

    private EditText edtKilosCargaSeco,edtPrecioCargaDiaSeco,edtGramosCafeBuenoSeco,edtTara;
    private double kilosCargaSeco, gramosCafeBuenoSeco, merma,factor,dif_factor,precioCargaDiaSeco,valorCargaSeco,tara,kilosFinalesSeco,valorKiloSeco,valorPagoSeco;
    private int muestreo = 250, constante = 17500 , factor_estandar = 88;
    private Button btnCostoCargaSeco,btnImprimirFacturaSeco;
    private TextView fechaSeco,tvValorAPagar;
    private TextClock textClockSeco;
    public String strTipo = "café seco",strFecha,strHora, strFechaHora,nombresUsuario,apellidosUsuario,nombreEmpresa,strFormatvalorPagoSeco,direccionEmpresa,telefonoEmpresa,nitEmpresa,ciudadEmpresa,departamentoEmpresa;
    public long idUsuario,idEmpresa,id_usuario_logued;
    public long idVenta;
    public String VoC;

    /*public ArrayAdapter<String> adaptersC;
    public ArrayList<String> spinnerCV;*/
    //public String[] arraySpinner = new String[]{"VENDE","COMPRA"};
    //public List<String> arraySpinner = new ArrayList<String>();


    //Instancia del controlador de ventas
    VentasController db_ventas;

    //Instancia del controlador de usuarios
    UsuariosController db_usuarios;

    //Instancia del controlador de café seco
    CafeSecoController db_cafe_seco;

    //Instancia del controlador de Empresas
    EmpresasController db_empresas;

    //Instancia del controlador de clientes
    ClientesController db_clientes;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_cafe_seco,null);

        idUsuario = getActivity().getIntent().getExtras().getLong("id");
        nombresUsuario = getActivity().getIntent().getExtras().getString("nombres");
        apellidosUsuario = getActivity().getIntent().getExtras().getString("apellidos");
        idEmpresa = getActivity().getIntent().getExtras().getLong("idEmpresa");
        id_usuario_logued = getActivity().getIntent().getExtras().getLong("id_usuario_logued");

        //Conexión al controlador de Ventas
        db_ventas = new VentasController(v.getContext());
        db_ventas.abrirBaseDeDatos();

        //Conexión al controlador de Usuarios
        db_usuarios = new UsuariosController(v.getContext());
        db_usuarios.abrirBaseDeDatos();

        //Conexión al controlador de Café Seco
        db_cafe_seco = new CafeSecoController(v.getContext());
        db_cafe_seco.abrirBaseDeDatos();

        //Conexión al controlador de Empresas
        db_empresas = new EmpresasController(v.getContext());
        db_empresas.abrirBaseDeDatos();

        //Conexión al controlador de clientes
        db_clientes = new ClientesController(v.getContext());
        db_clientes.abrirBaseDeDatos();

        edtKilosCargaSeco = (EditText)v.findViewById(R.id.edtKilosCargaSeco);
        edtPrecioCargaDiaSeco = (EditText)v.findViewById(R.id.edtPrecioCargaDiaSeco);
        edtGramosCafeBuenoSeco = (EditText)v.findViewById(R.id.edtGramosCafeBuenoSeco);
        edtTara = (EditText)v.findViewById(R.id.edtTara);
        fechaSeco = (TextView)v.findViewById(R.id.fechaSeco);
        textClockSeco = (TextClock) v.findViewById(R.id.textClockSeco);

        btnCostoCargaSeco = (Button)v.findViewById(R.id.btnCostoCargaSeco);

        //Obtener Fecha
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        strFecha = dateFormat.format(date);
        fechaSeco.setText(strFecha);

        //Obtener Hora
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        strHora = timeFormat.format(cal.getTime());
        textClockSeco.setText(strHora);

        //Obtener FechaHora
        DateFormat dateFormatFH = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        dateFormatFH.setTimeZone(TimeZone.getTimeZone("Africa/Abidjan"));
        Date dateFH = new Date();
        strFechaHora = dateFormatFH.format(dateFH);

        btnCostoCargaSeco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strKilosCargaSeco = edtKilosCargaSeco.getText().toString();
                if(TextUtils.isEmpty(strKilosCargaSeco)){
                    edtKilosCargaSeco.setError("Llena este campo");
                    return;
                }
                final String strPrecioCargaDiaSeco = edtPrecioCargaDiaSeco.getText().toString();
                if(TextUtils.isEmpty(strPrecioCargaDiaSeco)){
                    edtPrecioCargaDiaSeco.setError("Llena este campo");
                    return;
                }
                final String strGramosCafeBuenoSeco = edtGramosCafeBuenoSeco.getText().toString();
                if(TextUtils.isEmpty(strGramosCafeBuenoSeco)){
                    edtGramosCafeBuenoSeco.setError("Llena este campo");
                    return;
                }
                final String strTara = edtTara.getText().toString();
                if(TextUtils.isEmpty(strTara)){
                    edtTara.setError("Llena este campo");
                    return;
                }
                //valor de los kilos de carga total
                kilosCargaSeco = Double.parseDouble(strKilosCargaSeco);
                if(kilosCargaSeco <= 0){
                    edtKilosCargaSeco.setError("Ingrese una cantidad de kilos aceptable");
                    return;
                }
                //valor de los gramos de café bueno que se obtiene
                gramosCafeBuenoSeco = Double.parseDouble(strGramosCafeBuenoSeco);
                if(gramosCafeBuenoSeco >= 250  || gramosCafeBuenoSeco <170){
                    edtGramosCafeBuenoSeco.setError("Ingrese una cantidad de café bueno aceptable");
                    return;
                }
                //Cálculo de la merma
                merma = (muestreo- gramosCafeBuenoSeco) /2.5;
                final String strMerma = Double.toString(merma);
                //Cálculo del factor de calidad
                factor = constante/gramosCafeBuenoSeco;
                final String strFactor = String.format("%.1f", factor);
                factor = Double.parseDouble(strFactor);
                //Cálculo de la diferencia entre factores
                dif_factor = factor - factor_estandar;
                //valor de la carga de café del día
                precioCargaDiaSeco = Double.parseDouble(strPrecioCargaDiaSeco);
                if(precioCargaDiaSeco < 100000){
                    edtPrecioCargaDiaSeco.setError("Ingrese un real precio de la carga del día");
                    return;
                }
                //Condicionales del factor de calidad para hacer descuento o no
                if(factor >88 && factor<=92.8 ){
                    valorCargaSeco = precioCargaDiaSeco+(precioCargaDiaSeco*dif_factor/100);
                }else if(factor == 88){
                    valorCargaSeco = precioCargaDiaSeco;
                }else if(factor>92.8){
                    valorCargaSeco = precioCargaDiaSeco-(precioCargaDiaSeco*dif_factor/100);
                }
                tara = Double.parseDouble(strTara);
                if(tara > 70 || tara < 0){
                    edtTara.setError("Ingrese una tara aceptable");
                    return;
                }
                if(kilosCargaSeco < 80){
                    tara = 0;
                }
                kilosFinalesSeco = kilosCargaSeco - tara;
                final String strKilosFinalesSeco = Double.toString(kilosFinalesSeco);
                final String strValorCargaSeco = Double.toString(valorCargaSeco);
                //Valor de un kilo
                valorKiloSeco = valorCargaSeco/125;
                final String strvalorKiloSeco = Double.toString(valorKiloSeco);
                //Valor a pagar
                valorPagoSeco = kilosFinalesSeco * valorKiloSeco;
                final String strvalorPagoSeco = Double.toString(valorPagoSeco);
                final String strMuestra = Integer.toString(muestreo);
                final String strConstante = Double.toString(constante);
                final String strDifFactor = Double.toString(dif_factor);
                DecimalFormat formateador = new DecimalFormat("###,###.##");
                strFormatvalorPagoSeco = formateador.format(valorPagoSeco);
                //Toast.makeText(getActivity(),"Valor pago"+valorPagoSeco,Toast.LENGTH_LONG).show();


                //ALERT DIALOG
                View dialogo = inflater.inflate(R.layout.dialogo_personalizado,null);
                //TODO QUITAR VOC, MEJOR INTERFAZ ACTIVIDAD FACTURA, MEJORAR ACTIVIDAD REPORTES
                //COMENTAR VOC
                /*final List<String> arraySpinner = new ArrayList<String>();
                arraySpinner.add("COMPRAR");
                //arraySpinner.add("VENDER");
                Spinner spinnerC = (Spinner)dialogo.findViewById(R.id.spinnerC);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraySpinner);
                spinnerC.setAdapter(adapter);
                spinnerC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        VoC = arraySpinner.get(position);
                        Toast.makeText(getActivity(),"Voc:"+VoC,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }); */

                final EditText edtNombresCliente = (EditText)dialogo.findViewById(R.id.edtNombreCliente);
                final EditText edtCedulaCliente = (EditText)dialogo.findViewById(R.id.edtCedulaCliente);
                final EditText edtTelefonoCliente = (EditText)dialogo.findViewById(R.id.edtTelefonoCliente);

                //TODO MÁS VALIDACIONES DE LOS DATOS QUE SE INGRESAN PORQUE POR EJEMPLO PUEDEN SUPERAR LA CAPACIDAD DE ALMACENAMIENTO
                //final EditText edtDireccionCliente = (EditText)dialogo.findViewById(R.id.edtDireccionCliente);
                tvValorAPagar = (TextView)dialogo.findViewById(R.id.tvValorAPagar);
                tvValorAPagar.setText(strFormatvalorPagoSeco);

                Button btnGenerarFactura = (Button)dialogo.findViewById(R.id.btnGenerarFactura);
                btnGenerarFactura.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nombresCliente = edtNombresCliente.getText().toString();
                        final String cedulaCliente = edtCedulaCliente.getText().toString();
                        final String telefonoCliente = edtTelefonoCliente.getText().toString();
                        //final String direccionCliente = edtDireccionCliente.getText().toString();

                        if(TextUtils.isEmpty(nombresCliente)){
                            edtNombresCliente.setError("Llena este campo");
                            return;
                        }
                        if(TextUtils.isEmpty(cedulaCliente)){
                            edtCedulaCliente.setError("Llena este campo");
                            return;
                        }
                        if(TextUtils.isEmpty(telefonoCliente)){
                            edtTelefonoCliente.setError("Llena este campo");
                            return;
                        }
                        /*if(TextUtils.isEmpty(direccionCliente)){
                            edtDireccionCliente.setError("Llena este campo");
                            return;
                        }*/

                        //Inserción de datos en la tabla de ventas
                        db_ventas.insertDataVentas(idUsuario,strFecha,strHora,strPrecioCargaDiaSeco,strKilosFinalesSeco,strvalorPagoSeco,strTipo,strMuestra);
                        //Toast.makeText(getActivity(),""+idUsuario,Toast.LENGTH_SHORT).show();

                        idVenta = db_ventas.findVentasByUsuario(idUsuario);
                        //Toast.makeText(getActivity(),"idUsuario"+idUsuario+"idVenta:"+idVenta,Toast.LENGTH_SHORT).show();

                        nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);
                        direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
                        telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
                        nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);
                        ciudadEmpresa = db_empresas.findCiudadEmpresaById(idEmpresa);
                        departamentoEmpresa = db_empresas.findDepartamentoEmpresaById(idEmpresa);
                        //Toast.makeText(getActivity(),departamentoEmpresa,Toast.LENGTH_SHORT).show();
                        //Insertando datos en la tabla de clientes
                        db_clientes.insertDataClientes(nombresCliente,cedulaCliente,telefonoCliente,null);
                        //Inserción de datos en la tabla de café seco
                        //TODO agregar variedad
                        db_cafe_seco.insertDataCafeSeco(idVenta,strGramosCafeBuenoSeco,strMerma,strFactor,strConstante,strDifFactor,strTara,strKilosFinalesSeco,strvalorKiloSeco,strvalorPagoSeco,null,strMuestra,strValorCargaSeco);
                        Intent i = new Intent(getActivity(), FacturaActivity.class);
                        i.putExtra("idUsuario",idUsuario);
                        i.putExtra("idVenta",idVenta);
                        i.putExtra("idEmpresa",idEmpresa);
                        i.putExtra("nombreEmpresa",nombreEmpresa);
                        i.putExtra("direccionEmpresa",direccionEmpresa);
                        i.putExtra("telefonoEmpresa",telefonoEmpresa);
                        i.putExtra("departamentoEmpresa",departamentoEmpresa);
                        i.putExtra("ciudadEmpresa",ciudadEmpresa);
                        i.putExtra("nitEmpresa",nitEmpresa);
                        i.putExtra("nombresUsuario",nombresUsuario);
                        i.putExtra("apellidosUsuario",apellidosUsuario);
                        i.putExtra("tipo",strTipo);
                        i.putExtra("kilosTotalesSeco",strKilosFinalesSeco);
                        i.putExtra("valorPagoSeco",strFormatvalorPagoSeco);
                        i.putExtra("doubleValorPagoSeco",valorPagoSeco);
                        i.putExtra("nombresCliente",nombresCliente);
                        i.putExtra("cedulaCliente",cedulaCliente);
                        i.putExtra("telefonoCliente",telefonoCliente);
                        //i.putExtra("direccionCliente",direccionCliente);
                        i.putExtra("fecha",strFecha);
                        i.putExtra("hora",strHora);
                        i.putExtra("fechahora",strFechaHora);
                        //i.putExtra("VoC",VoC);
                        startActivity(i);
                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogo);
                builder.setTitle("Factura");
                builder.show();

            }
        });

        return v;
    }





}
