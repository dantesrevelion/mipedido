package com.example.dantesrevelion.mipedido.Utils;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.dantesrevelion.mipedido.Beans.BeanResponses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dantes Revelion on 07/07/2016.
 */
public class ConectionTask extends AsyncTask{
    BeanResponses responses=new BeanResponses();

    @Override
    protected Object doInBackground(Object[] objects) {

        JSONArray response;
        ConnectionUtils cn=new ConnectionUtils();
        //obtiene tabla de usuarios
        response = cn.connect(ConnectionUtils.getAllUsuariosParameter());
        responses.setResponseUsuarios(response);
        //obtiene tabla de ventas
        response = cn.connect(ConnectionUtils.getAllVentasParameter());
        responses.setResponseVenta(response);
        //obtiene tabla de productos
        response = cn.connect(ConnectionUtils.getAllProdParameter());
        responses.setResponseProductos(response);

        SQLiteDatabase db= (SQLiteDatabase) objects[0];
        db.execSQL("delete from usuarios");
        db.execSQL("VACUUM");
        JSONArray usuarios=responses.getResponseUsuarios();
        for (int i=0;i<usuarios.length();i++){
            try {
                JSONObject obj=usuarios.getJSONObject(i);
                db.execSQL("INSERT INTO usuarios (id, usuario, password,correo) " +
                        "VALUES ("+obj.get("id")+", '" +obj.get("usuario")+ "', '" +obj.get("password")+ "','"+obj.get("correo")+"' )");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        db.execSQL("delete from productos");
        db.execSQL("VACUUM");
        JSONArray productos=responses.getResponseProductos();
        for (int i=0;i<productos.length();i++){
            try {
                JSONObject obj=productos.getJSONObject(i);
                db.execSQL("INSERT INTO productos (id, nombre, denominacion,costo,marca,img) " +
                        "VALUES ("+obj.get("id")+", '" +obj.get("nombre")+ "', '" +obj.get("denominacion")+ "',"+obj.get("costo")+",'"+
                        obj.get("marca")+"','"+ obj.get("img")+"')");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        db.execSQL("delete from ventas");
        db.execSQL("VACUUM");
        JSONArray ventas=responses.getResponseVenta();
        for (int i=0;i<ventas.length();i++){
            try {
                JSONObject obj=ventas.getJSONObject(i);
                db.execSQL("INSERT INTO ventas (id_venta, id_producto, id_vendedor,fecha,cantidad,monto) " +
                        "VALUES ("+obj.get("id_venta")+", " +obj.get("id_producto")+ ", " +obj.get("id_vendedor")+ ",'"+obj.get("fecha")
                        +"',"+obj.get("cantidad")+","+obj.get("monto")+" )");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        db.close();

        return "OK";
    }
}

