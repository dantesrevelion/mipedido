package com.example.dantesrevelion.mipedido.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

/**
 * Created by Dantes Revelion on 08/07/2016.
 */
public class CheckIn {

    public static void checkInProcess(Context context){
        SQLiteHelper sqlHelper=new SQLiteHelper(context, "miPedidoLite", null, 1);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        JSONArray taskResult= ConnectionUtils.consultaSQLite(context,ConnectionUtils.queryCarritoUp());
        Toast toastError = Toast.makeText(context.getApplicationContext(),
                "Error al actualizar", Toast.LENGTH_SHORT);

        try {
            for(int i=0;i<taskResult.length();i++){
                String idp=taskResult.getJSONObject(i).getString("id_producto");
                String idv=taskResult.getJSONObject(i).getString("id_vendedor");
                String cantidad=taskResult.getJSONObject(i).getString("cantidad");
                String monto=taskResult.getJSONObject(i).getString("monto");
                String s=new InsertIntoVentas().execute(idp,idv,cantidad,monto).get().toString();
                ConnectionUtils.consultaSQLite(context,ConnectionUtils.updateEstadoVentatoS(taskResult.getJSONObject(i).getString("id_venta")));
            }



            Toast toast1 = Toast.makeText(context.getApplicationContext(),
                    "Actualizando base de datos", Toast.LENGTH_SHORT);
            toast1.show();
            String task=new ConectionTask().execute(db).get().toString();

            Toast toast2 = Toast.makeText(context.getApplicationContext(),
                    "Base de datos Actualizada", Toast.LENGTH_SHORT);
            toast2.show();
        } catch (InterruptedException e) {

            toastError.show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            toastError.show();
            e.printStackTrace();
        } catch (JSONException e) {
            toastError.show();
            e.printStackTrace();
        }
        System.out.println("check In End");
    }

    public static class InsertIntoVentas extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();

            //    response=cn.connect(ConnectionUtils.insertVentas(idp,idv,cantidad,monto));
            response=cn.connect(ConnectionUtils.insertVentas((String)param[0],(String)param[1],(String)param[2],(String)param[3]));

            return response;
        }

    }


}
