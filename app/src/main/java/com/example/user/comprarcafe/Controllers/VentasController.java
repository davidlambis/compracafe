package com.example.user.comprarcafe.Controllers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;
import com.example.user.comprarcafe.Models.Venta;

import java.util.ArrayList;

public class VentasController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public long idVenta;

    public VentasController(Context c) {
        context = c;
    }

    public VentasController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    //Método para insertar los datos del formulario en la tabla de Ventas
    public void insertDataVentas(long idUsuario, String strFecha,String strHora, String strPrecioDia, String strKilosTotales, String strValorTotal, String strTipo, String strMuestra){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_ID_USUARIO,idUsuario);
        values.put(SQLiteDBHelper.COLUMN_FECHA,strFecha);
        values.put(SQLiteDBHelper.COLUMN_HORA,strHora);
        values.put(SQLiteDBHelper.COLUMN_PRECIO_DIA,strPrecioDia);
        values.put(SQLiteDBHelper.COLUMN_KILOS_TOTALES,strKilosTotales);
        values.put(SQLiteDBHelper.COLUMN_VALOR_TOTAL,strValorTotal);
        values.put(SQLiteDBHelper.COLUMN_TIPO,strTipo);
        values.put(SQLiteDBHelper.COLUMN_MUESTRA,strMuestra);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_VENTAS,null,values);
    }

    //Método para cargar venta
    public ArrayList loadVenta() {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        ArrayList<Venta> venta = new ArrayList<Venta>();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_VENTAS;
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do {
                Venta getsetventa= new Venta();
                getsetventa.setIdVenta(cursor.getLong(0));
                venta.add(getsetventa);
            }while (cursor.moveToNext());
        }
        return venta;
    }

    //Método para cargar todas las ventas
    public ArrayList<Venta> findAllVentas() {

        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_VENTAS;
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Venta> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Venta venta = cursorToNote(cursor);
            list.add(venta);
        }
        return list;
    }


     public long findVentasByUsuario(long idUsuario) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();

        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_VENTAS+" where "+SQLiteDBHelper.COLUMN_ID_USUARIO+" = '"+idUsuario+"' ORDER BY "+SQLiteDBHelper.COLUMN_ID_VENTA+" DESC LIMIT 1";

        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            idVenta = cursor.getLong(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_ID_VENTA));
        }
         return idVenta;
    }

    /////Asignar datos de la base de datos al metodos Set
    private Venta cursorToNote(Cursor cursor) {
        Venta venta = new Venta();
        venta.setIdVenta(cursor.getLong(0));
        venta.setIdUsuario(cursor.getLong(1));
        venta.setFecha(cursor.getString(2));
        venta.setPrecioDia(cursor.getInt(3));
        venta.setKilosTotales(cursor.getDouble(4));
        venta.setValorTotal(cursor.getDouble(5));
        venta.setTipo(cursor.getString(6));
        venta.setMuestra(cursor.getInt(7));
        return venta;
    }


}
