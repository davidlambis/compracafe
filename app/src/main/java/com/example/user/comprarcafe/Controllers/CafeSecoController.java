package com.example.user.comprarcafe.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;

public class CafeSecoController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public CafeSecoController(Context c) {
        context = c;
    }

    public CafeSecoController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    //Método para insertar los datos del formulario en la tabla de Cafe Seco
    public void insertDataCafeSeco(long idVenta, String strCafeBueno,String strMerma, String strFactor,String strConstante,String strDifFactor,String strTara,String strKilosFinales,String strValorKilo,String strValorTotalSeco,String strVariedadSeco,String strMuestraSeco, String strValorCargaSeco){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_VENTA_ID,idVenta);
        values.put(SQLiteDBHelper.COLUMN_CAFE_BUENO,strCafeBueno);
        values.put(SQLiteDBHelper.COLUMN_CAFE_MERMA,strMerma);
        values.put(SQLiteDBHelper.COLUMN_CAFE_FACTOR,strFactor);
        values.put(SQLiteDBHelper.COLUMN_CAFE_CONSTANTE,strConstante);
        values.put(SQLiteDBHelper.COLUMN_CAFE_DIF_FACTOR,strDifFactor);
        values.put(SQLiteDBHelper.COLUMN_CAFE_TARA,strTara);
        values.put(SQLiteDBHelper.COLUMN_KILOS_FINALES,strKilosFinales);
        values.put(SQLiteDBHelper.COLUMN_VALOR_KILO,strValorKilo);
        values.put(SQLiteDBHelper.COLUMN_VALOR_TOTAL_SECO,strValorTotalSeco);
        values.put(SQLiteDBHelper.COLUMN_VARIEDAD_SECO,strVariedadSeco);
        values.put(SQLiteDBHelper.COLUMN_MUESTRA_SECO,strMuestraSeco);
        values.put(SQLiteDBHelper.COLUMN_VALOR_CARGA,strValorCargaSeco);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_CAFE_SECO,null,values);
    }





}
