package com.example.dantesrevelion.mipedido;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Menu extends BaseActivity {
  //  JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ListView lista = (ListView) findViewById(R.id.listaMenu);
        final String[] items ={"Productos","Lista de Ventas","Registro de Tickets","Reporte por Fecha","Mi Perfil"};
        VysorAdapterMenu adapter = new VysorAdapterMenu(Menu.this, R.layout.item_list, items);


      //  new Connection().execute();

        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent intent=new Intent(Menu.this, Lista_Producto.class);
                    intent.putExtra("id_usuario",getIntent().getExtras().getString("id"));
                    startActivity(intent);
                } else if(position == 1) {
                    startActivity(new Intent(Menu.this, ListaDeVentas.class));

                } else if(position == 2) {
                    startActivity(new Intent(Menu.this, RegistroTickets.class));

                } else if(position == 3) {
                    startActivity(new Intent(Menu.this, ReportePorFecha.class));
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
