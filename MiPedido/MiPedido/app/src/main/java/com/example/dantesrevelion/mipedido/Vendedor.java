package com.example.dantesrevelion.mipedido;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Vendedor extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_vendedor);
        String array[]=new String[]{"vendedor 1","vendedor 2","vendedor 3"};
        Spinner vendedores=(Spinner) findViewById( R.id.spinnerVendedores);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        vendedores.setAdapter(adapter);


        final ListView listaVendidos = (ListView) findViewById(R.id.listaVenta);
        final String[] vendidos ={"Producto 1","Producto 2","Producto 3","Producto 4","Producto 5","Producto 6","Producto 7","Producto 8","Producto 9"};
        VysorAdapterMenu adapterVendidos = new VysorAdapterMenu(Vendedor.this, R.layout.item_prodvendido, vendidos);
        listaVendidos.setAdapter(adapterVendidos);

    }
}
