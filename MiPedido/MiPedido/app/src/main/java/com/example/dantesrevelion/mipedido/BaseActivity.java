package com.example.dantesrevelion.mipedido;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;
import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.SQLiteHelper;
import com.example.dantesrevelion.mipedido.Utils.VolleyS;
import com.example.dantesrevelion.mipedido.orm.DatosGastos;
import com.example.dantesrevelion.mipedido.orm.DatosVenta;
import com.example.dantesrevelion.mipedido.orm.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dantes Revelion on 13/07/2016.
 */
public class BaseActivity extends AppCompatActivity {
    /**TODO SI YO SOY EL CORRECTO GILIPOLLAS PEDRITO*/
    public Toolbar toolbar;
    public static  VolleyS volley;
    public static RequestQueue fRequestQueue;
    List<DatosGastos> listaGastos;
    List<DatosVenta> listaVentas;
    MenuItem btnUpdate;

    LocationManager mlocManager;
    MyLocationListener mlocListener;
    public static Location currentLocation;
    public static String idUsuarioBase;
    boolean sendVentas=false,sendGastos=false;

    Handler handlerUpdate ;
    Runnable runStart;

    Handler handlerLocation ;
    Runnable runLocation;
    int contadorUpdate;

    public static BluetoothUtils utils;



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();

