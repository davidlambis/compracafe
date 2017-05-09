package com.example.user.comprarcafe.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;
import com.example.user.comprarcafe.Models.Empresa;

import java.util.ArrayList;

public class EmpresasController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public String nombreEmpresa, direccionEmpresa, telefonoEmpresa, nitEmpresa, estadoEmpresa, ciudadEmpresa, departamentoEmpresa;

    public EmpresasController(Context c) {
        context = c;
    }

    public EmpresasController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    //Método para insertar los datos del formulario en la tabla de Empresa
    public void InsertDataEmpresas(String strNombreCompraVenta, String strNit, String strDireccionCompraVenta, String strTelefonoCompraVenta, String strDepartamentoCompraVenta, String strCiudadCompraVenta) {
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_NOMBRE_EMPRESA, strNombreCompraVenta);
        values.put(SQLiteDBHelper.COLUMN_NIT, strNit);
        values.put(SQLiteDBHelper.COLUMN_DIRECCION_EMPRESA, strDireccionCompraVenta);
        values.put(SQLiteDBHelper.COLUMN_TELEFONO_EMPRESA, strTelefonoCompraVenta);
        values.put(SQLiteDBHelper.COLUMN_DEPARTAMENTO_EMPRESA, strDepartamentoCompraVenta);
        values.put(SQLiteDBHelper.COLUMN_CIUDAD_EMPRESA, strCiudadCompraVenta);
        //values.put(SQLiteDBHelper. COLUMN_ESTADO_EMPRESA,estadoEmpresa);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_EMPRESAS, null, values);
    }

    //Método para cargar empresas
    public ArrayList loadEmpresas() {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        ArrayList<Empresa> empresa = new ArrayList<Empresa>();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS;
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                Empresa getsetempresa = new Empresa();
                getsetempresa.setIdEmpresa(cursor.getInt(0));
                getsetempresa.setNombreEmpresa(cursor.getString(1));
                empresa.add(getsetempresa);
            } while (cursor.moveToNext());
        }
        return empresa;
    }

    //Método para listar todas las empresas
    public ArrayList<Empresa> findAllEmpresas() {

        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS;
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Empresa> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Empresa empresa = cursorToNote(cursor);
            list.add(empresa);
        }
        return list;
    }

    /////Asignar datos de la base de datos al metodos Set
    private Empresa cursorToNote(Cursor cursor) {
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(cursor.getLong(0));
        empresa.setNombreEmpresa(cursor.getString(1));
        empresa.setNIT(cursor.getString(2));
        empresa.setDireccionEmpresa(cursor.getString(3));
        empresa.setTelefonoEmpresa(cursor.getString(4));
        empresa.setDepartamentoEmpresa(cursor.getString(5));
        empresa.setCiudadEmpresa(cursor.getString(6));
        //empresa.setEstadoEmpresa(cursor.getString(5));
        return empresa;
    }

    public ArrayList<Empresa> findEmpresa(String nombreEmpresa) {

        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();

        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS + " where " + SQLiteDBHelper.COLUMN_NOMBRE_EMPRESA + " = '" + nombreEmpresa + "'";
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Empresa> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            Empresa empresa = cursorToNote(cursor);
            list.add(empresa);
        }
        return list;
    }

    public String findNombreEmpresaById(Long idEmpresa) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS + " where " + SQLiteDBHelper.COLUMN_ID_EMPRESA + " = '" + idEmpresa + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            nombreEmpresa = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_NOMBRE_EMPRESA));
        }
        return nombreEmpresa;
    }

    public String findDireccionEmpresaById(Long idEmpresa) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS + " where " + SQLiteDBHelper.COLUMN_ID_EMPRESA + " = '" + idEmpresa + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            direccionEmpresa = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_DIRECCION_EMPRESA));
        }
        return direccionEmpresa;
    }

    public String findTelefonoEmpresaById(Long idEmpresa) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS + " where " + SQLiteDBHelper.COLUMN_ID_EMPRESA + " = '" + idEmpresa + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            telefonoEmpresa = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_TELEFONO_EMPRESA));
        }
        return telefonoEmpresa;
    }

    public String findNitEmpresaById(Long idEmpresa) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS + " where " + SQLiteDBHelper.COLUMN_ID_EMPRESA + " = '" + idEmpresa + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            nitEmpresa = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_NIT));
        }
        return nitEmpresa;
    }

    public String findCiudadEmpresaById(Long idEmpresa) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS + " where " + SQLiteDBHelper.COLUMN_ID_EMPRESA + " = '" + idEmpresa + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            ciudadEmpresa = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_CIUDAD_EMPRESA));
        }
        return ciudadEmpresa;
    }

    public String findDepartamentoEmpresaById(Long idEmpresa) {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_EMPRESAS + " where " + SQLiteDBHelper.COLUMN_ID_EMPRESA + " = '" + idEmpresa + "'";
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            departamentoEmpresa = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_DEPARTAMENTO_EMPRESA));
        }
        return departamentoEmpresa;
    }

   /*public String findEstadoEmpresaById (Long idEmpresa){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select ="select * from " +SQLiteDBHelper.TABLE_NAME_EMPRESAS+" where "+ SQLiteDBHelper.COLUMN_ID_EMPRESA+" = '"+idEmpresa+"'";
        Cursor cursor = database.rawQuery(select,null);
        if(cursor.moveToFirst()){
            estadoEmpresa = cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_ESTADO_EMPRESA));
        }
        return estadoEmpresa;
    }*/


}
