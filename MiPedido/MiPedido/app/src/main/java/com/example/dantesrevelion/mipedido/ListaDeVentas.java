package com.example.dantesrevelion.mipedido;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterVentaUsuario;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

public class ListaDeVentas extends BaseActivity {
    Spinner spinervendedores=null;
    JSONArray taskResult=null;
    JSONArray taskResult2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_ventas);
        String [] array=null;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        try {
            taskResult= ConnectionUtils.consultaSQLite(this,ConnectionUtils.queryAllUsuarios());
            JSONArray filterResult =new JSONArray();
            for(int n=0;n<taskResult.length(); n++){
                if(ConnectionUtils.getUsuarioApp().equals("Administrador")){
                    filterResult.put(taskResult.getJSONObject(n));
                }else{
                    if(taskResult.getJSONObject(n).get("usuario").equals(ConnectionUtils.getUsuarioApp())){
                        filterResult.put(taskResult.getJSONObject(n));
                    }
                }

            }
            array=new String[filterResult.length()];
            for(int i=0;i<filterResult.length();i++){
                array[i]=filterResult.getJSONObject(i).get("usuario").toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinervendedores=(Spinner) findViewById( R.id.spinnerVendedores);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, array);
        spinervendedores.setAdapter(adapter);
        spinervendedores.setOnItemSelectedListener(itemselected);


        for(int i=0;i<array.length;i++){
            if(array[i].equals(getIntent().getExtras().getString("usuario"))){
                spinervendedores.setSelection(i);
                break;
            }

        }


    }

    Spinner.OnItemSelectedListener itemselected= new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



            JSONArray datosUsuario= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryDatosDeUsuario(spinervendedores.getSelectedItem().toString()));
            int id=0;
            try {
                id=Integer.parseInt(datosUsuario.getJSONObject(0).getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final ListView listaVendidos = (ListView) findViewById(R.id.listaDeVentas);



                taskResult2= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryVentasByUsuario(""+id));

            VysorAdapterVentaUsuario adapterVendidos = new VysorAdapterVentaUsuario(ListaDeVentas.this,
                    R.layout.item_listaventa,ConnectionUtils.jsonToArray(taskResult2,"nombre"), taskResult2);
            listaVendidos.setAdapter(adapterVendidos);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    /*
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
    */
}
