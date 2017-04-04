package com.example.user.comprarcafe.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;

public class CafeVerdeController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public CafeVerdeController(Context c) {
        context = c;
    }

    public CafeVerdeController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    //Método para insertar los datos del formulario en la tabla de Cafe Verde
    public void insertDataCafeVerde(long idVenta, String strDescuentoEstandarVerde,String strCafeDañadoVerde,String strVariedadVerde, String strMuestraVerde,String strKilosBuenosVerde,String strValorCargaVerde,String strValorTotalVerde){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_VENTA_ID_VERDE,idVenta);
        values.put(SQLiteDBHelper.COLUMN_DESCUENTO_ESTANDAR_VERDE,strDescuentoEstandarVerde);
        values.put(SQLiteDBHelper.COLUMN_CAFE_DAÑADO_VERDE,strCafeDañadoVerde);
        values.put(SQLiteDBHelper.COLUMN_VARIEDAD_VERDE,strVariedadVerde);
        values.put(SQLiteDBHelper.COLUMN_MUESTRA_VERDE,strMuestraVerde);
        values.put(SQLiteDBHelper.COLUMN_KILOS_BUENOS_VERDE,strKilosBuenosVerde);
        values.put(SQLiteDBHelper.COLUMN_VALOR_CARGA_VERDE,strValorCargaVerde);
        values.put(SQLiteDBHelper.COLUMN_VALOR_TOTAL_VERDE,strValorTotalVerde);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_CAFE_VERDE,null,values);
    }

}
