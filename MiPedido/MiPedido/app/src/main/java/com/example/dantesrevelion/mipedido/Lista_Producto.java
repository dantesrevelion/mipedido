package com.example.dantesrevelion.mipedido;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterProducto;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class Lista_Producto extends BaseActivity  {

    JSONArray taskResult=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__producto);
        final ListView lista = (ListView) findViewById(R.id.listaProductos);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        // final String[] items ={"1","2","3","4","5","6","7","8"};

        /*
        String[] items = new String[10];
        for (int i = 0; i < items.length; i++) {
            items[i] = " Epura 20L " + (i + 1);
        }
        */


            taskResult= ConnectionUtils.consultaSQLite(this,ConnectionUtils.queryAllProd());

        VysorAdapterProducto adapter = new VysorAdapterProducto(Lista_Producto.this,
                R.layout.item_producto,ConnectionUtils.jsonToArray(taskResult,"nombre"), taskResult);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Lista_Producto.this, Producto.class);
                try {
                    intent.putExtra("id_usuario",getIntent().getExtras().getString("id_usuario"));
                    intent.putExtra("id_producto",taskResult.getJSONObject(position).get("id").toString());
                    intent.putExtra("nombre",taskResult.getJSONObject(position).get("nombre").toString());
                    intent.putExtra("costo",taskResult.getJSONObject(position).get("costo").toString());
                    intent.putExtra("denominacion",taskResult.getJSONObject(position).get("denominacion").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);



            }
        });

    }


    /*
    private class ConnectionProd extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();
            response=cn.connect(ConnectionUtils.getAllProdParameter());


            return response;
        }

    }
    */
}