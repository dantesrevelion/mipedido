package com.example.dantesrevelion.mipedido;

import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterReporte;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ReportePorFecha extends BaseActivity {

    private static int idfecha=0;
    private static TextView fechaini=null;
    private static TextView fechafin=null;
    public static String fecha1,fecha2;
    public Spinner spinervendedores;
    JSONArray taskResult=null;
    JSONArray taskResult2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_por_fecha);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        fechaini=(TextView) findViewById(R.id.txtInicio);
        fechafin=(TextView) findViewById(R.id.txtFin);

        String [] array=null;

        try {
            taskResult= ConnectionUtils.consultaSQLite(this,ConnectionUtils.queryAllUsuarios());

            array=new String[taskResult.length()];
            for(int i=0;i<taskResult.length();i++){
                array[i]=taskResult.getJSONObject(i).get("usuario").toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinervendedores=(Spinner) findViewById( R.id.spinnerVendedores);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        spinervendedores.setAdapter(adapter);



    }

    public void onclickFechaInicio(View view){
        DialogFragment newFragment = new DatePickerFragment();
        idfecha=view.getId();
        newFragment.show(getSupportFragmentManager(), "Fecha de Inicio");

    }

    public void onclickFechaFin(View view){
        DialogFragment newFragment = new DatePickerFragment();
        idfecha=view.getId();
        newFragment.show(getSupportFragmentManager(), "Fecha Fin");
    }

    public static void setDate(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, 1);
        d = c.getTime();
        String fecha="";


        if(ReportePorFecha.idfecha==R.id.fechaInicio){
             fecha=d.getYear()+"-"+d.getMonth()+"-"+d.getDate();
             ReportePorFecha.fechaini.setText(""+fecha);
             fecha1=fecha;
        }
        if(ReportePorFecha.idfecha==R.id.fechaFin){
            c.setTime(d);
            c.add(Calendar.DATE, 1);
            d = c.getTime();
            fecha=d.getYear()+"-"+d.getMonth()+"-"+d.getDate();
            ReportePorFecha.fechafin.setText(""+fecha);
            fecha2=fecha;
        }
        System.out.println("MI FECHA PICKEADA "+fecha);


    }
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

    private class ConnectionVentasByUsuarioFecha extends AsyncTask {

        @Override
        protected Object doInBackground(Object... param) {

            JSONArray response=null;
            ConnectionUtils cn=new ConnectionUtils();
            response=cn.connect(ConnectionUtils.getVentasByUsuarioFechaParameter(param[0].toString(),param[1].toString(),param[2].toString()));

            return response;
        }

    }
*/
    public void OnClickFillVentas(View v){

        Toast toast1 = Toast.makeText(getApplicationContext(),
                "Seleccione fechas", Toast.LENGTH_SHORT);
        int idvendedor=spinervendedores.getSelectedItemPosition();
        System.out.println("id vendedor ------------->"+idvendedor);

        if(fecha1!=null && fecha2!=null) {
            if (!fecha1.trim().equals("") && !fecha2.trim().equals("")) {
                String f1=ConnectionUtils.formatDate(fecha1);
                String f2=ConnectionUtils.formatDate(fecha2);

                final ListView listaVendidos = (ListView) findViewById(R.id.listaVentaPorFecha);


                    taskResult2 = taskResult= ConnectionUtils.consultaSQLite(this,ConnectionUtils.queryVentasByUsuarioFecha(f1,f2,""+(idvendedor+1)));

                VysorAdapterReporte adapterVendidos = new VysorAdapterReporte(ReportePorFecha.this,
                        R.layout.item_producto, ConnectionUtils.jsonToArray(taskResult2, "nombre"), taskResult2);
                listaVendidos.setAdapter(adapterVendidos);

                calculaTotal();
            } else {

                toast1.show();
            }
        }else{
            toast1.show();
        }
        fecha1=fecha2="";
    }

    public void calculaTotal(){
        double monto=0;
       for (int i=0;i<taskResult2.length();i++){
           try {
               int costo= taskResult2.getJSONObject(i).getInt("costo");
               int cantidad= taskResult2.getJSONObject(i).getInt("cantidad");
               monto=monto+(costo*cantidad);
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }
        System.out.println("TOTAL------------->"+monto);
    }
}
