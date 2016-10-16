package com.example.dantesrevelion.mipedido.Utils;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.example.dantesrevelion.mipedido.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Dantes Revelion on 16/10/2016.
 */

public class UpdateDataBase extends BaseActivity{


    public void runUpdate(){
        /**TODO INICIA RUN UPDATE*/
        System.out.println("----------->INICIA RUN UPDATE");
        System.out.println("----------->OPTIENE BASE DE DATOS");
        SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        System.out.println("----------->OPTIENE DATOS A SUBIR");
        JSONArray taskResult= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryCarritoUp());
        JSONArray taskResultGastos= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryGastosUp());
        System.out.println("----------->SUBE LOS DATOS");
        try {
            for (int i = 0; i < taskResult.length(); i++) {
                String idp = taskResult.getJSONObject(i).getString("id_producto");
                String idv = taskResult.getJSONObject(i).getString("id_vendedor");
                String cantidad = taskResult.getJSONObject(i).getString("cantidad");
                String monto = taskResult.getJSONObject(i).getString("monto");
                String fecha = taskResult.getJSONObject(i).getString("fecha");
                String idventa = taskResult.getJSONObject(i).getString("id_venta");
           //   new CheckIn.InsertIntoVentas().execute(idp, idv, cantidad, monto, fecha, idventa);
                makeRequest(ConnectionUtils.insertVentas(idp,idv,cantidad,monto,fecha));


            }
            for (int i = 0; i < taskResultGastos.length(); i++) {
                String idv = taskResultGastos.getJSONObject(i).getString("idvendedor");
                String nombre = taskResultGastos.getJSONObject(i).getString("nombre");
                String codigo = taskResultGastos.getJSONObject(i).getString("codigo");
                String monto = taskResultGastos.getJSONObject(i).getString("monto");
                String fecha = taskResultGastos.getJSONObject(i).getString("fecha");
                String id = taskResultGastos.getJSONObject(i).getString("id");
                new CheckIn.InsertIntoGastos().execute(idv, nombre, codigo, monto, fecha, id);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
