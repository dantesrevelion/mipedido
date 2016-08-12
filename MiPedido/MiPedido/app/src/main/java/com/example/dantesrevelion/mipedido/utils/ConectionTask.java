package com.example.dantesrevelion.mipedido.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Beans.BeanResponses;
import com.example.dantesrevelion.mipedido.R;

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
        Context c=(Context)objects[1];
        ConnectionUtils.showNotificacion(c);





        JSONArray response;
        ConnectionUtils cn=new ConnectionUtils();
        //obtiene tabla de usuarios
        response=cn.connect(ConnectionUtils.iniciarSesion());
        ConnectionUtils.setprogress(10,c);
        response = cn.connect(ConnectionUtils.getAllUsuariosParameter());
        responses.setResponseUsuarios(response);
        ConnectionUtils.setprogress(20,c);
        //obtiene tabla de ventas
        response = cn.connect(ConnectionUtils.getAllVentasParameter());
        responses.setResponseVenta(response);
        ConnectionUtils.setprogress(30,c);
        //obtiene tabla de productos
        response = cn.connect(ConnectionUtils.getAllProdParameter());
        responses.setResponseProductos(response);
        ConnectionUtils.setprogress(40,c);

        response = cn.connect(ConnectionUtils.getAllGastos());
        responses.setResponseGastos(response);


        try {
            SQLiteDatabase db = (SQLiteDatabase) objects[0];
            db.execSQL("delete from usuarios");
            db.execSQL("VACUUM");
            JSONArray usuarios = responses.getResponseUsuarios();

            for (int i = 0; i < usuarios.length(); i++) {
                try {
                    JSONObject obj = usuarios.getJSONObject(i);
                    db.execSQL("INSERT INTO usuarios (id, usuario, password,correo) " +
                            "VALUES (" + obj.get("id") + ", '" + obj.get("usuario") + "', '" + obj.get("password") + "','" + obj.get("correo") + "' )");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            ConnectionUtils.setprogress(50,c);

            db.execSQL("delete from productos");
            db.execSQL("VACUUM");
            JSONArray productos = responses.getResponseProductos();


            for (int i = 0; i < productos.length(); i++) {
                try {
                    JSONObject obj = productos.getJSONObject(i);
                    db.execSQL("INSERT INTO productos (id, nombre, denominacion,costo,marca,img) " +
                            "VALUES (" + obj.get("id") + ", '" + obj.get("nombre") + "', '" + obj.get("denominacion") + "'," + obj.get("costo") + ",'" +
                            obj.get("marca") + "','" + obj.get("img") + "')");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            ConnectionUtils.setprogress(60,c);
            db.execSQL("delete from ventas");
            db.execSQL("VACUUM");
            JSONArray ventas = responses.getResponseVenta();

            for (int i = 0; i < ventas.length(); i++) {
                try {
                    JSONObject obj = ventas.getJSONObject(i);
                    db.execSQL("INSERT INTO ventas (id_venta, id_producto, id_vendedor,fecha,cantidad,monto) " +
                            "VALUES (" + obj.get("id_venta") + ", " + obj.get("id_producto") + ", " + obj.get("id_vendedor") + ",'" + obj.get("fecha")
                            + "'," + obj.get("cantidad") + "," + obj.get("monto") + " )");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            db.execSQL("delete from gastos");
            db.execSQL("VACUUM");
            JSONArray gastos = responses.getResponseGastos();

            for (int i = 0; i < gastos.length(); i++) {
                try {
                    JSONObject obj = gastos.getJSONObject(i);
                    db.execSQL("INSERT INTO `gastos` (`id`, `idvendedor`, `nombre`, `codigo`, `monto`, `fecha`) " +
                            "VALUES ("+obj.get("id")+", '"+obj.get("idvendedor")+"', '"+obj.get("nombre")+"', '"+obj.get("codigo")+"', '"+obj.get("monto")+"', "+obj.get("fecha")+");");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            ConnectionUtils.setprogress(70,c);
            ConnectionUtils.notificateOK(c);
            cn.connect(ConnectionUtils.cerrarSesion());





            ConnectionUtils.setprogress(80,c);
            db.close();
        }catch (NullPointerException ex){
            ConnectionUtils.notificateError(c);
            return "Error de configuracion";
        }

        return  "Base de datos Actualizada";
    }
}

