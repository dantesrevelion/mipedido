package com.example.dantesrevelion.mipedido.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dantes Revelion on 07/07/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `productos` (" +
                "  `id` int(11) NOT NULL," +
                "  `nombre` varchar(50) NOT NULL," +
                "  `denominacion` varchar(30) NOT NULL," +
                "  `costo` varchar(30) NOT NULL," +
                "  `marca` varchar(30) NOT NULL," +
                "  `img` varchar(200) NOT NULL" +
                ");");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `usuarios` (" +
                "  `id` int(11) NOT NULL," +
                "  `usuario` varchar(30) NOT NULL," +
                "  `password` varchar(30) NOT NULL," +
                "  `correo` varchar(30) NOT NULL" +
                ");");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `ventas` (" +
                "  `id_venta` int(11) NOT NULL," +
                "  `id_producto` int(11) NOT NULL," +
                "  `id_vendedor` int(11) NOT NULL," +
                "  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "  `cantidad` int(11) NOT NULL," +
                "  `monto` double NOT NULL" +
                ");");

        System.out.println("SQLite CREATED-");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
