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
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ReportePorFecha extends BaseActivity {

    private static int idfecha=0;
    private static TextView fechaini=null;
    private static TextView fechafin=null;
    private static TextView total=null;
    private static TextView tv_gastos=null;
    public static String fecha1,fecha2;
    public static int id=0;
    public Spinner spinervendedores;
    JSONArray taskResult=null;
    JSONArray taskResult2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_por_fecha);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_reporte);
        setSupportActionBar(toolbar);
        fechaini=(TextView) findViewById(R.id.txtInicio);
        fechafin=(TextView) findViewById(R.id.txtFin);
        total=(TextView) findViewById(R.id.total_reporte_fecha);
        tv_gastos=(TextView)findViewById(R.id.tv_gastos_reportefecha) ;
        String [] array=null;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        spinervendedores.setAdapter(adapter);

        for(int i=0;i<array.length;i++){
            if(array[i].equals(getIntent().getExtras().getString("usuario"))){
                spinervendedores.setSelection(i);
                break;
            }

        }



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
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String fecha="";


        if(ReportePorFecha.idfecha==R.id.fechaInicio){
             fecha=year+"-"+(month+1)+"-"+day;
             ReportePorFecha.fechaini.setText(""+fecha);
             fecha1=fecha;
        }
        if(ReportePorFecha.idfecha==R.id.fechaFin){
            c.add(Calendar.DAY_OF_MONTH, 1);
            int year2 = c.get(Calendar.YEAR);
            int month2 = c.get(Calendar.MONTH);
            int day2 = c.get(Calendar.DAY_OF_MONTH);
            fecha=year2+"-"+(month2+1)+"-"+day2;
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
        Toast toastNoData = Toast.makeText(getApplicationContext(),
                "No hay registros", Toast.LENGTH_SHORT);


        JSONArray datosUsuario= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryDatosDeUsuario(spinervendedores.getSelectedItem().toString()));

        try {
            id=Integer.parseInt(datosUsuario.getJSONObject(0).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(fecha1!=null && fecha2!=null) {
            if (!fecha1.trim().equals("") && !fecha2.trim().equals("")) {
                String f1=ConnectionUtils.formatDate(fecha1);
                String f2=ConnectionUtils.formatDate(fecha2);

                final ListView listaVendidos = (ListView) findViewById(R.id.listaVentaPorFecha);


                    taskResult2 = ConnectionUtils.consultaSQLite(this,ConnectionUtils.queryVentasByUsuarioFecha(f1,f2,""+id));

                VysorAdapterReporte adapterVendidos = new VysorAdapterReporte(ReportePorFecha.this,
                        R.layout.item_reportefecha, ConnectionUtils.jsonToArray(taskResult2, "nombre"), taskResult2);
                listaVendidos.setAdapter(adapterVendidos);
                if(taskResult.length()==0){toastNoData.show();}

                Double gastos=calcularGastos(f1,f2);
                calculaTotal(gastos);
            } else {

                toast1.show();
            }
        }else{
            toast1.show();
        }
        fecha1=fecha2="";
    }

    public void calculaTotal(Double gastos){
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
        fechafin.setText("Fecha Inicio");
        fechaini.setText("Fecha Fin");
        total.setText("$"+(monto-gastos));
        System.out.println("TOTAL------------->"+monto);
    }

    public Double calcularGastos(String f1,String f2){
       JSONArray taskResultGastos = ConnectionUtils.consultaSQLite(this,ConnectionUtils.queryMontoGastosByFecha(f1,f2,""+id));
        Double sumaGastos=0d;
        for (int i=0;i<taskResultGastos.length();i++){
            try {
                Double cant=Double.parseDouble(taskResultGastos.getJSONObject(i).getString("monto"));
                sumaGastos=sumaGastos+cant;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tv_gastos.setText("$"+String.valueOf(sumaGastos));
        return sumaGastos;

    }
}