        /*
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        requestUpdate();
        */

    }

    public boolean isGPSEnabled(){
        return mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public void requestUpdate() {
        handlerLocation = new Handler();
        contadorUpdate=0;
        runLocation = new Runnable() {
            public void run() {
                if (mlocListener != null && mlocManager != null) {
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("resquest");
                    }

                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_LOW);
                    criteria.setBearingAccuracy(Criteria.ACCURACY_LOW);

                    String provider = mlocManager.getBestProvider(criteria, true);

                    //mlocManager.requestLocationUpdates(provider, 0, 0, (LocationListener) mlocListener);
                    mlocManager.requestLocationUpdates(provider, 0, 0, (LocationListener) mlocListener);
                    //mlocManager.requestSingleUpdate(criteria,mlocListener,lopper);
                    // System.out.println(" LAT LONG " + MyLocationListener.getLocation());

                    //if (MyLocationListener.getLocation() != null) {


                    currentLocation = mlocManager.getLastKnownLocation(provider);
                    if(currentLocation!=null){
                        debug("Latitud------>" + currentLocation.getLatitude());
                        debug("Longitud----->" + currentLocation.getLongitude());
                        debug("Accuracy----->" + currentLocation.getAccuracy());
                    }



                    //            }
                }
                if(contadorUpdate<3){
                    contadorUpdate++;
                    handlerLocation.postDelayed(runLocation, 800);
                }

            }
        };

        handlerLocation.postDelayed(runLocation, 200);
    }
    public void stopUpdate(){
        if (mlocListener!=null && mlocManager!=null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mlocManager.removeUpdates(mlocListener);
        }
    }

    public void startUpdates( final float accuracy,final int timeupdate,final RelativeLayout layoutLoading){


        requestUpdate();
        handlerUpdate = new Handler();



        runStart = new Runnable() {
            public void run() {

                if(currentLocation!=null && currentLocation.getAccuracy()<accuracy){
                    System.out.println("**** ACCURACY OK ****"+currentLocation.getAccuracy());
                    stopUpdate();
                    if(layoutLoading!=null){
                        layoutLoading.setVisibility(View.GONE);
                    }
                }else{
                    requestUpdate();
                    handlerUpdate.postDelayed(runStart, timeupdate);

                }

            }
        };

        handlerUpdate.postDelayed(runStart, 2200);
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
            if("10".equals(idUsuarioBase)) {
                Intent intent = new Intent(BaseActivity.this, Configuracion.class);
                startActivity(intent);
            }
        }else if(id==R.id.update){
            btnUpdate=item;
            disableBtnUpdate();
            new Connection().execute();


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
    public void showToast(String text) {
        try {
            Toast.makeText(Login.context, text, Toast.LENGTH_SHORT).show();
        }catch (Exception ex){

        }
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
                debug("------------->Response "+jsonArray.toString());
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
                System.out.println(" -------------->ACTUALIZA VENTAS");
                SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
                SQLiteDatabase db = sqlHelper.getWritableDatabase();
                try {
                    if("ok".equals(response.getJSONObject(0).get("a"))) {
                        db.beginTransaction();
                        try {
                            //makeAllInserts();
                            for (int i = 0; i < listaVentas.size(); i++) {
                                ContentValues values = new ContentValues();
                                values.put("estatus", "S");
                                db.update("carrito", values, "id_venta=" + listaVentas.get(i).getId(), null);
                            }
                            db.setTransactionSuccessful();
                            System.out.println("-----SQLite Trasn Succesful ");
                        } catch (Exception ex) {
                            System.out.println("-----SQLite Trasn Ex " + ex);
                        } finally {
                            db.endTransaction();
                            sendVentas=true;
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,
                0,
                1f));
        fRequestQueue.add(jsonArrayRequest);
    }

    public void makePostVentasUsuario(String url, final JSONArray params){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray ventas) {

                SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
                SQLiteDatabase db = sqlHelper.getWritableDatabase();

                db.execSQL("delete from ventas");
                db.execSQL("VACUUM");
                //JSONArray ventas = jsonArray;
                JSONArray ventasLocal= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryCarritoUp());
                db.beginTransaction();
                try {
                    //makeAllInserts();
                    for(int i = 0; i < ventas.length(); i++) {
                        //  myDataBase = db.openDatabase();
                        JSONObject obj = ventas.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.put("id_venta", obj.get("id_venta").toString());
                        values.put("id_producto", obj.get("id_producto").toString());
                        values.put("id_vendedor", obj.get("id_vendedor").toString());
                        values.put("fecha", obj.get("fecha").toString());
                        values.put("cantidad", obj.get("cantidad").toString());
                        values.put("monto", obj.get("monto").toString());


                        db.insert("ventas", "monto", values);

                    }
                    db.setTransactionSuccessful();
                    System.out.println("-----SQLite Trasn Succesful ");

                    // anade a la lista de venta las ventas que estan pendientes por subir

                    for(int i = 0; i < ventasLocal.length(); i++) {
                        //  myDataBase = db.openDatabase();
                        JSONObject obj = ventasLocal.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.put("id_venta", obj.get("id_venta").toString());
                        values.put("id_producto", obj.get("id_producto").toString());
                        values.put("id_vendedor", obj.get("id_vendedor").toString());
                        values.put("fecha", obj.get("fecha").toString());
                        values.put("cantidad", obj.get("cantidad").toString());
                        values.put("monto", obj.get("monto").toString());


                        db.insert("ventas", "monto", values);

                    }
                }catch (Exception ex){
                    System.out.println("-----SQLite Trasn Ex "+ex);
                }finally {
                    db.endTransaction();
                }

                db.close();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  mTextView.setText(error.toString());
                System.out.println("------------->POST ERROR "+error.toString());
                enableBtnUpdate();
            }
        }) ;
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,
                0,
                1f));
        fRequestQueue.add(jsonArrayRequest);
    }
    public void makepostGastosUsuario(String url,JSONArray params){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {


                SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
                SQLiteDatabase db = sqlHelper.getWritableDatabase();
                db.execSQL("delete from gastos");
                db.execSQL("VACUUM");
                JSONArray gastos = jsonArray;

                db.beginTransaction();
                try {
                    //makeAllInserts();
                    for(int i = 0; i < gastos.length(); i++) {
                        //  myDataBase = db.openDatabase();
                        JSONObject obj = gastos.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.put("id", obj.get("id").toString());
                        values.put("idvendedor", obj.get("idvendedor").toString());
                        values.put("nombre", obj.get("nombre").toString());
                        values.put("codigo", obj.get("codigo").toString());
                        values.put("monto", obj.get("monto").toString());
                        values.put("fecha", obj.get("fecha").toString());
                        values.put("estatus", "S");


                        db.insert("gastos", "monto", values);

                    }
                    db.setTransactionSuccessful();
                    System.out.println("-----SQLite Trasn Succesful ");
                }catch (Exception ex){
                    System.out.println("-----SQLite Trasn Ex "+ex);
                }finally {
                    db.endTransaction();
                    enableBtnUpdate();
                }
                db.close();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  mTextView.setText(error.toString());
                System.out.println("------------->POST ERROR "+error.toString());
                enableBtnUpdate();
            }
        }) ;
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,
                0,
                1f));
        fRequestQueue.add(jsonArrayRequest);
    }
    public void makePostLocation(String url,JSONArray params){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


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
    public void disableBtnUpdate(){
        btnUpdate.setEnabled(false);
        Drawable resIcon = getResources().getDrawable(R.drawable.update);
        //resIcon.mutate().setColorFilter(Color.argb(80,45,45,45), PorterDuff.Mode.SRC_IN);
        resIcon.setAlpha(80);
        btnUpdate.setIcon(resIcon);
    }
    public void enableBtnUpdate(){
        if(btnUpdate!=null) {
            btnUpdate.setEnabled(true);
            Drawable resIcon = getResources().getDrawable(R.drawable.update);
            resIcon.setAlpha(255);
            btnUpdate.setIcon(resIcon);
        }
    }
    public void actualizaVentasGastos(String idusr){
        List<Usuario> listaUsuario=new ArrayList<>();
        Usuario usr=new Usuario();
        usr.setIdUsuario(idusr);
        usr.setSession(ConnectionUtils.getSession());
        usr.setSession(ConnectionUtils.getSession());
        listaUsuario.add(usr);

        JSONArray requestUsr=ConnectionUtils.parseBeantoJsonArray(listaUsuario);
        makePostVentasUsuario(ConnectionUtils.getVentasUsuarioParameter(),requestUsr);
        makepostGastosUsuario(ConnectionUtils.getGastosUsuarioParameter(),requestUsr);





    }
    public void makePostGastosRequest(String url,JSONArray params){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // mTextView.setText(response.toString());
                /*
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
                */

                System.out.println(" -------------->POST RESPONSE"+response);
                System.out.println(" -------------->ACTUALIZA GASTOS");
                SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
                SQLiteDatabase db = sqlHelper.getWritableDatabase();
                try {
                    if("ok".equals(response.getJSONObject(0).get("a"))) {
                        db.beginTransaction();
                        try {
                            //makeAllInserts();
                            for (int i = 0; i < listaGastos.size(); i++) {
                                ContentValues values = new ContentValues();
                                values.put("estatus", "S");
                                db.update("gastos", values, "id=" + listaGastos.get(i).getId(), null);
                            }
                            db.setTransactionSuccessful();
                            System.out.println("-----SQLite Trasn Succesful ");
                        } catch (Exception ex) {
                            System.out.println("-----SQLite Trasn Ex " + ex);
                        } finally {
                            db.endTransaction();
                            sendGastos=true;
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20*1000,
                0,
                1f));
        fRequestQueue.add(jsonArrayRequest);
    }

    final Handler handler = new Handler();
    final Runnable run = new Runnable() {
        public void run() {

            if(sendGastos && sendVentas) {
                runDownload();
            }else {
                handler.postDelayed(this,2000);


            }
        }

    };

    public void debug(String s){
        System.out.println(s);
    }

    public void runDownload(){
        final JsonArrayRequest requestClose=new JsonArrayRequest(ConnectionUtils.cerrarSesion(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                //  label.setText(jsonArray.toString());
                debug("------------->Cerrar sesion"+jsonArray.toString());
               // enableBtnUpdate();
                actualizaVentasGastos(idUsuarioBase);
                showToast("Base de Datos Actualizada");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
                enableBtnUpdate();
            }


        });

        final JsonArrayRequest requestUsuarios=new JsonArrayRequest(ConnectionUtils.getAllUsuariosParameter(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                debug("------------->Response Usuarios"+jsonArray.toString());

                SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
                SQLiteDatabase db = sqlHelper.getWritableDatabase();

                db.execSQL("delete from usuarios");
                db.execSQL("VACUUM");
                JSONArray usuarios = jsonArray;

                    /*
                    for (int i = 0; i < usuarios.length(); i++) {
                        try {
                            JSONObject obj = usuarios.getJSONObject(i);
                            db.execSQL("INSERT INTO usuarios (id, usuario, password,correo) " +
                                    "VALUES (" + obj.get("id") + ", '" + obj.get("usuario") + "', '" + obj.get("password") + "','" + obj.get("correo") + "' )");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    */
                db.beginTransaction();
                try {
                    //makeAllInserts();
                    for(int i = 0; i < usuarios.length(); i++) {
                        //  myDataBase = db.openDatabase();
                        JSONObject obj = usuarios.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.put("id", obj.get("id").toString());
                        values.put("usuario", obj.get("usuario").toString());
                        values.put("password", obj.get("password").toString());
                        values.put("correo", obj.get("correo").toString());


                        db.insert("usuarios", "correo", values);

                    }
                    db.setTransactionSuccessful();
                    System.out.println("-----SQLite Trasn Succesful ");
                }catch (Exception ex){
                    System.out.println("-----SQLite Trasn Ex "+ex);
                }finally {
                    db.endTransaction();
                }


                db.close();
                fRequestQueue.add(requestClose);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
                enableBtnUpdate();
            }


        });

        final JsonArrayRequest requestProductos=new JsonArrayRequest(ConnectionUtils.getAllProdParameter(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                debug("------------->Response Productos"+jsonArray.toString());
                SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
                SQLiteDatabase db = sqlHelper.getWritableDatabase();

                db.execSQL("delete from productos");
                db.execSQL("VACUUM");
                JSONArray productos = jsonArray;

                    /*
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
                    */

                db.beginTransaction();
                try {
                    //makeAllInserts();
                    for(int i = 0; i < productos.length(); i++) {
                        //  myDataBase = db.openDatabase();
                        JSONObject obj = productos.getJSONObject(i);
                        ContentValues values = new ContentValues();
                        values.put("id", obj.get("id").toString());
                        values.put("nombre", obj.get("nombre").toString());
                        values.put("denominacion", obj.get("denominacion").toString());
                        values.put("costo", obj.get("costo").toString());
                        values.put("marca", obj.get("marca").toString());
                        values.put("img", obj.get("img").toString());


                        db.insert("productos", "monto", values);

                    }
                    db.setTransactionSuccessful();
                    System.out.println("-----SQLite Trasn Succesful ");
                }catch (Exception ex){
                    System.out.println("-----SQLite Trasn Ex "+ex);
                }finally {
                    db.endTransaction();
                }



                db.close();
                fRequestQueue.add(requestUsuarios);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
                enableBtnUpdate();
            }


        });




        final JsonArrayRequest requestOpen=new JsonArrayRequest(ConnectionUtils.iniciarSesion(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                debug("------------->Iniciar sesion"+jsonArray.toString());



                fRequestQueue.add(requestProductos);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onConnectionFailed(error);
                enableBtnUpdate();
            }


        });



        fRequestQueue.add(requestOpen);

    }




    public class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {
            runUpdate();

            return true;
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

                 //   String latitude = taskResult.getJSONObject(i).getString("latitude")!=null && !"".equals(taskResult.getJSONObject(i).getString("latitude")) ?taskResult.getJSONObject(i).getString("latitude"):"";
                  //  String longitude = taskResult.getJSONObject(i).getString("longitude")!=null && !"".equals(taskResult.getJSONObject(i).getString("longitude")) ?taskResult.getJSONObject(i).getString("longitude"):"";
                    String latitude = taskResult.getJSONObject(i).getString("latitude");
                    String longitude = taskResult.getJSONObject(i).getString("longitude");

                    DatosVenta ventas=new DatosVenta();
                    ventas.setSession(ConnectionUtils.getSession());
                    ventas.setMonto(monto);
                    ventas.setCantidad(cantidad);
                    ventas.setFecha(fecha);
                    ventas.setIdp(idp);
                    ventas.setIdv(idv);
                    ventas.setId(idventa);

                    ventas.setLatitude(latitude);
                    ventas.setLongitude(longitude);
                    listaVentas.add(ventas);


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

                }
                sendGastos=false;
                sendVentas=false;



                System.out.println("----------->INICIA PETICIONES");
                if(listaVentas.size()>0){
                    JSONArray requestVentas=ConnectionUtils.parseBeantoJsonArray(listaVentas);
                    makePostVentasRequest(ConnectionUtils.insertVentasPost(),requestVentas);
                }else{
                    sendVentas=true;
                }

                if(listaGastos.size()>0){
                    JSONArray requestGastos=ConnectionUtils.parseBeantoJsonArray(listaGastos);
                    makePostGastosRequest(ConnectionUtils.insertGastosPost(),requestGastos);
                }else {
                    sendGastos=true;
                }

                if(!listaGastos.isEmpty() || !listaVentas.isEmpty()){
                    handler.postDelayed(run,1000);
                }else{
                    runDownload();
                    //actualizaVentasGastos(idUsuarioBase);
                }

                System.out.println("----------->INICIA DESCARGA");


            } catch (JSONException e) {
                e.printStackTrace();
                enableBtnUpdate();
            }
        }



    }

    /** Se ejecuta cuando encuentra un dispositivo */
    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String printerNameConfig=preferences.getString("config_printer","");

            String action = intent.getAction();
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found

                //searchResult.add(device);

                System.out.println("--------->Device "+device.getName());
                /*
                SearchList search=new SearchList();

                search.addToSearchList(device.getName(),device.getAddress());
                search.setDevices(device);

                */
                if(BluetoothUtils.devices!=null){
                    BluetoothUtils.devices.add(device);
                }


                if(printerNameConfig.equals(device.getName())){
                    BluetoothUtils.setMmDevice(device,context);
                    //BluetoothUtils.pairDevice(device);
                    utils.stopSearch();
                }

            }
            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                /*
                final int state        = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Log.d("BLUETOOTH","----->PAIRED");
                    try {
                        utils.openBT(context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    Log.d("BLUETOOTH","----->unPAIRED");
                }
                */

            }
            if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
                if("SPP-R200III".equals(device.getName()) || "Bluedio".equals(device.getName()) ){
                    Log.d("BLUETOOTH","CONNECTED: "+device.getName());
                    BluetoothUtils.printerConected=true;
                    Toast.makeText(context, "> TRUE", Toast.LENGTH_SHORT).show();
                }
            }
            if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){
                if("SPP-R200III".equals(device.getName()) || "Bluedio".equals(device.getName()) ){
                    Log.d("BLUETOOTH","DISCONNECTED: "+device.getName());
                    BluetoothUtils.printerConected=false;
                    //BluetoothUtils.alreadyConected
                    Toast.makeText(context, "> FALSE", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
