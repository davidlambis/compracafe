package com.example.user.comprarcafe.Fragments;


import android.content.Intent;
import android.os.Bundle;
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

import com.example.user.comprarcafe.Activities.FacturaActivity;
import com.example.user.comprarcafe.Controllers.CafeVerdeController;
import com.example.user.comprarcafe.Controllers.ClientesController;
import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Controllers.UsuariosController;
import com.example.user.comprarcafe.Controllers.VentasController;
import com.example.user.comprarcafe.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Fragment_cafe_verde extends Fragment{

    private EditText edtKilosCargaVerde,edtPrecioCargaDiaVerde,edtGramosCafeMaloVerde;
    private double precioCargaDiaVerde, valorCargaVerde, gramosMalosVerde, cafeDañadoVerde ,kilosCargaVerde,kilosBuenosVerde,precioCargaVerde, precioTotalCargaVerde;
    private Button btnCostoCargaVerde;
    private TextView fechaVerde,tvValorAPagar;
    private TextClock textClockVerde;
    public String VoC,strFechaVerde, strHoraVerde,strFechaHora, strTipo="café verde", strMuestraVerde = "100",nombresUsuario,apellidosUsuario,nombreEmpresa,direccionEmpresa,telefonoEmpresa,strFormatvalorPagoVerde,nitEmpresa,ciudadEmpresa,departamentoEmpresa;
    public long idUsuario,idVenta,idEmpresa,id_usuario_logued;;

    //Instancia del controlador de ventas
    VentasController db_ventas;

    //Instancia del controlador de usuarios
    UsuariosController db_usuarios;

    //Instancia del controlador de café verde
    CafeVerdeController db_cafe_verde;

    //Instancia del controlador de Empresas
    EmpresasController db_empresas;

    //Instancia del controlador de clientes
    ClientesController db_clientes;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_cafe_verde,null);

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

        //Conexión al controlador de Café Verde
        db_cafe_verde = new CafeVerdeController(v.getContext());
        db_cafe_verde.abrirBaseDeDatos();

        //Conexión al controlador de Empresas
        db_empresas = new EmpresasController(v.getContext());
        db_empresas.abrirBaseDeDatos();

        //Conexión al controlador de clientes
        db_clientes = new ClientesController(v.getContext());
        db_clientes.abrirBaseDeDatos();

        fechaVerde = (TextView)v.findViewById(R.id.fechaVerde);

        edtKilosCargaVerde = (EditText)v.findViewById(R.id.edtKilosCargaVerde);
        edtPrecioCargaDiaVerde = (EditText)v.findViewById(R.id.edtPrecioCargaDiaVerde);
        edtGramosCafeMaloVerde = (EditText)v.findViewById(R.id.edtGramosCafeMaloVerde);
        btnCostoCargaVerde = (Button)v.findViewById(R.id.btnCostoCargaVerde);
        textClockVerde = (TextClock)v.findViewById(R.id.textClockVerde);

        //Obtener Fecha
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        strFechaVerde = dateFormat.format(date);
        fechaVerde.setText(strFechaVerde);
        //Obtener Hora
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        strHoraVerde = timeFormat.format(cal.getTime());
        textClockVerde.setText(strHoraVerde);
        //Obtener FechaHora
        DateFormat dateFormatFH = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        dateFormatFH.setTimeZone(TimeZone.getTimeZone("Africa/Abidjan"));
        Date dateFH = new Date();
        strFechaHora = dateFormatFH.format(dateFH);


        btnCostoCargaVerde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strKilosCargaVerde = edtKilosCargaVerde.getText().toString();
                if(TextUtils.isEmpty(strKilosCargaVerde)){
                    edtKilosCargaVerde.setError("Llena este campo");
                    return;
                }
                final String strPrecioCargaDiaVerde = edtPrecioCargaDiaVerde.getText().toString();
                if(TextUtils.isEmpty(strPrecioCargaDiaVerde)){
                    edtPrecioCargaDiaVerde.setError("Llena este campo");
                    return;
                }
                String strGramosCafeMaloVerde = edtGramosCafeMaloVerde.getText().toString();
                if(TextUtils.isEmpty(strGramosCafeMaloVerde)){
                    edtGramosCafeMaloVerde.setError("Llena este campo");
                    return;
                }
                precioCargaDiaVerde = Double.parseDouble(strPrecioCargaDiaVerde);
                if(precioCargaDiaVerde < 100000){
                    edtPrecioCargaDiaVerde.setError("Ingrese un real precio de la carga del día");
                    return;
                }
                valorCargaVerde = precioCargaDiaVerde - (precioCargaDiaVerde*10/100);
                valorCargaVerde = valorCargaVerde/2;
                final String strValorCargaVerde = Double.toString(valorCargaVerde);
                gramosMalosVerde = Double.parseDouble(strGramosCafeMaloVerde);
                if(gramosMalosVerde > 15 || gramosMalosVerde < 0){
                    edtGramosCafeMaloVerde.setError("Ingrese una cantidad de café malo aceptable");
                    return;
                }
                cafeDañadoVerde = gramosMalosVerde + 5;
                final String strCafeDañadoVerde = Double.toString(cafeDañadoVerde);
                kilosCargaVerde = Double.parseDouble(strKilosCargaVerde);
                if(kilosCargaVerde <= 0){
                    edtKilosCargaVerde.setError("Ingrese una cantidad de kilos aceptable");
                    return;
                }
                kilosBuenosVerde = kilosCargaVerde -(kilosCargaVerde*cafeDañadoVerde/100);
                final String strKilosBuenosVerde = Double.toString(kilosBuenosVerde);
                precioCargaVerde = valorCargaVerde - (valorCargaVerde * cafeDañadoVerde/100);
                precioTotalCargaVerde = kilosCargaVerde * precioCargaVerde / 125;
                DecimalFormat formateador = new DecimalFormat("###,###.##");
                strFormatvalorPagoVerde = formateador.format(precioTotalCargaVerde);
                final String strPrecioTotalCargaVerde = Double.toString(precioTotalCargaVerde);

                //Toast.makeText(getActivity(),"idusuario:"+idUsuario+"nombresUsuario:"+nombresUsuario+"apellidosUsuario:"+apellidosUsuario+"idEmpresa:"+idEmpresa,Toast.LENGTH_LONG).show();
                //ALERT DIALOG
                View dialogo = inflater.inflate(R.layout.dialogo_personalizado,null);
                //COMENTAR VOC
                /*final List<String> arraySpinner = new ArrayList<String>();
                arraySpinner.add("COMPRAR");
                arraySpinner.add("VENDER");
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
                //final EditText edtDireccionCliente = (EditText)dialogo.findViewById(R.id.edtDireccionCliente);
                tvValorAPagar = (TextView)dialogo.findViewById(R.id.tvValorAPagar);
                tvValorAPagar.setText(strFormatvalorPagoVerde);

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
                        db_ventas.insertDataVentas(idUsuario,strFechaVerde,strHoraVerde,strPrecioCargaDiaVerde,strKilosCargaVerde,strPrecioTotalCargaVerde,strTipo,strMuestraVerde);
                        idVenta = db_ventas.findVentasByUsuario(idUsuario);
                        //Toast.makeText(getActivity(),"idUsuario"+idUsuario+"idVenta:"+idVenta,Toast.LENGTH_SHORT).show();
                        nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);
                        direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
                        telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
                        nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);
                        ciudadEmpresa = db_empresas.findCiudadEmpresaById(idEmpresa);
                        departamentoEmpresa = db_empresas.findDepartamentoEmpresaById(idEmpresa);
                        //Insertando datos en la tabla de clientes
                        db_clientes.insertDataClientes(nombresCliente,cedulaCliente,telefonoCliente,null);
                        //Inserción de datos en la tabla de café verde
                        //TODO agregar descuento estándar verde y Variedad
                        db_cafe_verde.insertDataCafeVerde(idVenta,"10",strCafeDañadoVerde,null,strMuestraVerde,strKilosBuenosVerde,strValorCargaVerde,strPrecioTotalCargaVerde);
                        Intent i = new Intent(getActivity(), FacturaActivity.class);
                        i.putExtra("idUsuario",idUsuario);
                        i.putExtra("idVenta",idVenta);
                        i.putExtra("idEmpresa",idEmpresa);
                        i.putExtra("nombreEmpresa",nombreEmpresa);
                        i.putExtra("direccionEmpresa",direccionEmpresa);
                        i.putExtra("telefonoEmpresa",telefonoEmpresa);
                        i.putExtra("nitEmpresa",nitEmpresa);
                        i.putExtra("departamentoEmpresa",departamentoEmpresa);
                        i.putExtra("ciudadEmpresa",ciudadEmpresa);
                        i.putExtra("nombresUsuario",nombresUsuario);
                        i.putExtra("apellidosUsuario",apellidosUsuario);
                        i.putExtra("tipo",strTipo);
                        i.putExtra("kilosTotalesVerde",strKilosCargaVerde);
                        i.putExtra("valorPagoVerde",strFormatvalorPagoVerde);
                        i.putExtra("doubleValorPagoVerde",precioTotalCargaVerde);
                        i.putExtra("nombresCliente",nombresCliente);
                        i.putExtra("cedulaCliente",cedulaCliente);
                        i.putExtra("telefonoCliente",telefonoCliente);
                        //i.putExtra("direccionCliente",direccionCliente);
                        i.putExtra("fecha",strFechaVerde);
                        i.putExtra("hora",strHoraVerde);
                        i.putExtra("fechahora",strFechaHora);
                        //i.putExtra("VoC",VoC);
                        startActivity(i);
                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogo);
                builder.setTitle("Factura");
                //builder.setMessage("El valor a pagar es de: $"+strFormatvalorPagoVerde);
                builder.show();


            }
        });
        return v;
    }
}
