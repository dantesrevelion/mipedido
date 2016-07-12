package com.example.dantesrevelion.mipedido.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dantes Revelion on 01/07/2016.
 */
public class ConnectionUtils {

    public static String DOMAIN="mipedidoapp.esy.es";


    public ConnectionUtils(){

    }
    public static String getUserParameter(String usuario){
        String COMP_USER="http://"+DOMAIN+"/mipedido/res/usuario.php?usuario="+usuario;
        return COMP_USER;
    }
    public static String getIdentUserPassParameter(String usuario, String password){
        String IDENT_USERPASS="http://"+DOMAIN+"/mipedido/res/password.php?user="+usuario+"&identify="+password;
        return IDENT_USERPASS;
    }

    public static String getAllProdParameter(){
        String ALL_PROD="http://"+DOMAIN+"/mipedido/res/allprod.php";
        return ALL_PROD;
    }
    public static String getAllVentasParameter(){
        String ALL_VENTAS="http://"+DOMAIN+"/mipedido/res/allventas.php";
        return ALL_VENTAS;
    }
    public static String getVentaByfechaParameter(String ini,String fin){
        String VENTA_BYFECHA="http://"+DOMAIN+"/mipedido/res/ventabydaterange.php?di="+ini+"&df="+fin;
        return VENTA_BYFECHA;
    }

    public static String getAllUsuariosParameter(){
        String VENTA_BYFECHA="http://"+DOMAIN+"/mipedido/res/allusuarios.php";
        return VENTA_BYFECHA;
    }
    public static String getVentasByUsuarioFechaParameter(String ini,String fin,String idv){
        String VENTA_BYFECHA="http://"+DOMAIN+"/mipedido/res/ventasbyusuariofecha.php?di="+ini+"&df="+fin+"&idv="+idv;
        return VENTA_BYFECHA;
    }
    public static String getVentasByUsuarioParameter(String idv){
        String VENTA_BYFECHA="http://"+DOMAIN+"/mipedido/res/ventasbyusuario.php?idv="+idv;
        return VENTA_BYFECHA;
    }
    public static String querySearchUsuario(String usuario){
        String response = "select usuario from usuarios where usuario='"+usuario+"'";
        return response;
    }
    public static String queryIdentifyUsuario(String usuario,String pass ){
        String response = "SELECT usuario,correo from usuarios where usuario='"+usuario+"' and password='"+pass+"'";
        return response;
    }
    public static String queryAllUsuarios(){
        String response = "SELECT * from usuarios ";
        return response;
    }
    public static String queryAllProd(){
        String response = "select * from productos";
        return response;
    }
    public static String queryVentasByUsuario(String idv){
        String response = "select * from ventas JOIN productos,usuarios where ventas.id_producto=productos.id and ventas.id_vendedor="+idv;
        return response;
    }
    public static String queryVentasByUsuarioFecha(String fini,String ffin,String idv){
        String response = "select * from ventas JOIN productos,usuarios where ventas.id_producto=productos.id and fecha between '"+fini+"' and '"+ffin+"' and ventas.id_vendedor="+idv;
        return response;
    }
    HttpURLConnection urlConnection;
    InputStream is;

    public JSONArray connect(String urlParameter) {
        JSONArray response=null;
        try {
            URL url= new URL(urlParameter);
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.connect();
            is=urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            String result = sb.toString();
            response=converToJson(result);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;
    }

    public JSONArray converToJson(String s){
        JSONArray json=null;
        try {
            json=new JSONArray(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String [] jsonToArray(JSONArray json,String parameter){

        String[] response = new String[json.length()];
        for(int i = 0; i < json.length(); i++){
            try {
                response[i]=json.getJSONObject(i).getString(parameter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return response;
    }

    public static String formatDate(String s){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String response="";
        try {
            Date date = formatter.parse(s);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            response=sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }





        return response;
    }

    public static boolean conectadoWifi(Context context){

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean conectadoRedMovil(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    public static JSONArray consultaSQLite(Context context,String query){
        SQLiteHelper sqlHelper=new SQLiteHelper(context, "miPedidoLite", null, 1);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        Cursor c=db.rawQuery(query,null);
        JSONArray response=cursorToJsonArray(c);
        return response;
    }

    public static JSONArray cursorToJsonArray(Cursor c){


        JSONArray resultSet 	= new JSONArray();
        JSONObject returnObj 	= new JSONObject();

        c.moveToFirst();
        while (c.isAfterLast() == false) {

            int totalColumn = c.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( c.getColumnName(i) != null )
                {

                    try
                    {

                        if( c.getString(i) != null )
                        {

                            rowObject.put(c.getColumnName(i) ,  c.getString(i) );
                        }
                        else
                        {
                            rowObject.put( c.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                       e.printStackTrace();
                    }
                }

            }

            resultSet.put(rowObject);
            c.moveToNext();
        }

        c.close();
        System.out.println("CONVERTED-------------------------->"+resultSet);
        return resultSet;


    }

}
