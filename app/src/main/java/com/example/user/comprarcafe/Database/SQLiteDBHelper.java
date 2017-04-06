package com.example.user.comprarcafe.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Esquema de la base de datos
public class SQLiteDBHelper extends SQLiteOpenHelper {

    //Nombre de la base de datos y versión de la misma
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    //Datos de la tabla de usuarios
    public static final String TABLE_NAME_USUARIOS = "usuarios";
    public static final String COLUMN_ID =  "idUsuario";
    public static final String COLUMN_EMPRESA_ID =  "idEmpresa";
    public static final String COLUMN_ESTADO_SESION =  "estadoSesion";
    public static final String COLUMN_NOMBRES =  "nombres";
    public static final String COLUMN_APELLIDOS =  "apellidos";
    public static final String COLUMN_CEDULA =  "cedula";
    public static final String COLUMN_DIRECCION =  "direccion";
    public static final String COLUMN_TELEFONO =  "telefono";
    public static final String COLUMN_CORREO =  "correo";
    public static final String COLUMN_CONTRASEÑA =  "contraseña";

    //Datos de la tabla de Empresa
    public static final String TABLE_NAME_EMPRESAS = "empresas";
    public static final String COLUMN_ID_EMPRESA =  "idEmpresa";
    //public static final String COLUMN_ESTADO_EMPRESA =  "Estado";
    public static final String COLUMN_NOMBRE_EMPRESA =  "nombreEmpresa";
    public static final String COLUMN_NIT =  "NIT";
    public static final String COLUMN_DIRECCION_EMPRESA =  "direccionEmpresa";
    public static final String COLUMN_TELEFONO_EMPRESA =  "telefonoEmpresa";
    public static final String COLUMN_DEPARTAMENTO_EMPRESA =  "departamentoEmpresa";
    public static final String COLUMN_CIUDAD_EMPRESA =  "ciudadEmpresa";

    //Datos de la tabla de venta
    public static final String TABLE_NAME_VENTAS = "ventas";
    public static final String COLUMN_ID_VENTA =  "idVenta";
    public static final String COLUMN_ID_USUARIO =  "idUsuario";
    public static final String COLUMN_FECHA =  "fecha";
    public static final String COLUMN_HORA =  "hora";
    public static final String COLUMN_PRECIO_DIA =  "precioDia";
    public static final String COLUMN_KILOS_TOTALES =  "kilosTotales";
    public static final String COLUMN_VALOR_TOTAL =  "valorTotal";
    public static final String COLUMN_TIPO =  "tipo";
    public static final String COLUMN_MUESTRA =  "muestra";

    //Datos de la tabla de café seco
    public static final String TABLE_NAME_CAFE_SECO = "cafeseco";
    public static final String COLUMN_ID_CAFE_SECO =  "idCafeSeco";
    public static final String COLUMN_VENTA_ID =  "idVenta";
    public static final String COLUMN_CAFE_BUENO =  "cafeBueno";
    public static final String COLUMN_CAFE_MERMA =  "merma";
    public static final String COLUMN_CAFE_FACTOR =  "factor";
    public static final String COLUMN_CAFE_CONSTANTE =  "constante";
    public static final String COLUMN_CAFE_DIF_FACTOR =  "diferenciaFactor";
    public static final String COLUMN_CAFE_TARA =  "tara";
    public static final String COLUMN_KILOS_FINALES =  "kilosFinales";
    public static final String COLUMN_VALOR_KILO =  "valorKilo";
    public static final String COLUMN_VALOR_TOTAL_SECO =  "valorTotal";
    public static final String COLUMN_VARIEDAD_SECO =  "variedad";
    public static final String COLUMN_MUESTRA_SECO=  "muestra";
    public static final String COLUMN_VALOR_CARGA =  "valorCarga";

    //Datos de la tabla de café verde
    public static final String TABLE_NAME_CAFE_VERDE = "cafeverde";
    public static final String COLUMN_ID_CAFE_VERDE =  "idCafeVerde";
    public static final String COLUMN_VENTA_ID_VERDE =  "idVenta";
    public static final String COLUMN_DESCUENTO_ESTANDAR_VERDE =  "descuentoEstandar";
    public static final String COLUMN_CAFE_DAÑADO_VERDE =  "cafeDañado";
    public static final String COLUMN_VARIEDAD_VERDE =  "variedad";
    public static final String COLUMN_MUESTRA_VERDE=  "muestra";
    public static final String COLUMN_KILOS_BUENOS_VERDE =  "kilosBuenos";
    public static final String COLUMN_VALOR_CARGA_VERDE =  "valorCarga";
    public static final String COLUMN_VALOR_TOTAL_VERDE =  "valorTotal";

