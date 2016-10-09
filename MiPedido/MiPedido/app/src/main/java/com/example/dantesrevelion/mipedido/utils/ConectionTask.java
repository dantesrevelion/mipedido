package com.example.dantesrevelion.mipedido.Utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Beans.BeanResponses;
import com.example.dantesrevelion.mipedido.NetworkState;
import com.example.dantesrevelion.mipedido.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dantes Revelion on 07/07/2016.
 */
public class ConectionTask extends AsyncTask{
    BeanResponses responses=new BeanResponses();
    boolean tablaProductos=false,tablaVentas=false,tablaUsuarios=false,tablaGastos=false;

    @Override
    protected Object doInBackground(Object[] objects) {
        Context c=(Context)objects[1];
        Activity activity=(Activity)objects[2] ;
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
            tablaUsuarios=searchError(responses.getResponseUsuarios());
            if(!tablaUsuarios) {

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
                ConnectionUtils.setprogress(50, c);
            }
            tablaProductos=searchError(responses.getResponseProductos());
            if(!tablaProductos) {
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
                ConnectionUtils.setprogress(60, c);
            }
            tablaVentas=searchError(responses.getResponseVenta());
            if(!tablaVentas) {
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
            }
            tablaGastos=searchError(responses.getResponseGastos());
            if(!tablaGastos) {
                db.execSQL("delete from gastos");
                db.execSQL("VACUUM");
                JSONArray gastos = responses.getResponseGastos();

                for (int i = 0; i < gastos.length(); i++) {
                    try {
                        JSONObject obj = gastos.getJSONObject(i);
                        db.execSQL("INSERT INTO `gastos` (`id`, `idvendedor`, `nombre`, `codigo`, `monto`, `fecha`,`estatus`) " +
                                "VALUES (" + obj.get("id") + ", " + obj.get("idvendedor") + ", '" + obj.get("nombre") + "', '" + obj.get("codigo") + "', " + obj.get("monto") + ", '" + obj.get("fecha") + "','S')");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ConnectionUtils.setprogress(70, c);
                ConnectionUtils.notificateOK(c);
            }
            cn.connect(ConnectionUtils.cerrarSesion());





            ConnectionUtils.setprogress(80,c);
            db.close();
            NetworkState ns= new NetworkState(activity);
            if(checkUpdate()){
                return  "Base de datos Actualizada";
            }else{
                ConnectionUtils.notificateIncompleta(c);
                return  "Actualización Incompleta";
            }

        }catch (NullPointerException ex){
            ConnectionUtils.notificateError(c);
            NetworkState ns= new NetworkState(activity);
            return "Error de configuracion";
        }

       // NetworkState.cin=new NetworkState.callCheckIn();


    }

    private boolean searchError(JSONArray array){
        boolean response=false;
        for(int i=0;i<array.length();i++){
            try {

                if(array.getJSONObject(i).get("a").equals("Error")){
                    response=true;
                }else {
                    response=false;
                }

            } catch (JSONException e) {
                response=false;
            }

        }
       return response;
    }
    private boolean checkUpdate(){
        if(!tablaGastos && !tablaVentas && !tablaProductos && !tablaUsuarios){

            return true;
        }else{
            return false;
        }
    }
}

