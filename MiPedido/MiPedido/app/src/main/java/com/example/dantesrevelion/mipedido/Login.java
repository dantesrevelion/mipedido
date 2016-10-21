package com.example.dantesrevelion.mipedido;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConectionTask;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.MyAnimationUtils;
import com.example.dantesrevelion.mipedido.Utils.SQLiteHelper;
import com.example.dantesrevelion.mipedido.Utils.VolleyS;


import org.json.JSONArray;
import org.json.JSONException;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.ExecutionException;

public class Login extends BaseActivity {

    LinearLayout layoutUser;
    LinearLayout layoutPass;
    EditText inputTextUser=null;
    EditText inputTextPsw=null;
    NetworkState receiver=null;
    boolean wasConfigOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkState(this);
        registerReceiver(receiver, filter);
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();

        /*
        if(ConnectionUtils.conectadoWifi(this) || ConnectionUtils.conectadoRedMovil(this)) {
            CheckIn.checkInProcess(this);

        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Modo Offline", Toast.LENGTH_SHORT);
            toast1.show();
        }
        */
        layoutUser = (LinearLayout) findViewById(R.id.layoutUser);
        layoutPass = (LinearLayout) findViewById(R.id.layoutPassword);
        inputTextUser = (EditText) findViewById(R.id.inputUser);
        inputTextPsw = (EditText) findViewById(R.id.inputPassword);
        layoutPass.setVisibility(View.INVISIBLE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String u=preferences.getString("UsuarioLogin","");
        String p=preferences.getString("PasswordLogin","");
        System.out.println(u+"-----login-----"+p);
        if(!"".equals(u) && !"".equals(p)) {
            inputTextUser.setText(u);
            inputTextPsw.setText(p);
        }
       // ConnectionUtils.consultaSQLite(this,"select * from usuarios");
        MyAnimationUtils.translateAnimation(layoutUser, 800L, 2.0f, 1000, 0, 0, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasConfigOpen){
            new Connection().execute();
            wasConfigOpen=false;
        }
    }

    public void nextStep(View view){

        String entry=inputTextUser.getText().toString();
        //  JSONArray taskResult= (JSONArray) new Connection().execute(entry).get();
        JSONArray taskResult=ConnectionUtils.consultaSQLite(this,ConnectionUtils.querySearchUsuario(entry));

        if(taskResult.length()!=0) {
            MyAnimationUtils.translateAnimation(layoutUser,800L,2.0f,0,-900,0,0);
            layoutUser.setVisibility(View.INVISIBLE);
            layoutPass.setVisibility(View.VISIBLE);
            MyAnimationUtils.translateAnimation(layoutPass,800L,2.0f,900,0,0,0);
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                            "El usuario no existe", Toast.LENGTH_SHORT);
            toast1.show();
        }


    }
    public void loadMenu(View view){
        try {
            String entryUser=inputTextUser.getText().toString();
            String entryPass=inputTextPsw.getText().toString();
            System.out.println(entryUser+"---"+entryPass);
            JSONArray taskResult= ConnectionUtils.consultaSQLite(this,ConnectionUtils.queryIdentifyUsuario(entryUser,entryPass));
            System.out.println("JSON "+taskResult);
            if(taskResult.length()!=0) {
                Intent intent=new Intent(Login.this,Menu.class);
                intent.putExtra("id",taskResult.getJSONObject(0).get("id").toString());
                intent.putExtra("usuario",taskResult.getJSONObject(0).get("usuario").toString());
                intent.putExtra("correo",taskResult.getJSONObject(0).get("correo").toString());
                rememberUser(entryUser,entryPass);
                startActivity(intent);
            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Contraseña Incorrecta", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void loadUser(View view){
        MyAnimationUtils.translateAnimation(layoutPass,800L,2.0f,0,900,0,0);
        layoutUser.setVisibility(View.VISIBLE);
        layoutPass.setVisibility(View.INVISIBLE);
        MyAnimationUtils.translateAnimation(layoutUser,800L,2.0f,-900,0,0,0);
    }
    public void openConfig(View v){
        Intent intent=new Intent(Login.this,Configuracion.class);
        startActivity(intent);
        wasConfigOpen=true;
    }

    public void rememberUser(String user,String pass){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UsuarioLogin",user);
        editor.putString("PasswordLogin",pass);
        editor.commit();
    }



    /*
    private class ConnectionUserPass extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();
            if(param!=null){
                if(!param[0].toString().trim().equals("")){
                    if(!param[1].toString().trim().equals("")) {
                        response = cn.connect(ConnectionUtils.getIdentUserPassParameter
                                (param[0].toString(),param[1].toString()));
                    }
                }
            }
            return response;
        }

    }
    */

}