    //Datos de la tabla de café pasilla
    public static final String TABLE_NAME_CAFE_PASILLA = "cafepasilla";
    public static final String COLUMN_ID_CAFE_PASILLA =  "idCafePasilla";
    public static final String COLUMN_VENTA_ID_PASILLA =  "idVenta";
    public static final String COLUMN_KILOS_TOTALES_PASILLA =  "kilosTotales";
    public static final String COLUMN_KILOS_ZARANDA_PASILLA =  "kilosZaranda";
    public static final String COLUMN_VALOR_PUNTO_PASILLA =  "valorPunto";
    public static final String COLUMN_RINDE_PASILLA =  "rinde";
    public static final String COLUMN_VARIEDAD_PASILLA =  "variedad";
    public static final String COLUMN_MUESTRA_PASILLA=  "muestra";
    public static final String COLUMN_VALOR_ARROBA_PASILLA =  "valorArroba";
    public static final String COLUMN_VALOR_TOTAL_PASILLA =  "valorTotal";

    //Datos de la tabla de clientes
    public static final String TABLE_NAME_CLIENTES = "clientes";
    public static final String COLUMN_ID_CLIENTE =  "idClientes";
    public static final String COLUMN_NOMBRE_CLIENTE =  "nombresCliente";
    public static final String COLUMN_CEDULA_CLIENTE =  "cedulaCliente";
    public static final String COLUMN_TELEFONO_CLIENTE = "telefonoCliente";
    public static final String COLUMN_DIRECCION_CLIENTE = "direccionCliente";

    //Datos de la tabla de Facturas
    public static final String TABLE_NAME_FACTURAS = "facturas";
    public static final String COLUMN_ID_FACTURA =  "idFactura";
    public static final String COLUMN_VENTA_ID_FACTURA =  "idVenta";
    public static final String COLUMN_CLIENTE_ID_FACTURA =  "idCliente";
    public static final String COLUMN_VOC_FACTURA =  "vendeOcompra";
    public static final String COLUMN_NOMBRE_EMPRESA_FACTURA =  "nombreEmpresa";
    public static final String COLUMN_NOMBRES_USUARIO_FACTURA =  "nombresUsuario";
    public static final String COLUMN_APELLIDOS_USUARIO_FACTURA =  "apellidosUsuario";
    public static final String COLUMN_TIPO_CAFE_FACTURA =  "tipoCafe";
    public static final String COLUMN_KILOS_TOTALES_FACTURA =  "kilosTotales";
    public static final String COLUMN_VALOR_PAGO_FACTURA = "valorTotalPago";
    public static final String COLUMN_FECHA_FACTURA = "fechaFactura";
    public static final String COLUMN_NIT_EMPRESA = "nitEmpresa";
    public static final String COLUMN_NOMBRES_CLIENTE_FACTURA = "nombresCliente";
    public static final String COLUMN_CEDULA_CLIENTE_FACTURA = "cedulaCliente";
    public static final String COLUMN_TELEFONO_CLIENTE_FACTURA = "telefonoCliente";
    public static final String COLUMN_HORA_FACTURA = "horaFactura";
    public static final String COLUMN_DEPARTAMENTO_EMPRESA_FACTURA = "departamentoEmpresaFactura";
    public static final String COLUMN_CIUDAD_EMPRESA_FACTURA = "ciudadEmpresaFactura";

    //Sintaxix SQL para crear la tabla de usuarios
    private static final String CREATE_TABLE_QUERY_USUARIOS =
            "CREATE TABLE " + TABLE_NAME_USUARIOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRES + " TEXT , "+
                    COLUMN_APELLIDOS + " TEXT , " +
                    COLUMN_CEDULA + " TEXT , " +
                    COLUMN_DIRECCION + " TEXT , " +
                    COLUMN_TELEFONO + " TEXT , " +
                    COLUMN_CORREO + " TEXT , " +
                    COLUMN_CONTRASEÑA + " TEXT  , " +
                    COLUMN_EMPRESA_ID + " INTEGER , " +
                    COLUMN_ESTADO_SESION + " TEXT , " +
                    "FOREIGN KEY("+COLUMN_EMPRESA_ID+") REFERENCES "+TABLE_NAME_EMPRESAS+"("+COLUMN_ID_EMPRESA+"));";

