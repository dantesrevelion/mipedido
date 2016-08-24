package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterCarrito;
import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterSearchList;
import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;
import com.example.dantesrevelion.mipedido.Utils.CheckIn;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class CarritoCompra extends BaseActivity {

    private Toolbar toolbar;
    JSONArray taskResult=null;
    ListView lista;
    VysorAdapterCarrito adapter;
    Double total=0d;
    BluetoothUtils utils;
    TextView tv_total;
    Button bt_generar;
    Button bt_imprimir;
    Button bt_eliminar;
    boolean searchIsVisible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compra);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_carrito); // Attaching the layout to the toolbar object
        tv_total= (TextView) findViewById(R.id.tv_total_carrito);
        bt_generar=(Button) findViewById(R.id.bt_generar_carrito);
        bt_imprimir=(Button) findViewById(R.id.bt_imprimir_carrito);
        bt_eliminar=(Button) findViewById(R.id.bt_eliminar_carrito);
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

            CheckIn.checkInProcess(this);

        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(),
                    "Venta offline", Toast.LENGTH_SHORT);
            toast1.show();
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

            System.out.println("checked "+cb.isChecked());

        }
        consultaCarrito();
        calculaTotal();
    }

    public void imprimir(View v){
        /*
        try {

            debug("Bluetooth is ON");
            boolean isEnabled=(boolean) new verificarBluetoothClass().execute(this).get();
            debug("response "+isEnabled);

            if(isEnabled){
                debug("Bluetooth Search Devices");
                new buscarDispositivos().execute();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    */

        if(utils.bluetoothIsOn(this)){
            if(utils.isPaired()){

            }else{
                utils.searchDevices();
                showSearchList();
                utils.setTextViewLEL(tv_total);
            }

        }

      //  Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
       // startActivityForResult(enableBluetooth, 0);
    }
    public void showSearchList(){
        switchButtons(false);
        searchIsVisible=true;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchList fragment = new SearchList();
        fragmentTransaction.add(R.id.fragment_container_carrito, fragment, "listSearch");
        fragmentTransaction.commit();

    }
    public void switchButtons(boolean bol){
        bt_eliminar.setEnabled(bol);
        bt_imprimir.setEnabled(bol);
        bt_generar.setEnabled(bol);
    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
        if(searchIsVisible){
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container_carrito)).commit();
            searchIsVisible=false;
            switchButtons(true);
        }else{
            super.onBackPressed();
        }
    }

    private class buscarDispositivosClass extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            return "";
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        debug("request code "+requestCode);
        debug("is paired "+utils.isPaired());
        /** si esta on */
        if(101==requestCode){
            if(utils.isPaired()){

            }else{
                utils.searchDevices();
                showSearchList();
                utils.setTextViewLEL(tv_total);
            }

        }
        if(102==requestCode){

        }

    }



}
