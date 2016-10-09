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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.VolleyS;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Dantes Revelion on 13/07/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public static  VolleyS volley;
    public static RequestQueue fRequestQueue;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();


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

          //  System.out.println("------------------update>");
          //  new callCheckIn().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            onPreStartConnection();
            fRequestQueue.add(request);
        }
    }

    public void onPreStartConnection() {
       this.setProgressBarIndeterminateVisibility(true);
    }

    public void onConnectionFinished() {
        this.setProgressBarIndeterminateVisibility(false);
    }

    public void onConnectionFailed(VolleyError error) {
        this.setProgressBarIndeterminateVisibility(false);
        debug("ERROR VOLLEY----->"+error);
        debug("CODE "+error.getCause());
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
    }

    public void makeRequest(){
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=39.476245,-0.349448&sensor=true";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
              //  label.setText(jsonArray.toString());
                debug("VOLLEY RESPONSE ARRAY--------------->"+jsonArray.toString());
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
            }


        });
        addToQueue(request);
    }
    public void makeRequest2(){
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=39.476245,-0.349448&sensor=true";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        debug("VOLLEY RESPONSE object--------------->"+response.toString());
                        onConnectionFinished();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        addToQueue(jsObjRequest);
    }
    public void debug(String s){
        System.out.println(s);
    }


}
