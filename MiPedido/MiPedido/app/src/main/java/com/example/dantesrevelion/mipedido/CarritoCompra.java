package com.example.dantesrevelion.mipedido;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterCarrito;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compra);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_carrito); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        consultaCarrito();

    }

    public void consultaCarrito(){
        taskResult= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryCarrito());

        lista= (ListView) findViewById(R.id.listaCarrito);

        adapter= new VysorAdapterCarrito(CarritoCompra.this, R.layout.item_carrito,
                ConnectionUtils.jsonToArray(taskResult,"id_venta"), taskResult);


        lista.setAdapter(adapter);

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

    }


}
