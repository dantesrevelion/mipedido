package com.example.dantesrevelion.mipedido;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterProducto;
import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterVentaUsuario;
import com.example.dantesrevelion.mipedido.utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class ListaDeVentas extends AppCompatActivity {
    Spinner spinervendedores=null;
    JSONArray taskResult=null;
    JSONArray taskResult2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_ventas);
        String [] array=null;

        try {
            taskResult= (JSONArray) new ConnectionAllUsuarios().execute().get();

            array=new String[taskResult.length()];
            for(int i=0;i<taskResult.length();i++){
                array[i]=taskResult.getJSONObject(i).get("usuario").toString();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinervendedores=(Spinner) findViewById( R.id.spinnerVendedores);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        spinervendedores.setAdapter(adapter);
        spinervendedores.setOnItemSelectedListener(itemselected);


    }

    Spinner.OnItemSelectedListener itemselected= new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            System.out.println("SELECTED--------------------------->");
            int idvendedor=spinervendedores.getSelectedItemPosition();
            final ListView listaVendidos = (ListView) findViewById(R.id.listaDeVentas);


            try {
                taskResult2= (JSONArray) new ConnectionVentasByUsuario().execute((idvendedor+1)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            VysorAdapterVentaUsuario adapterVendidos = new VysorAdapterVentaUsuario(ListaDeVentas.this,
                    R.layout.item_listaventa,ConnectionUtils.jsonToArray(taskResult2,"nombre"), taskResult2);
            listaVendidos.setAdapter(adapterVendidos);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private class ConnectionAllUsuarios extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();
            response=cn.connect(ConnectionUtils.getAllUsuariosParameter());

            return response;
        }

    }

    private class ConnectionVentasByUsuario extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();
            response=cn.connect(ConnectionUtils.getVentasByUsuarioParameter( param[0].toString()));

            return response;
        }

    }
}
