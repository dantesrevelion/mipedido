package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterCarrito;
import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;
import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;

public class CarritoCompra extends BaseActivity {

    private Toolbar toolbar;
    JSONArray taskResult=null;
    ListView lista;
    VysorAdapterCarrito adapter;
    Double total=0d;
    public static BluetoothUtils utils;
    TextView tv_total;
    public static Button bt_generar;
    public static Button bt_imprimir;
    public static Button bt_eliminar;
    public static boolean searchIsVisible=false;
    public static Activity activity;
    public static Handler handler = new Handler();

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
        utils= new BluetoothUtils(getBaseContext());


    }

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
    public void generarVenta(View v) throws JSONException {
      //  handler.postDelayed(r,1000);
      //  thread.start();


        System.out.println("GENERAR VENTA------------->");
        for(int i=0;i<taskResult.length();i++){
            ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.updateEstadoVentatoP(taskResult.getJSONObject(i).getString("id_venta")));
            String idp=taskResult.getJSONObject(i).getString("id_producto");
            String idv=taskResult.getJSONObject(i).getString("id_vendedor");
            String cant=taskResult.getJSONObject(i).getString("cantidad");
            String monto=taskResult.getJSONObject(i).getString("monto");
            ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.insertVenta(idp,idv,cant,monto));
        }

        consultaCarrito();


        if(ConnectionUtils.conectadoWifi(this) || ConnectionUtils.conectadoRedMovil(this)) {

         //   CheckIn.checkInProcess(this);
            new callCheckIn().execute();

        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Venta offline", Toast.LENGTH_SHORT);
            toast1.show();
        }



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


        for (int i=0;i<adapter.getCount();i++){
            CheckBox cb=(CheckBox) lista.getChildAt(i).findViewById(R.id.check);

            if(cb.isChecked()){

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

    public void imprimir(View v){
        System.out.println("--------->Inicia proceso de impresion");
        if(utils.bluetoothIsOn(this)){
            if(utils.isPaired()){

            }else{
                utils.searchDevices();
                showSearchList();

            }

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

    public class callCheckIn extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            CheckIn.checkInRunnable(getBaseContext(),activity);
            return true;
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("--------->Activity result");
        /** si esta on */
        if(101==requestCode){
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter.isEnabled()) {
                if (utils.isPaired()) {

                } else {
                    utils.searchDevices();
                    showSearchList();

                }
            }

        }
        if(102==requestCode){

        }

    }



}
