package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterCarrito;
import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;
import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.SQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CarritoCompra extends BaseActivity {

    private Toolbar toolbar;
    JSONArray taskResult=null;
    ListView lista;
    VysorAdapterCarrito adapter;
    Double total=0d;

    TextView tv_total;
    public static Button bt_generar;
    public static Button bt_imprimir;
    public static Button bt_eliminar;
    public static boolean searchIsVisible=false;
    public static Activity activity;
    public static Handler handler = new Handler();

//    Handler handlerUpdate ;
//    Runnable runStart;

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                for (int i=0;i<2000;i++){
                    debug("DERP "+i);
                    //handler.postDelayed(this, 1000);
                    sleep(1000);
                    handler.post(this);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable r = new Runnable() {
        public void run() {

            /*
            System.out.println("GENERAR VENTA------------->");
            for(int i=0;i<taskResult.length();i++){
                try {

                ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.updateEstadoVentatoP(taskResult.getJSONObject(i).getString("id_venta")));
                String idp=taskResult.getJSONObject(i).getString("id_producto");
                String idv=taskResult.getJSONObject(i).getString("id_vendedor");
                String cant=taskResult.getJSONObject(i).getString("cantidad");
                String monto=taskResult.getJSONObject(i).getString("monto");
                ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.insertVenta(idp,idv,cant,monto));
                } catch (JSONException e) {
                  debug("error al obtener datos json");
                }
            }

            consultaCarrito();


            if(ConnectionUtils.conectadoWifi(activity) || ConnectionUtils.conectadoRedMovil(activity)) {

                CheckIn.checkInProcess(activity);

            }else{
                Toast toast1 = Toast.makeText(getApplicationContext(),
                        "Venta offline", Toast.LENGTH_SHORT);
                toast1.show();
            }
            //handler.postDelayed(this, 1000);
            */
            for (int i=0;i<2000;i++){
                debug("DERP "+i);
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compra);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_carrito); // Attaching the layout to the toolbar object
        tv_total= (TextView) findViewById(R.id.tv_total_carrito);
        bt_generar=(Button) findViewById(R.id.bt_generar_carrito);
        bt_imprimir=(Button) findViewById(R.id.bt_imprimir_carrito);
        bt_eliminar=(Button) findViewById(R.id.bt_eliminar_carrito);
        activity =this;
        setSupportActionBar(toolbar);
        consultaCarrito();

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        SearchList.flagSerch=true;
        startUpdates(18,1000,null);
        /*
        requestUpdate();
        handlerUpdate = new Handler();



        runStart = new Runnable() {
            public void run() {

                if(currentLocation.getAccuracy()<15){
                    System.out.println("**** ACCURACY OK ****"+currentLocation.getAccuracy());
                    stopUpdate();
                }else{
                    handlerUpdate.postDelayed(runStart, 1000);
                    requestUpdate();
                }

            }
        };

        handlerUpdate.postDelayed(runStart, 200);
        */

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopUpdate();
    }
    Handler handlerStart;
    public void consultaCarrito(){

        taskResult= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryCarrito());

        lista= (ListView) findViewById(R.id.listaCarrito);

        adapter= new VysorAdapterCarrito(CarritoCompra.this, R.layout.item_carrito,
                ConnectionUtils.jsonToArray(taskResult,"id_venta"), taskResult);


        lista.setAdapter(adapter);
        calculaTotal();

    }

    public void calculaTotal(){
        total=0d;
        for(int i=0;i<taskResult.length();i++){


            try {
                String monto=taskResult.getJSONObject(i).getString("monto");
                Double valor=Double.parseDouble(monto);
                total=total+valor;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        tv_total.setText("$"+String.valueOf(total));
    }
    public void generarVenta(final View v) throws JSONException {
      //  handler.postDelayed(r,1000);
      //  thread.start();
        /*
        if(!isGPSEnabled()){
            Toast.makeText(this, "Activar GPS para realizar la venta", Toast.LENGTH_SHORT).show();
            return ;
        }
        */

       // requestUpdate();


        final Handler handler = new Handler();
        final Runnable run = new Runnable() {
            public void run() {
                //stopUpdate();
                /*
                if(currentLocation==null){
                    stopUpdate();
                    requestUpdate();
                    handler.postDelayed(this,2000);
                }else{

                }
                */

        System.out.println("GENERAR VENTA------------->");
        /*
        for(int i=0;i<taskResult.length();i++){
            ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.updateEstadoVentatoP(taskResult.getJSONObject(i).getString("id_venta")));
            String idp=taskResult.getJSONObject(i).getString("id_producto");
            String idv=taskResult.getJSONObject(i).getString("id_vendedor");
            String cant=taskResult.getJSONObject(i).getString("cantidad");
            String monto=taskResult.getJSONObject(i).getString("monto");
            ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.insertVenta(idp,idv,cant,monto));
        }
*/
        SQLiteHelper sqlHelper=new SQLiteHelper(getBaseContext(), "miPedidoLite", null, 1);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            //makeAllInserts();
            //id_producto, id_vendedor,fecha,cantidad,monto
            for (int i = 0; i < taskResult.length(); i++) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String fecha=year+"-"+(month+1)+"-"+day+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                String formated=ConnectionUtils.formatDateGeneral(fecha,"yyyy-MM-dd HH:mm:ss");
                ContentValues values = new ContentValues();
                values.put("id_producto", taskResult.getJSONObject(i).getString("id_producto"));
                values.put("id_vendedor", taskResult.getJSONObject(i).getString("id_vendedor"));
                values.put("cantidad", taskResult.getJSONObject(i).getString("cantidad"));
                values.put("monto", taskResult.getJSONObject(i).getString("monto"));
                values.put("fecha", formated);
                values.put("latitude",currentLocation.getLatitude());
                values.put("longitude",currentLocation.getLongitude());
                ContentValues valuesUp = new ContentValues();
                valuesUp.put("estatus","V");
              //  valuesUp.put("latitude",currentLocation.getLatitude());
              //  valuesUp.put("longitude",currentLocation.getLongitude());

                db.update("carrito", valuesUp, "id_venta=" +taskResult.getJSONObject(i).getString("id_venta"), null);
                db.insert("ventas", "monto", values);
                //db.update("gastos", values, "id=" + listaGastos.get(i).getId(), null);
            }
            db.setTransactionSuccessful();
            //imprimir(v);
            //BluetoothUtils.testText(getListToPrint());
            Answers.getInstance().logCustom(new CustomEvent("Venta Generada"));
            System.out.println("-----SQLite Trasn Succesful ");
            bt_imprimir.setEnabled(true);
        } catch (Exception ex) {
            System.out.println("-----SQLite Trasn Ex " + ex);
        } finally {
            db.endTransaction();
        }
        consultaCarrito();


            }
        };
        handler.postDelayed(run,500);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.config){
            System.out.println("Configuracion clicked");
            Intent intent=new Intent(CarritoCompra.this,Configuracion.class);
            startActivity(intent);
        }
        return true;
    }


    public void eliminarSeleccion(View v){

        int count=lista.getCount();
        for (int i=0;i<count;i++){
            //CheckBox cb=(CheckBox) lista.getChildAt(i).findViewById(R.id.check);
            //CheckBox cb=(CheckBox) lista.getChildAt(i).findViewById(R.id.check);
            boolean isChecked=adapter.isChecked()[i];

            if(isChecked){

                try {
                    ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.deleteCarrito( taskResult.getJSONObject(i).get("id_venta").toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        consultaCarrito();
        calculaTotal();
    }
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
    public  String padLeft(String str, int n) {
        return String.format("%1$" + n + "s", str);
    }
    public List<String> getListToPrint() throws JSONException {
        List<String> registros=new ArrayList<>();
        for (int i = 0; i < taskResult.length(); i++) {

            //  valuesUp.put("latitude",currentLocation.getLatitude());
            //  valuesUp.put("longitude",currentLocation.getLongitude());
            JSONArray localValue= ConnectionUtils.consultaSQLite(getBaseContext(),"select * from productos where id="+taskResult.getJSONObject(i).getString("id_producto"));
            //JSONArray localUsuarios= ConnectionUtils.consultaSQLite(getBaseContext(),"select * from usuario where id="+localValue.getJSONObject(i).getString("id_vendedor"));
            String cant=padRight(taskResult.getJSONObject(i).getString("cantidad"),3);
            String art=padRight(localValue.getJSONObject(0).getString("nombre"),14);
            String cu=padLeft(localValue.getJSONObject(0).getString("costo"),7);
            String totalcu="$"+padLeft(taskResult.getJSONObject(i).getString("monto"),7);
            String cadena=art+cant+cu+totalcu;
            //String value = new String(cadena., "UTF-8");
            registros.add(cadena);
        }
        registros.add("                                ");
        registros.add("                                ");
        registros.add("       TOTAL            $"+padLeft(String.valueOf(total),7));
        registros.add("                                ");
        registros.add("Latitud: "+padRight(String.valueOf(currentLocation.getLatitude()),23));
        registros.add("Longitud: "+padRight(String.valueOf(currentLocation.getLongitude()),22));

        return registros;
    }

    final Handler handlerImprimir = new Handler();
    int counterTrys=0;
    public void imprimir(final View v){
        Answers.getInstance().logCustom(new CustomEvent("Inicia pronceso Imprimir "));
        System.out.println("--------->Inicia proceso de impresion");
        bt_imprimir.setEnabled(false);
        counterTrys=0;
        if(utils.bluetoothIsOn(this)){
            utils.searchDevices();

            handlerImprimir.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean flagOK=false;

                    for(BluetoothDevice deviceLocal:BluetoothUtils.devices){
                        Answers.getInstance().logCustom(new CustomEvent("Bandera de conexion").putCustomAttribute("Conected Flag","Flag: "+BluetoothUtils.printerConected));

                        if(("SPP-R200III".equals(deviceLocal.getName()) || "Bluedio".equals(deviceLocal.getName()))){
                            Answers.getInstance().logCustom(new CustomEvent("Encontro la impresora").putCustomAttribute("device","device: "+deviceLocal.getAddress()));
                            Log.d("Printing Progres", ">>>>>>>>>>>>>>Encontro la impresora");
                            try {
                                    utils.openBT(getBaseContext());
                                    utils.sendData(getListToPrint(),ConnectionUtils.getUsuarioApp());
                                    generarVenta(v);
                                    Log.d("Printing Progres", ">>>>>>>>>>>>>>Venta Generada");
                                    flagOK=true;
                                    break;


                            } catch (IOException e) {
                                Log.d("Printing Progres", ">>>>>>>>>>>>>>No se realizo la conexion");
                               // Toast.makeText(getBaseContext(), "no se pudo imprimir", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                Answers.getInstance().logCustom(new CustomEvent("No se realizo la conexion ").putCustomAttribute("Error",e.getMessage()));
                                bt_imprimir.setEnabled(true);
                                break;
                            } catch (JSONException e) {
                                Log.d("Printing Progres", ">>>>>>>>>>>>>>No se realizo la conexion");
                                Answers.getInstance().logCustom(new CustomEvent("No se realizo la conexion ").putCustomAttribute("Error",e.getMessage()));
                               // Toast.makeText(getBaseContext(), "no se pudo imprimir", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                bt_imprimir.setEnabled(true);
                                break;
                            }


                        }
                    }


                    if(counterTrys<4){
                        Log.d("Printing Progres", ">>>>>>>>>>>>>>Busca en la lista de nuevo");
                        handlerImprimir.postDelayed(this,2000);
                    }else if(!flagOK){
                        Toast.makeText(getBaseContext(), "Impresora no disponible", Toast.LENGTH_SHORT).show();
                        bt_imprimir.setEnabled(true);
                    }
                    counterTrys++;

                }
            }, 3000);

            /*
            if(BluetoothUtils.alreadyConected || utils.isPaired(this)){
                try {
                    if(!utils.isOpen()){
                       utils.openBT(getBaseContext());
                    }


                    try {

                        utils.sendData(getListToPrint(),ConnectionUtils.getUsuarioApp());
                        generarVenta(v);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        bt_imprimir.setEnabled(true);
                    }
                } catch (IOException e) {
                 //   Toast toast=new Toast(getBaseContext());
                 //   toast.setText("no se pudo imprimir");
                    Toast.makeText(this, "no se pudo imprimir", Toast.LENGTH_SHORT).show();
                    bt_imprimir.setEnabled(true);
                }
            }else{
                utils.searchDevices();
                showSearchList();
                bt_imprimir.setEnabled(true);
            }
        */

        }else{
            bt_imprimir.setEnabled(true);
        }

      //  Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
       // startActivityForResult(enableBluetooth, 0);
    }
    public void showSearchList(){
        System.out.println("--------->Muestra lista de dispositivos");
        switchButtons(false);
        searchIsVisible=true;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchList fragment = new SearchList();
        fragmentTransaction.add(R.id.fragment_container_carrito, fragment, "listSearch");
        fragmentTransaction.commit();

    }
    public static void switchButtons(boolean bol){
        bt_eliminar.setEnabled(bol);
        bt_imprimir.setEnabled(bol);
        bt_generar.setEnabled(bol);
    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
        if(searchIsVisible){
            hideSearchList();
            utils.stopSearch();
            try {
                utils.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            super.onBackPressed();
        }
    }
    public static void hideSearchList(){
        activity.getFragmentManager().beginTransaction().remove(activity.getFragmentManager().findFragmentById(R.id.fragment_container_carrito)).commit();
        searchIsVisible=false;
        switchButtons(true);
       // utils.stopSearch();
    }

    /*
    public class callCheckIn extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            CheckIn.checkInRunnable(getBaseContext(),activity);
            return true;
        }

    }
    */



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** si esta on */
        if(101==requestCode){
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter.isEnabled()) {
                /*
                if (BluetoothUtils.alreadyConected || !utils.isPaired(this)) {

                    utils.searchDevices();
                    showSearchList();

                }
                */
            }

        }
        if(102==requestCode){

        }

    }



}
