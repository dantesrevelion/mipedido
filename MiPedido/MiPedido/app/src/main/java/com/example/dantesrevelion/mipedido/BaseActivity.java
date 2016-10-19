package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.SQLiteHelper;
import com.example.dantesrevelion.mipedido.Utils.VolleyS;
import com.example.dantesrevelion.mipedido.orm.DatosGastos;
import com.example.dantesrevelion.mipedido.orm.DatosVenta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dantes Revelion on 13/07/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public static  VolleyS volley;
    public static RequestQueue fRequestQueue;
    List<DatosGastos> listaGastos;
    List<DatosVenta> listaVentas;


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

    public void makeRequestFirst(JsonArrayRequest request){
    //    String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=39.476245,-0.349448&sensor=true";
        /*
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
        */
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

    public void makeRequest(String url){




        final JsonArrayRequest requestClose=new JsonArrayRequest(ConnectionUtils.cerrarSesion(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                //  label.setText(jsonArray.toString());
                debug("------------->Cerrar sesion"+jsonArray.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
            }


        });
        /**Todo SE HACEN LAS PETICIONES EN LISTA*/
        final JsonArrayRequest requestData=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                //  label.setText(jsonArray.toString());
                debug("------------->Response"+jsonArray.toString());
                fRequestQueue.add(requestClose);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
            }


        });
        final JsonArrayRequest requestOpen=new JsonArrayRequest(ConnectionUtils.iniciarSesion(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                //  label.setText(jsonArray.toString());
                debug("------------->Iniciar sesion"+jsonArray.toString());
                fRequestQueue.add(requestData);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
            }


        });



        fRequestQueue.add(requestOpen);
    }


    public void makePostVentasRequest(String url,JSONArray params){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               // mTextView.setText(response.toString());
                System.out.println(" -------------->POST RESPONSE"+response);
                try {
                    if("ok".equals(response.getJSONObject(0).get("a"))){
                        for(DatosVenta item: listaVentas){
                            ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.updateEstadoVentatoS(item.getId()));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  mTextView.setText(error.toString());
                System.out.println("------------->POST ERROR "+error.toString());
            }
        }) ;
        fRequestQueue.add(jsonArrayRequest);
    }
    public void makePostGastosRequest(String url,JSONArray params){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // mTextView.setText(response.toString());
                System.out.println(" -------------->POST RESPONSE"+response);
                try {
                    if("ok".equals(response.getJSONObject(0).get("a"))){
                        for(DatosGastos item: listaGastos){
                            ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.updateEstadoGastosS(item.getId()));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  mTextView.setText(error.toString());
                System.out.println("------------->POST ERROR "+error.toString());
            }
        }) ;
        fRequestQueue.add(jsonArrayRequest);
    }


    public void debug(String s){
        System.out.println(s);
    }

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
            listaGastos=new ArrayList<>();
            listaVentas=new ArrayList<>();
            ConnectionUtils.createConection(getBaseContext());

            System.out.println("----------->LLENA REQUEST VENTAS");
            for (int i = 0; i < taskResult.length(); i++) {
                String idp = taskResult.getJSONObject(i).getString("id_producto");
                String idv = taskResult.getJSONObject(i).getString("id_vendedor");
                String cantidad = taskResult.getJSONObject(i).getString("cantidad");
                String monto = taskResult.getJSONObject(i).getString("monto");
                String fecha = taskResult.getJSONObject(i).getString("fecha");
                String idventa = taskResult.getJSONObject(i).getString("id_venta");
                DatosVenta ventas=new DatosVenta();
                ventas.setSession(ConnectionUtils.getSession());
                ventas.setMonto(monto);
                ventas.setCantidad(cantidad);
                ventas.setFecha(fecha);
                ventas.setIdp(idp);
                ventas.setIdv(idv);
                ventas.setId(idventa);
                listaVentas.add(ventas);
                //   new CheckIn.InsertIntoVentas().execute(idp, idv, cantidad, monto, fecha, idventa);
                //     makeRequest(ConnectionUtils.insertVentas(idp,idv,cantidad,monto,fecha));

            }
            System.out.println("----------->LLENA REQUEST GASTOS");
            for (int i = 0; i < taskResultGastos.length(); i++) {
                String idv = taskResultGastos.getJSONObject(i).getString("idvendedor");
                String nombre = taskResultGastos.getJSONObject(i).getString("nombre");
                String codigo = taskResultGastos.getJSONObject(i).getString("codigo");
                String monto = taskResultGastos.getJSONObject(i).getString("monto");
                String fecha = taskResultGastos.getJSONObject(i).getString("fecha");
                String id = taskResultGastos.getJSONObject(i).getString("id");
                DatosGastos gastos=new DatosGastos();
                gastos.setCodigo(codigo);
                gastos.setIdv(idv);
                gastos.setMonto(monto);
                gastos.setNombre(nombre);
                gastos.setParamFecha(fecha);
                gastos.setId(id);
                gastos.setSession(ConnectionUtils.getSession());
                listaGastos.add(gastos);

                //   new CheckIn.InsertIntoGastos().execute(idv, nombre, codigo, monto, fecha, id);

            }

            System.out.println("----------->INICIA PETICIONES");
            if(listaVentas.size()>0){
                JSONArray requestVentas=ConnectionUtils.parseBeantoJsonArray(listaVentas);
                makePostVentasRequest(ConnectionUtils.insertVentasPost(),requestVentas);
            }
            if(listaGastos.size()>0){
                JSONArray requestGastos=ConnectionUtils.parseBeantoJsonArray(listaGastos);
                makePostGastosRequest(ConnectionUtils.insertGastosPost(),requestGastos);
            }

            System.out.println("----------->INICIA DESCARGA");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
