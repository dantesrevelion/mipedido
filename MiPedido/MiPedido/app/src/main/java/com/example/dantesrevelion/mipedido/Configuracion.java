package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

public class Configuracion extends PreferenceActivity {

    public static Activity activity=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configuracion);
        activity=this;

    }


    @Override
    protected void onDestroy() {
        System.out.println("close config");
        ConnectionUtils.createConection(getBaseContext());
        if(ConnectionUtils.conectadoWifi(this) || ConnectionUtils.conectadoRedMovil(this)) {
           // CheckIn.checkInProcess(this.getBaseContext(),this);
           // new callCheckIn().execute();
        }
        super.onDestroy();
    }

    public class callCheckIn extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            CheckIn.checkInRunnable(getBaseContext(),activity);
            return true;
        }

    }



}
