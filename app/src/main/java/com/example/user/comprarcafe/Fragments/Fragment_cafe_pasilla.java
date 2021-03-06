package com.example.user.comprarcafe.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.user.comprarcafe.Activities.FacturaActivity;
import com.example.user.comprarcafe.Controllers.CafePasillaController;
import com.example.user.comprarcafe.Controllers.ClientesController;
import com.example.user.comprarcafe.Controllers.EmpresasController;
import com.example.user.comprarcafe.Controllers.UsuariosController;
import com.example.user.comprarcafe.Controllers.VentasController;
import com.example.user.comprarcafe.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Fragment_cafe_pasilla extends Fragment {

    //Variables
    private EditText edtKilosCargaPasilla, edtKilosCargaZarandaPasilla, edtValorPuntoPasilla;
    private double kilosCargaZarandaPasilla, rinde, valorPuntoPasilla, valorArrobaPasilla, valorKiloPasilla, costoTotalCargaPasilla, kilosCargaPasilla;
    private Button btnCostoCargaPasilla;
    private TextView fechaPasilla, tvValorAPagar;
    private TextClock textClockHoraPasilla;
    private String VoC, strFechaPasilla, strHoraPasilla, strFechaHora, strTipo = "café pasilla", strMuestraPasilla = "250", nombresUsuario, apellidosUsuario, nombreEmpresa, direccionEmpresa, telefonoEmpresa, nitEmpresa, ciudadEmpresa, departamentoEmpresa;
    long idUsuario, idVenta, idEmpresa, id_usuario_logued;
    private String Nombre_Empresa, Nit_Empresa, Direccion_Empresa, Telefono_Empresa, Departamento_Empresa, Ciudad_Empresa;

    //Instancia del controlador de ventas
    VentasController db_ventas;

    //Instancia del controlador de usuarios
    UsuariosController db_usuarios;

    //Instancia del controlador de café verde
    CafePasillaController db_cafe_pasilla;

    //Instancia del controlador de Empresas
    EmpresasController db_empresas;

    //Instancia del controlador de clientes
    ClientesController db_clientes;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Infla la vista para el fragmento
        View v;
        v = inflater.inflate(R.layout.fragment_cafe_pasilla, null);

        //Obtención de datos de usuario por getIntent() que vienen del MainActivity
        idUsuario = getActivity().getIntent().getExtras().getLong("id");
        nombresUsuario = getActivity().getIntent().getExtras().getString("nombres");
        apellidosUsuario = getActivity().getIntent().getExtras().getString("apellidos");
        idEmpresa = getActivity().getIntent().getExtras().getLong("idEmpresa");
        Nombre_Empresa = getActivity().getIntent().getExtras().getString("Nombre_Empresa");
        Nit_Empresa = getActivity().getIntent().getExtras().getString("Nit_Empresa");
        Direccion_Empresa = getActivity().getIntent().getExtras().getString("Direccion_Empresa");
        Telefono_Empresa = getActivity().getIntent().getExtras().getString("Telefono_Empresa");
        Departamento_Empresa = getActivity().getIntent().getExtras().getString("Departamento_Empresa");
        Ciudad_Empresa = getActivity().getIntent().getExtras().getString("Ciudad_Empresa");
        id_usuario_logued = getActivity().getIntent().getExtras().getLong("id_usuario_logued");

        //Conexión al controlador de Ventas
        db_ventas = new VentasController(v.getContext());
        db_ventas.abrirBaseDeDatos();

        //Conexión al controlador de Usuarios
        db_usuarios = new UsuariosController(v.getContext());
        db_usuarios.abrirBaseDeDatos();

        //Conexión al controlador de Café Verde
        db_cafe_pasilla = new CafePasillaController(v.getContext());
        db_cafe_pasilla.abrirBaseDeDatos();

        //Conexión al controlador de Empresas
        db_empresas = new EmpresasController(v.getContext());
        db_empresas.abrirBaseDeDatos();

        //Conexión al controlador de clientes
        db_clientes = new ClientesController(v.getContext());
        db_clientes.abrirBaseDeDatos();

        //Llamado de objetos del layout
        fechaPasilla = (TextView) v.findViewById(R.id.fechaPasilla);
        textClockHoraPasilla = (TextClock) v.findViewById(R.id.textClockPasilla);
        edtKilosCargaPasilla = (EditText) v.findViewById(R.id.edtKilosCargaPasilla);
        edtKilosCargaZarandaPasilla = (EditText) v.findViewById(R.id.edtKilosCargaZarandaPasilla);
        edtValorPuntoPasilla = (EditText) v.findViewById(R.id.edtValorPuntoPasilla);

        //Obtener Fecha
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        strFechaPasilla = dateFormat.format(date);
        fechaPasilla.setText(strFechaPasilla);
        //Obtener Hora
        Calendar cal = Calendar.getInstance();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        strHoraPasilla = timeFormat.format(cal.getTime());
        textClockHoraPasilla.setText(strHoraPasilla);
        //Obtener FechaHora
        DateFormat dateFormatFH = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        dateFormatFH.setTimeZone(TimeZone.getTimeZone("Africa/Abidjan"));
        Date dateFH = new Date();
        strFechaHora = dateFormatFH.format(dateFH);

        //Listener del botón de Calcular el valor a pagar
        btnCostoCargaPasilla = (Button) v.findViewById(R.id.btnCostoCargaPasilla);
        btnCostoCargaPasilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strKilosCargaPasilla = edtKilosCargaPasilla.getText().toString();
                if (TextUtils.isEmpty(strKilosCargaPasilla)) {
                    edtKilosCargaPasilla.setError("Llena este campo");
                    return;
                }
                final String strKilosCargaZarandaPasilla = edtKilosCargaZarandaPasilla.getText().toString();
                if (TextUtils.isEmpty(strKilosCargaZarandaPasilla)) {
                    edtKilosCargaZarandaPasilla.setError("Llena este campo");
                    return;
                }
                final String strValorPuntoPasilla = edtValorPuntoPasilla.getText().toString();
                if (TextUtils.isEmpty(strValorPuntoPasilla)) {
                    edtValorPuntoPasilla.setError("Llena este campo");
                    return;
                }
                kilosCargaPasilla = Double.parseDouble(strKilosCargaPasilla);
                kilosCargaZarandaPasilla = Double.parseDouble(strKilosCargaZarandaPasilla);
                if (kilosCargaZarandaPasilla > kilosCargaPasilla) {
                    edtKilosCargaZarandaPasilla.setError("Los kilos de zaranda no pueden ser mayores a los kilos totales de la carga");
                    return;
                }
                if (kilosCargaZarandaPasilla <= 0) {
                    edtKilosCargaZarandaPasilla.setError("Ingrese una cantidad de kilos de zaranda aceptable");
                    return;
                }
                if (kilosCargaPasilla <= 0) {
                    edtKilosCargaPasilla.setError("Ingrese una cantidad de kilos aceptable");
                    return;
                }
                rinde = kilosCargaZarandaPasilla / 2.5;
                final String strRindePasilla = Double.toString(rinde);
                valorPuntoPasilla = Double.parseDouble(strValorPuntoPasilla);
                if (valorPuntoPasilla <= 0) {
                    edtValorPuntoPasilla.setError("Ingrese un valor punto aceptable");
                    return;
                }
                valorArrobaPasilla = rinde * valorPuntoPasilla;
                final String strValorArrobaPasilla = Double.toString(valorArrobaPasilla);
                valorKiloPasilla = valorArrobaPasilla / 12.5;
                costoTotalCargaPasilla = valorKiloPasilla * kilosCargaZarandaPasilla;
                final String strCostoTotalCargaPasilla = Double.toString(costoTotalCargaPasilla);
                DecimalFormat formateador = new DecimalFormat("###,###.##");
                final String strFormatvalorPagoPasilla = formateador.format(costoTotalCargaPasilla);


                //Infla un dialogo con el layout de dialogo personalizado donde se piden datos del cliente para completar los datos de la factura
                View dialogo = inflater.inflate(R.layout.dialogo_personalizado, null);
                final EditText edtNombresCliente = (EditText) dialogo.findViewById(R.id.edtNombreCliente);
                final EditText edtCedulaCliente = (EditText) dialogo.findViewById(R.id.edtCedulaCliente);
                final EditText edtTelefonoCliente = (EditText) dialogo.findViewById(R.id.edtTelefonoCliente);
                //final EditText edtDireccionCliente = (EditText)dialogo.findViewById(R.id.edtDireccionCliente);
                tvValorAPagar = (TextView) dialogo.findViewById(R.id.tvValorAPagar);
                tvValorAPagar.setText(strFormatvalorPagoPasilla);


                Button btnGenerarFactura = (Button) dialogo.findViewById(R.id.btnGenerarFactura);
                btnGenerarFactura.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nombresCliente = edtNombresCliente.getText().toString();
                        final String cedulaCliente = edtCedulaCliente.getText().toString();
                        final String telefonoCliente = edtTelefonoCliente.getText().toString();

                        if (TextUtils.isEmpty(nombresCliente)) {
                            edtNombresCliente.setError("Llena este campo");
                            return;
                        }
                        if (TextUtils.isEmpty(cedulaCliente)) {
                            edtCedulaCliente.setError("Llena este campo");
                            return;
                        }
                        if (TextUtils.isEmpty(telefonoCliente)) {
                            edtTelefonoCliente.setError("Llena este campo");
                            return;
                        }
                        //Inserción de datos en la tabla de ventas
                        //TODO agregar Precio del día de la carga
                        db_ventas.insertDataVentas(idUsuario, strFechaPasilla, strHoraPasilla, null, strKilosCargaZarandaPasilla, strCostoTotalCargaPasilla, strTipo, strMuestraPasilla);
                        idVenta = db_ventas.findVentasByUsuario(idUsuario);

                        //Halla estos datos en base al idEmpresa asignado
                        nombreEmpresa = db_empresas.findNombreEmpresaById(idEmpresa);
                        direccionEmpresa = db_empresas.findDireccionEmpresaById(idEmpresa);
                        telefonoEmpresa = db_empresas.findTelefonoEmpresaById(idEmpresa);
                        nitEmpresa = db_empresas.findNitEmpresaById(idEmpresa);
                        ciudadEmpresa = db_empresas.findCiudadEmpresaById(idEmpresa);
                        departamentoEmpresa = db_empresas.findDepartamentoEmpresaById(idEmpresa);
                        //Insertando datos en la tabla de clientes
                        db_clientes.insertDataClientes(nombresCliente, cedulaCliente, telefonoCliente, null);
                        //Inserción de datos en la tabla de café pasilla
                        //TODO Agregar variedad
                        db_cafe_pasilla.insertDataCafePasilla(idVenta, strKilosCargaPasilla, strKilosCargaZarandaPasilla, strValorPuntoPasilla, strRindePasilla, null, strMuestraPasilla, strValorArrobaPasilla, strCostoTotalCargaPasilla);
                        //Inicia actividad de factura y pasa todos los datos a esa actividad mediante el intent.
                        Intent i = new Intent(getActivity(), FacturaActivity.class);
                        i.putExtra("idUsuario", idUsuario);
                        i.putExtra("idVenta", idVenta);
                        i.putExtra("idEmpresa", idEmpresa);
                        i.putExtra("nombreEmpresa", Nombre_Empresa);
                        i.putExtra("direccionEmpresa", Direccion_Empresa);
                        i.putExtra("telefonoEmpresa", Telefono_Empresa);
                        i.putExtra("nitEmpresa", Nit_Empresa);
                        i.putExtra("departamentoEmpresa", Departamento_Empresa);
                        i.putExtra("ciudadEmpresa", Ciudad_Empresa);
                        i.putExtra("nombresUsuario", nombresUsuario);
                        i.putExtra("apellidosUsuario", apellidosUsuario);
                        i.putExtra("tipo", strTipo);
                        i.putExtra("kilosTotalesPasilla", strKilosCargaPasilla);
                        i.putExtra("valorPagoPasilla", strFormatvalorPagoPasilla);
                        i.putExtra("doubleValorPagoPasilla", costoTotalCargaPasilla);
                        i.putExtra("nombresCliente", nombresCliente);
                        i.putExtra("cedulaCliente", cedulaCliente);
                        i.putExtra("telefonoCliente", telefonoCliente);
                        //i.putExtra("direccionCliente",direccionCliente);
                        i.putExtra("fecha", strFechaPasilla);
                        i.putExtra("hora", strHoraPasilla);
                        i.putExtra("fechahora", strFechaHora);
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
