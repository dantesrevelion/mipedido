package com.example.dantesrevelion.mipedido.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.MainActivity;
import com.example.dantesrevelion.mipedido.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dantes Revelion on 01/07/2016.
 */
public class ConnectionUtils {

   /*   dominio=mipedidoapp.esy.es
        $host=mysql.hostinger.mx
	    $user=u152348074_ma12m
	    $password=mipedido123
	    $db=u152348074_mpdb

	    provisional0716
    */
    private static String DOMAIN="";
    private static String HOST="";
    private static String USUARIO="";
    private static String PASS="";
    private static String BD="";
    private static String USUARIO_APP="";


    public ConnectionUtils(){

    }
    public static void createConection(Context context){
        System.out.println("GETTING PREFERENCES ");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String domain=preferences.getString("config_domain","");
        String host=preferences.getString("config_host","");
        String user=preferences.getString("config_user","");
        String pass=preferences.getString("config_pass","");
        String bd=preferences.getString("config_db","");

        /**TODO remove hardcode*/
            domain="mipedidoapp.esy.es";host="mysql.hostinger.mx";user="u152348074_ma12m";pass="mipedido123";bd="u152348074_mpdb";
        /***/
        System.out.println("DATOS DE CONFIGURACION: "+domain+" "+host+" "+user+" "+pass+" "+bd);
        setDOMAIN(domain);
        setHOST(host);
        setUSUARIO(user);
        setPASS(pass);
        setBD(bd);


    }
    public static String iniciarSesion(){
        String COMP_USER="http://"+ getDOMAIN() +"/mipedido/res/iniciarSesion.php?h="+getHOST()+"&u="+getUSUARIO()+"&p="+getPASS()+"&b="+getBD();
        return COMP_USER;
    }
    public static String cerrarSesion(){
        String COMP_USER="http://"+ getDOMAIN() +"/mipedido/res/cerrarSesion.php";
        return COMP_USER;
    }
    public static String getUserParameter(String usuario){
        String COMP_USER="http://"+ getDOMAIN() +"/mipedido/res/usuario.php?usuario="+usuario;
        return COMP_USER;
    }
    public static String getIdentUserPassParameter(String usuario, String password){
        String IDENT_USERPASS="http://"+ getDOMAIN() +"/mipedido/res/password.php?user="+usuario+"&identify="+password;
        return IDENT_USERPASS;
    }

    public static String getAllProdParameter(){
        String ALL_PROD="http://"+ getDOMAIN() +"/mipedido/res/allprod.php";
        return ALL_PROD;
    }
    public static String getAllGastos(){
        String ALL_PROD="http://"+ getDOMAIN() +"/mipedido/res/allGastos.php";
        return ALL_PROD;
    }
    public static String getAllVentasParameter(){
        String ALL_VENTAS="http://"+ getDOMAIN() +"/mipedido/res/allventas.php";
        return ALL_VENTAS;
    }
    public static String getVentaByfechaParameter(String ini,String fin){
        String VENTA_BYFECHA="http://"+ getDOMAIN() +"/mipedido/res/ventabydaterange.php?di="+ini+"&df="+fin;
        return VENTA_BYFECHA;
    }

