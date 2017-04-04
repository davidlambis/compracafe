package com.example.user.comprarcafe.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;

public class CafePasillaController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public CafePasillaController(Context c) {
        context = c;
    }

    public CafePasillaController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    //Método para insertar los datos del formulario en la tabla de Cafe Pasilla
    public void insertDataCafePasilla(long idVenta,String strKilosTotalesPasilla,String strKilosZarandaPasilla,String strValorPuntoPasilla,String strRindePasilla,String strVariedadPasilla,String strMuestraPasilla,String strValorArrobaPasilla,String strValorTotalPasilla){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_VENTA_ID_PASILLA,idVenta);
        values.put(SQLiteDBHelper.COLUMN_KILOS_TOTALES_PASILLA,strKilosTotalesPasilla);
        values.put(SQLiteDBHelper.COLUMN_KILOS_ZARANDA_PASILLA,strKilosZarandaPasilla);
        values.put(SQLiteDBHelper.COLUMN_VALOR_PUNTO_PASILLA,strValorPuntoPasilla);
        values.put(SQLiteDBHelper.COLUMN_RINDE_PASILLA,strRindePasilla);
        values.put(SQLiteDBHelper.COLUMN_VARIEDAD_PASILLA,strVariedadPasilla);
        values.put(SQLiteDBHelper.COLUMN_MUESTRA_PASILLA,strMuestraPasilla);
        values.put(SQLiteDBHelper.COLUMN_VALOR_ARROBA_PASILLA,strValorArrobaPasilla);
        values.put(SQLiteDBHelper.COLUMN_VALOR_TOTAL_PASILLA,strValorTotalPasilla);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_CAFE_PASILLA,null,values);
    }

}
