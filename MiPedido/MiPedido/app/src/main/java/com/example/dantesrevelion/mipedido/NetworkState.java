package com.example.dantesrevelion.mipedido;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConectionTask;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dantes Revelion on 18/07/2016.
 */
public class NetworkState extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
        Toast toastError = Toast.makeText(context.getApplicationContext(),
                "Error al actualizar", Toast.LENGTH_SHORT);
        if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED ) {
            Toast toast1 = Toast.makeText(context,
                    "Conectado", Toast.LENGTH_SHORT);
            toast1.show();
            if(ConnectionUtils.conectadoWifi(context)) {
                try {

                    ConnectionUtils.createConection(context);
                    CheckIn.checkInProcess(context);


                }catch (NullPointerException ex){
                    toastError.show();
                }

            }
        }else{
            System.out.println("desconectado");
            Toast toast1 = Toast.makeText(context,
                    "Desconectado", Toast.LENGTH_SHORT);
            toast1.show();
        }
    }

    private class CheckInternet extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();

            cn.connect(ConnectionUtils.iniciarSesion());
            response=cn.connect(ConnectionUtils.getAllUsuariosParameter());

            return response;
        }

    }


}