    public static String getAllUsuariosParameter(){
        String VENTA_BYFECHA="http://"+ getDOMAIN() +"/mipedido/res/allusuarios.php";
        return VENTA_BYFECHA;
    }
    public static String getVentasByUsuarioFechaParameter(String ini,String fin,String idv){
        String VENTA_BYFECHA="http://"+ getDOMAIN() +"/mipedido/res/ventasbyusuariofecha.php?di="+ini+"&df="+fin+"&idv="+idv;
        return VENTA_BYFECHA;
    }
    public static String getVentasByUsuarioParameter(String idv){
        String VENTA_BYFECHA="http://"+ getDOMAIN() +"/mipedido/res/ventasbyusuario.php?idv="+idv;
        return VENTA_BYFECHA;
    }
    public static String querySearchUsuario(String usuario){
        String response = "select usuario from usuarios where usuario='"+usuario+"'";
        return response;
    }
    public static String queryIdentifyUsuario(String usuario,String pass ){
        String response = "SELECT id,usuario,correo from usuarios where usuario='"+usuario+"' and password='"+pass+"'";
        return response;
    }
    public static String queryAllUsuarios(){
        String response = "SELECT * from usuarios ";
        return response;
    }
    public static String queryDatosDeUsuario(String usuario){
        String response = "SELECT * from usuarios where usuario='"+usuario+"'";
        return response;
    }
    public static String queryAllProd(){
        String response = "select * from productos";
        return response;
    }
    public static String queryVentasByUsuario(String idv,String fecha,String fecha2){
      //  String response = "select * from ventas JOIN productos,usuarios where ventas.id_producto=productos.id and ventas.id_vendedor="+idv+" AND usuarios.id ="+idv+" AND fecha between '"+fecha+"' and '"+fecha2+"'";
        return "";
    }
    public static String queryVentasByUsuarioFecha(String fini,String ffin,String idv){
        String response = "select * from ventas JOIN productos,usuarios where ventas.id_producto=productos.id and fecha between '"+fini+"' and '"+ffin+"' and ventas.id_vendedor="+idv+" AND usuarios.id ="+idv;
        return response;
    }
    public static String queryMontoGastosByFecha(String fini,String ffin,String idv){
        String response = "select monto from gastos where fecha between '"+fini+"' and '"+ffin+"' and idvendedor="+idv;
        return response;
    }

    public static String insertCarrito(String idp,String idv,String cant,String monto,String estatus){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String fecha=year+"-"+(month+1)+"-"+day+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        String formated=ConnectionUtils.formatDateGeneral(fecha,"yyyy-MM-dd HH:mm:ss");

        String response = "INSERT INTO carrito ( id_producto, id_vendedor,fecha,cantidad,monto,estatus)" +
                "VALUES ("+idp+","+idv+",'"+formated+"',"+cant+","+monto+",'"+estatus+"'); ";
        return response;
    }
    public static String insertGastos(String idu,String nombre,String codigo,String monto,String estatus){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String fecha=year+"-"+(month+1)+"-"+day+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        String formated=ConnectionUtils.formatDateGeneral(fecha,"yyyy-MM-dd HH:mm:ss");
        String response = "INSERT INTO `gastos` (`idvendedor`, `nombre`, `codigo`, `monto`,fecha,`estatus`)" +
                " VALUES ('"+idu+"', '"+nombre+"', '"+codigo+"', '"+monto+"','"+formated+"','"+estatus+"'); ";
        return response;
    }
    public static String insertVenta(String idp,String idv,String cant,String monto){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String fecha=year+"-"+(month+1)+"-"+day+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        String formated=ConnectionUtils.formatDateGeneral(fecha,"yyyy-MM-dd HH:mm:ss");


        String response = "INSERT INTO ventas ( id_producto, id_vendedor,fecha,cantidad,monto)" +
                "VALUES ("+idp+","+idv+",'"+formated+"',"+cant+","+monto+"); ";
        return response;
    }
    public static String queryCarrito(){
        String response = "select * from carrito where estatus='P'";
        return response;
    }
    public static String queryCarritoUp(){
        String response = "select * from carrito where estatus='V'";
        return response;
    }
    public static String queryGastosUp(){
        String response = "select * from gastos where estatus='P'";
        return response;
    }

    public static String updateEstadoVentatoP(String id){
        String response = "update carrito SET estatus='V' where id_venta="+id;
        return response;
    }
    public static String updateEstadoVentatoS(String id){
        String response = "update carrito SET estatus='S' where id_venta="+id;
        return response;
    }
    public static String updateEstadoGastosS(String id){
        String response = "update gastos SET estatus='S' where id="+id;
        return response;
    }
    public static String deleteCarrito(String id){
        String response = "delete from carrito where id_venta="+id;
        return response;
    }

