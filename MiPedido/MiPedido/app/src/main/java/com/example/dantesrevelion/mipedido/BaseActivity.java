package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.Menu;

import com.example.dantesrevelion.mipedido.Utils.CheckIn;

/**
 * Created by Dantes Revelion on 13/07/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public static Activity activity;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);



    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        System.out.println("ON CREATE OPTION MENU");
        getMenuInflater().inflate(R.menu.mipedido_menu, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("ON OPTION ITEM SELECTED ");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart) {
            System.out.println("clicked cartttt");
            Intent intent=new Intent(BaseActivity.this,CarritoCompra.class);
            startActivity(intent);

            return true;
        }else if (id==R.id.config){
            System.out.println("Configuracion clicked");
            Intent intent=new Intent(BaseActivity.this,Configuracion.class);
            startActivity(intent);
        }else if(id==R.id.update){
            System.out.println("------------------update>");
            new callCheckIn().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    public void debug(String s){
        System.out.println(s);
    }

    public class callCheckIn extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            CheckIn.checkInRunnable(activity.getBaseContext(),activity);
            return true;
        }

    }
}
