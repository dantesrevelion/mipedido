package com.example.dantesrevelion.mipedido.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.NetworkState;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dantes Revelion on 08/07/2016.
 */
public class CheckIn {
    public static ConnectionUtils cn=null;
    public static Context contextG=null;

    public static void checkInRunnable(final Context context, Activity activity){

        System.out.println("CHECK IN PROCESS------------->");
        SQLiteHelper sqlHelper=new SQLiteHelper(context, "miPedidoLite", null, 1);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        JSONArray taskResult= ConnectionUtils.consultaSQLite(context,ConnectionUtils.queryCarritoUp());
        JSONArray taskResultGastos= ConnectionUtils.consultaSQLite(context,ConnectionUtils.queryGastosUp());
        cn=new ConnectionUtils();
        contextG=context;



        try {
            for(int i=0;i<taskResult.length();i++){
                String idp=taskResult.getJSONObject(i).getString("id_producto");
                String idv=taskResult.getJSONObject(i).getString("id_vendedor");
                String cantidad=taskResult.getJSONObject(i).getString("cantidad");
                String monto=taskResult.getJSONObject(i).getString("monto");
                String fecha=taskResult.getJSONObject(i).getString("fecha");
                String idventa=taskResult.getJSONObject(i).getString("id_venta");
                new InsertIntoVentas().execute(idp,idv,cantidad,monto,fecha,idventa);

            }
            for(int i=0;i<taskResultGastos.length();i++){
                String idv=taskResultGastos.getJSONObject(i).getString("idvendedor");
                String nombre=taskResultGastos.getJSONObject(i).getString("nombre");
                String codigo=taskResultGastos.getJSONObject(i).getString("codigo");
                String monto=taskResultGastos.getJSONObject(i).getString("monto");
                String fecha=taskResultGastos.getJSONObject(i).getString("fecha");
                String id=taskResultGastos.getJSONObject(i).getString("id");
                new InsertIntoGastos().execute(idv,nombre,codigo,monto,fecha,id);

            }




         //   Toast toast1 = Toast.makeText(context.getApplicationContext(),
          //          "Actualizando base de datos", Toast.LENGTH_SHORT);
          //  toast1.show();


            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context.getApplicationContext(),
                            "Actualizando base de datos", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
          //      String task=new ConectionTask().execute(db,activity).get().toString();
            new ConectionTask().execute(db,context,activity);


            //Toast toast2 = Toast.makeText(activity.getApplicationContext(), task, Toast.LENGTH_SHORT);
            //toast2.show();
        } catch (JSONException e) {
        //    toastError.show();
            e.printStackTrace();
        }
        System.out.println("check In End");

    }



    public static class InsertIntoVentas extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;


            //    response=cn.connect(ConnectionUtils.insertVentas(idp,idv,cantidad,monto));
            cn.connect(ConnectionUtils.iniciarSesion());
            response=cn.connect(ConnectionUtils.insertVentas((String)param[0],(String)param[1],(String)param[2],(String)param[3],(String)param[4]));
            cn.connect(ConnectionUtils.cerrarSesion());
            System.out.println("---------------<<<>RESPONSE "+response);
            try {
                if("ok".equals(response.getJSONObject(0).get("a"))) {
                    ConnectionUtils.consultaSQLite(contextG,ConnectionUtils.updateEstadoVentatoS((String)param[5]));
                }else{
                    ConnectionUtils.notificateError(contextG);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

    }
    public static class InsertIntoGastos extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
         //   ConnectionUtils cn=new ConnectionUtils();

            cn.connect(ConnectionUtils.iniciarSesion());
            response=cn.connect(ConnectionUtils.insertGastosWEB((String)param[0],(String)param[1],(String)param[2],(String)param[3],(String)param[4]));
            cn.connect(ConnectionUtils.cerrarSesion());

            System.out.println("---------------<<<>RESPONSE "+response);
            try {
                if("ok".equals(response.getJSONObject(0).get("a"))) {
                    ConnectionUtils.consultaSQLite(contextG,ConnectionUtils.updateEstadoGastosS((String)param[5]));
                }else{
                    ConnectionUtils.notificateError(contextG);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

    }



}