    public static String insertVentas(String idp,String idv,String cantidad,String monto,String fecha){
        String url = "";
        try {
            url = "http://"+ getDOMAIN() +"/mipedido/res/ventasinsert.php?idp="+idp+"&idv="+idv+"&c="+cantidad+"&m="+monto+"&pFecha="+ URLEncoder.encode(fecha, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static String insertGastosWEB(String idv,String nombre,String codigo,String monto,String paramFecha){
        String url = "";
        try {
            url = "http://"+ getDOMAIN() +"/mipedido/res/gastosinsert.php?idu="+idv+"&nombre="+nombre+"&cod="+codigo+"&m="+monto+"&pFecha="+ URLEncoder.encode(paramFecha, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
    HttpURLConnection urlConnection;
    InputStream is;

    public static String getDOMAIN() {
        return DOMAIN;
    }

    public static void setDOMAIN(String DOMAIN) {
        ConnectionUtils.DOMAIN = DOMAIN;
    }

    public static String getUSUARIO() {
        return USUARIO;
    }

    public static void setUSUARIO(String USUARIO) {
        ConnectionUtils.USUARIO = USUARIO;
    }

    public static String getPASS() {
        return PASS;
    }

    public static void setPASS(String PASS) {
        ConnectionUtils.PASS = PASS;
    }

    public static String getBD() {
        return BD;
    }

    public static void setBD(String BD) {
        ConnectionUtils.BD = BD;
    }

    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        ConnectionUtils.HOST = HOST;
    }

    public static String getUsuarioApp() {
        return USUARIO_APP;
    }

    public static void setUsuarioApp(String usuarioApp) {
        USUARIO_APP = usuarioApp;
    }

    public JSONArray connect(String urlParameter) {
        JSONArray response=null;

        try {
            System.out.println("CREAR CONECCION CON------------->"+urlParameter);

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
            System.out.println("Error 1");
        } catch (IOException e) {
            System.out.println("Error 2");
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
    public static String formatDateGeneral(String s,String format){
        DateFormat formatter = new SimpleDateFormat(format);
        String response="";
        try {
            Date date = formatter.parse(s);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
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
    private static NotificationCompat.Builder mBuilder;
    private static NotificationManager mNotificationManager;
    private static PendingIntent contIntent;
    public static void showNotificacion (Context c){

       mBuilder =new NotificationCompat.Builder(c)
                        .setSmallIcon(R.drawable.download)
                        .setLargeIcon((((BitmapDrawable)c.getResources()
                                .getDrawable(R.drawable.download)).getBitmap()))
                        .setContentTitle("Actualizando BD")
                        .setTicker("Actualizando BD");
        Intent notIntent =
                new Intent(c, MainActivity.class);

         contIntent = PendingIntent.getActivity(c, 0, notIntent, 0);
         mNotificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(01, mBuilder.build());

        mBuilder.setContentIntent(contIntent);
    }
    public static void setprogress(int n,Context c){
        mBuilder.setProgress(80,n,false);
        mNotificationManager.notify(01, mBuilder.build());
    }

    public static void notificateError(Context c){
        mBuilder =new NotificationCompat.Builder(c)
                .setSmallIcon(R.drawable.download)
                .setLargeIcon((((BitmapDrawable)c.getResources()
                        .getDrawable(R.drawable.download)).getBitmap()))
                .setContentTitle("Error al Actualizar la BD")
                .setTicker("Error al Actualizar");
        mNotificationManager.notify(01, mBuilder.build());
    }

    public static void notificateOK(Context c){
        mBuilder =new NotificationCompat.Builder(c)
                .setSmallIcon(R.drawable.download)
                .setLargeIcon((((BitmapDrawable)c.getResources()
                        .getDrawable(R.drawable.download)).getBitmap()))
                .setContentTitle("BD Actualizada")
                .setTicker("BD Actualizada");
        mNotificationManager.notify(01, mBuilder.build());
    }

}
