package com.example.dantesrevelion.mipedido;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Menu extends AppCompatActivity {
  //  JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final ListView lista = (ListView) findViewById(R.id.listaMenu);
        final String[] items ={"Productos","Mi Perfil","Reporte por Fecha","Lista de Ventas"};
        VysorAdapterMenu adapter = new VysorAdapterMenu(Menu.this, R.layout.item_list, items);


      //  new Connection().execute();

        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    startActivity(new Intent(Menu.this, Lista_Producto.class));
                } else if(position == 1) {

                    Intent intent=new Intent(Menu.this, Perfil.class);
                    intent.putExtra("usuario",getIntent().getExtras().getString("usuario"));
                    intent.putExtra("correo",getIntent().getExtras().getString("correo"));
                    startActivity(intent);


                } else if(position == 2) {
                    startActivity(new Intent(Menu.this, ReportePorFecha.class));
                } else if(position == 3) {
                    startActivity(new Intent(Menu.this, ListaDeVentas.class));
                }else {

                }


            }
        });
    }

    /*
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