    //Sintaxix SQL para crear la tabla de empresas
    private static final String CREATE_TABLE_QUERY_EMPRESAS =
            "CREATE TABLE " + TABLE_NAME_EMPRESAS + " (" +
                    COLUMN_ID_EMPRESA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE_EMPRESA + " TEXT , "+
                    COLUMN_NIT + " TEXT , " +
                    COLUMN_DIRECCION_EMPRESA + " TEXT , " +
                    COLUMN_TELEFONO_EMPRESA + " TEXT , " +
                    COLUMN_DEPARTAMENTO_EMPRESA + " TEXT , " +
                    COLUMN_CIUDAD_EMPRESA + " TEXT " + ")";
                    //COLUMN_ESTADO_EMPRESA + " TEXT " + ")";

    //Sintaxix SQL para crear la tabla de ventas
    private static final String CREATE_TABLE_QUERY_VENTAS =
            "CREATE TABLE " + TABLE_NAME_VENTAS + " (" +
                    COLUMN_ID_VENTA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ID_USUARIO + " INTEGER ," +
                    COLUMN_FECHA + " TEXT  , "+
                    COLUMN_HORA + " TEXT  , "+
                    COLUMN_PRECIO_DIA + " TEXT, " +
                    COLUMN_KILOS_TOTALES + " TEXT , " +
                    COLUMN_VALOR_TOTAL + " TEXT , " +
                    COLUMN_TIPO + " TEXT , " +
                    COLUMN_MUESTRA + " TEXT , " +
                    "FOREIGN KEY("+COLUMN_ID_USUARIO+") REFERENCES "+TABLE_NAME_USUARIOS+"("+COLUMN_ID+"));";

    //Sintaxix SQL para crear la tabla de Café Seco
    private static final String CREATE_TABLE_QUERY_CAFE_SECO =
            "CREATE TABLE " + TABLE_NAME_CAFE_SECO + " (" +
                    COLUMN_ID_CAFE_SECO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VENTA_ID + " INTEGER ," +
                    COLUMN_CAFE_BUENO + " TEXT , "+
                    COLUMN_CAFE_MERMA + " TEXT , "+
                    COLUMN_CAFE_FACTOR + " TEXT , "+
                    COLUMN_CAFE_CONSTANTE + " TEXT  , "+
                    COLUMN_CAFE_DIF_FACTOR + " TEXT , "+
                    COLUMN_CAFE_TARA + " TEXT  , "+
                    COLUMN_KILOS_FINALES + " TEXT , "+
                    COLUMN_VALOR_KILO + " TEXT , "+
                    COLUMN_VALOR_TOTAL_SECO + " TEXT  , "+
                    COLUMN_VARIEDAD_SECO + " TEXT , "+
                    COLUMN_MUESTRA_SECO + " TEXT  , "+
                    COLUMN_VALOR_CARGA + " TEXT , " +
                    "FOREIGN KEY("+COLUMN_VENTA_ID+") REFERENCES "+TABLE_NAME_VENTAS+"("+COLUMN_ID_VENTA+"));";

    //Sintaxix SQL para crear la tabla de Café Verde
    private static final String CREATE_TABLE_QUERY_CAFE_VERDE =
            "CREATE TABLE " + TABLE_NAME_CAFE_VERDE + " (" +
                    COLUMN_ID_CAFE_VERDE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VENTA_ID_VERDE + " INTEGER ," +
                    COLUMN_DESCUENTO_ESTANDAR_VERDE + " TEXT , "+
                    COLUMN_CAFE_DAÑADO_VERDE + " TEXT  , "+
                    COLUMN_VARIEDAD_VERDE + " TEXT , "+
                    COLUMN_MUESTRA_VERDE + " TEXT  , "+
                    COLUMN_KILOS_BUENOS_VERDE+ " TEXT , "+
                    COLUMN_VALOR_CARGA_VERDE+ " TEXT , "+
                    COLUMN_VALOR_TOTAL_VERDE+ " TEXT , "+
                    "FOREIGN KEY("+COLUMN_VENTA_ID_VERDE+") REFERENCES "+TABLE_NAME_VENTAS+"("+COLUMN_ID_VENTA+"));";

