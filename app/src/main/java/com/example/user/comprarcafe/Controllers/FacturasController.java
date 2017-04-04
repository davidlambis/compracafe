package com.example.user.comprarcafe.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;
import com.example.user.comprarcafe.Models.Factura;

import java.util.ArrayList;
import java.util.List;

public class FacturasController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public FacturasController(Context c) {
        context = c;
    }

    public FacturasController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    //Método para insertar datos en la tabla de facturas
    public void insertDataFacturas(long idVenta,long idCliente,String VoC, String nombreEmpresaFactura,String nombresUsuarioFactura,String apellidosUsuarioFactura,String tipoCafeFactura,String kilosTotalesFactura,String valorPagoFactura,String fechaFactura,String nitEmpresa,String nombresCliente,String cedulaCliente,String telefonoCliente,String horaFactura,String ciudadFactura){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_VENTA_ID_FACTURA,idVenta);
        values.put(SQLiteDBHelper.COLUMN_CLIENTE_ID_FACTURA,idCliente);
        values.put(SQLiteDBHelper.COLUMN_VOC_FACTURA,VoC);
        values.put(SQLiteDBHelper.COLUMN_NOMBRE_EMPRESA_FACTURA,nombreEmpresaFactura);
        values.put(SQLiteDBHelper.COLUMN_NOMBRES_USUARIO_FACTURA,nombresUsuarioFactura);
        values.put(SQLiteDBHelper.COLUMN_APELLIDOS_USUARIO_FACTURA,apellidosUsuarioFactura);
        values.put(SQLiteDBHelper.COLUMN_TIPO_CAFE_FACTURA,tipoCafeFactura);
        values.put(SQLiteDBHelper.COLUMN_KILOS_TOTALES_FACTURA,kilosTotalesFactura);
        values.put(SQLiteDBHelper.COLUMN_VALOR_PAGO_FACTURA,valorPagoFactura);
        values.put(SQLiteDBHelper.COLUMN_FECHA_FACTURA,fechaFactura);
        values.put(SQLiteDBHelper.COLUMN_NIT_EMPRESA,nitEmpresa);
        values.put(SQLiteDBHelper.COLUMN_NOMBRES_CLIENTE_FACTURA,nombresCliente);
        values.put(SQLiteDBHelper.COLUMN_CEDULA_CLIENTE_FACTURA,cedulaCliente);
        values.put(SQLiteDBHelper.COLUMN_TELEFONO_CLIENTE_FACTURA,telefonoCliente);
        values.put(SQLiteDBHelper.COLUMN_HORA_FACTURA,horaFactura);
        values.put(SQLiteDBHelper.COLUMN_CIUDAD_EMPRESA_FACTURA,ciudadFactura);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_FACTURAS,null,values);
    }


    //Método para listar todas las empresas
    /*public List<String> findFacturasByFecha(String nit,String fecha){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        List<String> arrayList = new ArrayList<String>();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_FACTURAS+" where "+SQLiteDBHelper.COLUMN_NIT_EMPRESA+" = '"+nit+"'"+" AND "+SQLiteDBHelper.COLUMN_FECHA_FACTURA+" = '"+fecha+"'";
        Cursor c = database.rawQuery(select,null);
        while(c.moveToNext()){
            arrayList.add(c.getString(c.getColumnIndex("nombreEmpresa")));
            arrayList.add(c.getString(c.getColumnIndex("tipoCafe")));
        }
        c.close();
        return arrayList;
    } */

    public ArrayList<Factura> findFacturasByFechaAndNit(String nit,String fecha) {

        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_FACTURAS+" where "+SQLiteDBHelper.COLUMN_NIT_EMPRESA+" = '"+nit+"'"+" AND "+SQLiteDBHelper.COLUMN_FECHA_FACTURA+" = '"+fecha+"'";
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Factura> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Factura factura = cursorToNote(cursor);
            list.add(factura);
        }
        return list;
    }

    /////Asignar datos de la base de datos al metodos Set
    private Factura cursorToNote(Cursor cursor) {
        Factura factura = new Factura();
        factura.setIdFactura(cursor.getLong(0));
        factura.setIdVenta(cursor.getLong(1));
        factura.setIdCliente(cursor.getLong(2));
        factura.setVoC(cursor.getString(3));
        factura.setNombreEmpresa(cursor.getString(4));
        factura.setNombresUsuario(cursor.getString(5));
        factura.setApellidosUsuario(cursor.getString(6));
        factura.setTipoCafe(cursor.getString(7));
        factura.setKilosTotales(cursor.getString(8));
        factura.setValorPago(cursor.getString(9));
        factura.setFecha(cursor.getString(10));
        factura.setNitEmpresa(cursor.getString(11));
        factura.setNombresCliente(cursor.getString(12));
        factura.setCedulaCliente(cursor.getString(13));
        factura.setTelefonoCliente(cursor.getString(14));
        factura.setHora(cursor.getString(15));
        factura.setCiudad(cursor.getString(16));
        //empresa.setEstadoEmpresa(cursor.getString(5));
        return factura;
    }


}
