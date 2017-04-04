package com.example.user.comprarcafe.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;

public class ClientesController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public long idCliente;

    public ClientesController(Context c) {
        context = c;
    }

    public ClientesController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    //Método para insertar datos en la tabla de clientes
    public void insertDataClientes(String nombresCliente, String cedulaCliente,String telefonoCliente,String direccionCliente){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_NOMBRE_CLIENTE,nombresCliente);
        values.put(SQLiteDBHelper.COLUMN_CEDULA_CLIENTE,cedulaCliente);
        values.put(SQLiteDBHelper.COLUMN_TELEFONO_CLIENTE,telefonoCliente);
        values.put(SQLiteDBHelper.COLUMN_DIRECCION_CLIENTE,direccionCliente);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_CLIENTES,null,values);
    }


    public long idClienteByNombre(String cedulaCliente){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select ="select * from " +SQLiteDBHelper.TABLE_NAME_CLIENTES+" where "+ SQLiteDBHelper.COLUMN_CEDULA_CLIENTE+" = '"+cedulaCliente+"'";
        Cursor cursor = database.rawQuery(select,null);
        if(cursor.moveToFirst()){
            idCliente = cursor.getLong(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_ID_CLIENTE));
        }
        return idCliente;
    }
}