    //Sintaxix SQL para crear la tabla de Café Pasilla
    private static final String CREATE_TABLE_QUERY_CAFE_PASILLA =
            "CREATE TABLE " + TABLE_NAME_CAFE_PASILLA + " (" +
                    COLUMN_ID_CAFE_PASILLA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VENTA_ID_PASILLA+ " INTEGER, " +
                    COLUMN_KILOS_TOTALES_PASILLA+ " TEXT, " +
                    COLUMN_KILOS_ZARANDA_PASILLA+ " TEXT, " +
                    COLUMN_VALOR_PUNTO_PASILLA+ " TEXT, " +
                    COLUMN_RINDE_PASILLA+ " TEXT, " +
                    COLUMN_VARIEDAD_PASILLA+ " TEXT, " +
                    COLUMN_MUESTRA_PASILLA+ " TEXT, " +
                    COLUMN_VALOR_ARROBA_PASILLA+ " TEXT, " +
                    COLUMN_VALOR_TOTAL_PASILLA+ " TEXT, " +
                    "FOREIGN KEY("+COLUMN_VENTA_ID_PASILLA+") REFERENCES "+TABLE_NAME_VENTAS+"("+COLUMN_ID_VENTA+"));";

    //Sintaxis SQL para crear la tabla de Clientes
    private static final String CREATE_TABLE_QUERY_CLIENTES =
            "CREATE TABLE " + TABLE_NAME_CLIENTES + " (" +
                    COLUMN_ID_CLIENTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE_CLIENTE + " TEXT , "+
                    COLUMN_CEDULA_CLIENTE + " TEXT, " +
                    COLUMN_TELEFONO_CLIENTE + " TEXT, " +
                    COLUMN_DIRECCION_CLIENTE + " TEXT NULL " + ")";

    //Sintaxis SQL para crear la tabla de facturas
    private static final String CREATE_TABLE_QUERY_FACTURAS =
            "CREATE TABLE " + TABLE_NAME_FACTURAS + " (" +
                    COLUMN_ID_FACTURA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_VENTA_ID_FACTURA+ " INTEGER, " +
                    COLUMN_CLIENTE_ID_FACTURA + " INTEGER, "+
                    COLUMN_VOC_FACTURA+ " TEXT, " +
                    COLUMN_NOMBRE_EMPRESA_FACTURA+ " TEXT, " +
                    COLUMN_NOMBRES_USUARIO_FACTURA+ " TEXT, " +
                    COLUMN_APELLIDOS_USUARIO_FACTURA+ " TEXT, " +
                    COLUMN_TIPO_CAFE_FACTURA+ " TEXT, " +
                    COLUMN_KILOS_TOTALES_FACTURA+ " TEXT, " +
                    COLUMN_VALOR_PAGO_FACTURA+ " TEXT, " +
                    COLUMN_FECHA_FACTURA+ " TEXT, " +
                    COLUMN_NIT_EMPRESA+ " TEXT, " +
                    COLUMN_NOMBRES_CLIENTE_FACTURA+ " TEXT, " +
                    COLUMN_CEDULA_CLIENTE_FACTURA+ " TEXT, " +
                    COLUMN_TELEFONO_CLIENTE_FACTURA+ " TEXT, " +
                    COLUMN_HORA_FACTURA+ " TEXT, " +
                    COLUMN_DEPARTAMENTO_EMPRESA_FACTURA+ " TEXT, " +
                    COLUMN_CIUDAD_EMPRESA_FACTURA+ " TEXT, " +
                    "FOREIGN KEY("+COLUMN_VENTA_ID_FACTURA+") REFERENCES "+TABLE_NAME_VENTAS+"("+COLUMN_ID_VENTA+"));" +
                    "FOREIGN KEY("+COLUMN_CLIENTE_ID_FACTURA+") REFERENCES "+TABLE_NAME_CLIENTES+"("+COLUMN_ID_CLIENTE+"));" ;

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY_EMPRESAS);
        db.execSQL(CREATE_TABLE_QUERY_USUARIOS);
        db.execSQL(CREATE_TABLE_QUERY_VENTAS);
        db.execSQL(CREATE_TABLE_QUERY_CAFE_SECO);
        db.execSQL(CREATE_TABLE_QUERY_CAFE_VERDE);
        db.execSQL(CREATE_TABLE_QUERY_CAFE_PASILLA);
        db.execSQL(CREATE_TABLE_QUERY_CLIENTES);
        db.execSQL(CREATE_TABLE_QUERY_FACTURAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_FACTURAS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_CAFE_PASILLA);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_CAFE_VERDE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_CAFE_SECO);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_QUERY_VENTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EMPRESAS);
        onCreate(db);
    }
}
