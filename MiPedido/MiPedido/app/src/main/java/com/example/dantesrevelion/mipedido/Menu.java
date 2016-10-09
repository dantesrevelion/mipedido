package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.content.Intent;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.VolleyS;

public class Menu extends BaseActivity {
  //  JSONArray jsonArray;

    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ListView lista = (ListView) findViewById(R.id.listaMenu);
        final String[] items ={"Productos","Lista de Ventas","Gastos Operativos","Reporte por Fecha","Mi Perfil"};
        VysorAdapterMenu adapter = new VysorAdapterMenu(Menu.this, R.layout.item_list, items);
        activity=this;

      //  new Connection().execute();
        ConnectionUtils.setUsuarioApp(getIntent().getExtras().getString("usuario"));
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent intent=new Intent(Menu.this, Lista_Producto.class);
                    intent.putExtra("id_usuario",getIntent().getExtras().getString("id"));
                    startActivity(intent);
                } else if(position == 1) {
                    Intent intent=new Intent(Menu.this, ListaDeVentas.class);
                    intent.putExtra("usuario",getIntent().getExtras().getString("usuario"));
                    startActivity(intent);

                } else if(position == 2) {
                    Intent intent=new Intent(Menu.this,RegistroTickets.class);
                    intent.putExtra("id",getIntent().getExtras().getString("id"));
                    startActivity(intent);

                } else if(position == 3) {
                    Intent intent=new Intent(Menu.this, ReportePorFecha.class);
                    intent.putExtra("usuario",getIntent().getExtras().getString("usuario"));
                    startActivity(intent);
                }else if(position==4){
                    Intent intent=new Intent(Menu.this, Perfil.class);
                    intent.putExtra("usuario",getIntent().getExtras().getString("usuario"));
                    intent.putExtra("correo",getIntent().getExtras().getString("correo"));
                    startActivity(intent);
                }


            }
        });
    }

    @Override
    public  void onBackPressed(){

    }


    /*
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.mipedido_menu, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart) {
            System.out.println("clicked cartttt");

            Intent intent=new Intent(Menu.this,CarritoCompra.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
           // connect();
           // sendGet();
            ConnectionUtils cn=new ConnectionUtils();
            jsonArray=cn.connect(ConnectionUtils.ALL_PROD);
            System.out.println("RESULT "+jsonArray);
            return null;
        }

    }
    */

}
