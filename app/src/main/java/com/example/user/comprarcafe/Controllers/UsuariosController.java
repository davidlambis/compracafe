package com.example.user.comprarcafe.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.comprarcafe.Database.SQLiteDBHelper;
import com.example.user.comprarcafe.Models.Usuario;

import java.util.ArrayList;

public class UsuariosController {
    private SQLiteDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    long idEmpresa,idUsuario;
    String nombres,apellidos;

    public UsuariosController(Context c) {
        context = c;
    }

    public UsuariosController abrirBaseDeDatos() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        dbHelper.close();
    }

    //Método para insertar los datos del formulario en la tabla de usuarios
    public void InsertDataUsuarios(long idEmpresa, String strNombresUsuario, String strApellidosUsuario, String strCedulaUsuario,String strDireccionUsuario, String strTelefonoUsuario, String strCorreoUsuario, String strContraseñaUsuario,String estadoSesion){
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_EMPRESA_ID,idEmpresa);
        values.put(SQLiteDBHelper.COLUMN_NOMBRES,strNombresUsuario);
        values.put(SQLiteDBHelper.COLUMN_APELLIDOS,strApellidosUsuario);
        values.put(SQLiteDBHelper.COLUMN_CEDULA,strCedulaUsuario);
        values.put(SQLiteDBHelper.COLUMN_DIRECCION,strDireccionUsuario);
        values.put(SQLiteDBHelper.COLUMN_TELEFONO,strTelefonoUsuario);
        values.put(SQLiteDBHelper.COLUMN_CORREO,strCorreoUsuario);
        values.put(SQLiteDBHelper.COLUMN_CONTRASEÑA,strContraseñaUsuario);
        values.put(SQLiteDBHelper.COLUMN_ESTADO_SESION,estadoSesion);
        long id = database.insert(SQLiteDBHelper.TABLE_NAME_USUARIOS,null,values);
    }

    public Cursor validarLogin(String correo, String contraseña){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SQLiteDBHelper.TABLE_NAME_USUARIOS + " WHERE " + SQLiteDBHelper.COLUMN_CORREO + "=? AND " + SQLiteDBHelper.COLUMN_CONTRASEÑA + "=?", new String[]{correo, contraseña});
        return cursor;
    }

    public ArrayList loadUsuarios() {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        ArrayList<Usuario> usuario = new ArrayList<Usuario>();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_USUARIOS;
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()){
            do {
                Usuario getsetusuario= new Usuario();
                getsetusuario.setIdUsuario(cursor.getLong(0));
                getsetusuario.setNombres(cursor.getString(1));
                getsetusuario.setApellidos(cursor.getString(2));
                getsetusuario.setCedula(cursor.getInt(3));
                getsetusuario.setDireccion(cursor.getString(4));
                getsetusuario.setTelefono(cursor.getInt(5));
                getsetusuario.setCorreo(cursor.getString(6));
                getsetusuario.setContraseña(cursor.getString(7));
                getsetusuario.setIdEmpresa(cursor.getLong(8));
                getsetusuario.setEstadoSesion(cursor.getString(9));
                usuario.add(getsetusuario);
            }while (cursor.moveToNext());
        }
        return usuario;
    }

    public long idUsuarioByCorreo(String correo){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_USUARIOS+" where "+SQLiteDBHelper.COLUMN_CORREO+" = '"+correo+"'";
        Cursor cursor = database.rawQuery(select,null);
        if(cursor.moveToFirst()){
            idUsuario = cursor.getLong(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_ID));
        }
        return idUsuario;
    }
    public String nombresUsuarioByCorreo(String correo){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_USUARIOS+" where "+SQLiteDBHelper.COLUMN_CORREO+" = '"+correo+"'";
        Cursor cursor = database.rawQuery(select,null);
        if(cursor.moveToFirst()){
            nombres= cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_NOMBRES));
        }
        return nombres;
    }
    public String apellidosUsuarioByCorreo(String correo){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_USUARIOS+" where "+SQLiteDBHelper.COLUMN_CORREO+" = '"+correo+"'";
        Cursor cursor = database.rawQuery(select,null);
        if(cursor.moveToFirst()){
            apellidos= cursor.getString(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_APELLIDOS));
        }
        return apellidos;
    }

    public long idEmpresaByCorreo(String correo){
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_USUARIOS+" where "+SQLiteDBHelper.COLUMN_CORREO+" = '"+correo+"'";
        Cursor cursor = database.rawQuery(select,null);
        if(cursor.moveToFirst()){
            idEmpresa = cursor.getLong(cursor.getColumnIndex(SQLiteDBHelper.COLUMN_EMPRESA_ID));
        }
        return idEmpresa;
    }

    public ArrayList<Usuario> findUsuario(String correo) {

        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();

        String select = "select * from "+SQLiteDBHelper.TABLE_NAME_USUARIOS+" where "+SQLiteDBHelper.COLUMN_CORREO+" = '"+correo+"'";
        Cursor cursor = database.rawQuery(select, null);
        ///Cursor cursor = database.query(table, columns, null, null, null, null, null);
        ArrayList<Usuario> list = new ArrayList<>();
        if(cursor.moveToFirst()) {
            Usuario usuario = cursorToNote(cursor);
            list.add(usuario);
        }
        return list;
    }


    //Método para listar todas las empresas
    public ArrayList<Usuario> findAllUsuarios() {

        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        String select = "select * from " + SQLiteDBHelper.TABLE_NAME_USUARIOS;
        Cursor cursor = database.rawQuery(select, null);
        ArrayList<Usuario> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Usuario usuario = cursorToNote(cursor);
            list.add(usuario);
        }
        return list;
    }

    /////Asignar datos de la base de datos al metodos Set
    private Usuario cursorToNote(Cursor cursor) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(cursor.getLong(0));
        usuario.setNombres(cursor.getString(1));
        usuario.setApellidos(cursor.getString(2));
        usuario.setCedula(cursor.getInt(3));
        usuario.setDireccion(cursor.getString(4));
        usuario.setTelefono(cursor.getInt(5));
        usuario.setCorreo(cursor.getString(6));
        usuario.setContraseña(cursor.getString(7));
        usuario.setIdEmpresa(cursor.getLong(8));
        usuario.setEstadoSesion(cursor.getString(9));
        return usuario;
    }

    /// Actualizar
    public  void actualizarUsuario(Long idusuario, String Estado_Sesion){
        ContentValues cv = new ContentValues();

        cv.put(SQLiteDBHelper.COLUMN_ESTADO_SESION, Estado_Sesion);
        database.update(SQLiteDBHelper.TABLE_NAME_USUARIOS, cv,SQLiteDBHelper.COLUMN_ID+"="+ idusuario, null);

    }


}