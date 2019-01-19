package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterVentaUsuario;
import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.VolleyS;
import com.example.dantesrevelion.mipedido.orm.DatosGastos;
import com.example.dantesrevelion.mipedido.orm.DatosVenta;
import com.example.dantesrevelion.mipedido.orm.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaDeVentas extends BaseActivity {
    Spinner spinervendedores=null;
    JSONArray taskResult=null;
    JSONArray taskResult2=null;
    TextView tv_total=null;
    TextView tv_cantidad=null;
    public static boolean searchIsVisibleLista=false;
    public static Activity activityLista;
    static Button imprimitBtn;

    Double totalPrint;
    Activity actividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_ventas);
        String [] array=null;
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        tv_total=(TextView)findViewById(R.id.total_lista_ventas);
        tv_cantidad=(TextView)findViewById(R.id.cantidad_lista_ventas);
        actividad=this;
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();

        imprimitBtn= (Button) findViewById(R.id.bt_imprimir_lista);
        activityLista =this;
        SearchList.flagSerch=false;
       // makeRequest();
       // ConnectionUtils cn=new ConnectionUtils();


        /*
        ConnectionUtils.createConection(getBaseContext());
        List<DatosGastos> listaGastos=new ArrayList<>();
        DatosGastos gastos=new DatosGastos();
        gastos.setCodigo("11111333");
        gastos.setIdv("22222");
        gastos.setMonto("555555");
        gastos.setNombre("Revelion");
        gastos.setParamFecha("2016-10-05 00:21:47");
        gastos.setSession(ConnectionUtils.getSession());
        listaGastos.add(gastos);
        listaGastos.add(gastos);
        listaGastos.add(gastos);
        JSONArray requestGastos=ConnectionUtils.parseBeantoJsonArray(listaGastos);
        makePostRequest(ConnectionUtils.insertGastosPost(),requestGastos);

        ConnectionUtils.createConection(getBaseContext());
        List<DatosVenta> listaVentas=new ArrayList<>();
        DatosVenta ventas=new DatosVenta();
        ventas.setSession(ConnectionUtils.getSession());
        ventas.setMonto("123");
        ventas.setCantidad("223");
        ventas.setFecha("2016-10-05 00:21:47");
        ventas.setIdp("333");
        ventas.setIdv("666");
        listaVentas.add(ventas);
        listaVentas.add(ventas);
        listaVentas.add(ventas);
        listaVentas.add(ventas);
        JSONArray requestVentas=ConnectionUtils.parseBeantoJsonArray(listaVentas);
        makePostRequest(ConnectionUtils.insertVentasPost(),requestVentas);
        */
        //ConnectionUtils.createConection(getBaseContext());
        //makeRequest(ConnectionUtils.getAllVentasParameter());








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

    public List<String> getListToPrint() throws JSONException {
        List<String> registros=new ArrayList<>();
        for (int i = 0; i < taskResult2.length(); i++) {

            //  valuesUp.put("latitude",currentLocation.getLatitude());
            //  valuesUp.put("longitude",currentLocation.getLongitude());
            JSONArray localValue= ConnectionUtils.consultaSQLite(getBaseContext(),"select * from productos where id="+taskResult2.getJSONObject(i).getString("id_producto"));
            //JSONArray localUsuarios= ConnectionUtils.consultaSQLite(getBaseContext(),"select * from usuario where id="+localValue.getJSONObject(i).getString("id_vendedor"));
            String cant=padRight(taskResult2.getJSONObject(i).getString("cantidad"),3);
            String art=padRight(localValue.getJSONObject(0).getString("nombre"),14);
            String cu=padLeft(localValue.getJSONObject(0).getString("costo"),7);
            String totalcu="$"+padLeft(taskResult2.getJSONObject(i).getString("monto"),7);
            String cadena=art+cant+cu+totalcu;
            //String value = new String(cadena., "UTF-8");
            registros.add(cadena);
        }
        registros.add("                                ");
        registros.add("                                ");
        registros.add("       TOTAL            $"+padLeft(String.valueOf(totalPrint),7));
        registros.add("                                ");
        registros.add("Latitud: "+padRight(String.valueOf(currentLocation.getLatitude()),23));
        registros.add("Longitud: "+padRight(String.valueOf(currentLocation.getLongitude()),22));

        return registros;
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }
    public  String padLeft(String str, int n) {
        return String.format("%1$" + n + "s", str);
    }

    public void imprimir(View v){
        System.out.println("--------->Inicia proceso de impresion");
            imprimitBtn.setEnabled(false);

        if(utils.bluetoothIsOn(this)){
            if(utils.isPaired(this)){
                try {
                    if(!utils.isOpen()){
                        utils.openBT(getBaseContext());
                    }


                    try {

                        utils.sendData(getListToPrint(),ConnectionUtils.getUsuarioApp());
                       // generarVenta(v);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        imprimitBtn.setEnabled(true);
                    }
                } catch (IOException e) {
                    //   Toast toast=new Toast(getBaseContext());
                    //   toast.setText("no se pudo imprimir");
                    Toast.makeText(this, "no se pudo imprimir", Toast.LENGTH_SHORT).show();
                    imprimitBtn.setEnabled(true);
                }
            }else{
                utils.searchDevices();
                showSearchListLista();
                imprimitBtn.setEnabled(true);
            }

        }else{
            imprimitBtn.setEnabled(true);
        }
        imprimitBtn.setEnabled(true);
        //  Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // startActivityForResult(enableBluetooth, 0);
    }

    public void showSearchListLista(){
        System.out.println("--------->Muestra lista de dispositivos");
       // switchButtons(false);
        searchIsVisibleLista=true;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SearchList fragment = new SearchList();
        fragmentTransaction.add(R.id.fragment_container_carrito_lista, fragment, "listSearch");
        fragmentTransaction.commit();

    }

    public static void hideSearchListLista(){
        activityLista.getFragmentManager().beginTransaction().remove(activityLista.getFragmentManager().findFragmentById(R.id.fragment_container_carrito_lista)).commit();
        searchIsVisibleLista=false;
        imprimitBtn.setEnabled(true);
        //switchButtons(true);
        // utils.stopSearch();
    }
    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        if(searchIsVisibleLista){
            hideSearchListLista();
            utils.stopSearch();
            try {
                utils.closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            super.onBackPressed();
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

                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String fecha=year+"-"+(month+1)+"-"+day;


                c.add(Calendar.DAY_OF_MONTH, 1);
                int year2 = c.get(Calendar.YEAR);
                int month2 = c.get(Calendar.MONTH);
                int day2 = c.get(Calendar.DAY_OF_MONTH);
                String fecha2=year2+"-"+(month2+1)+"-"+day2;

             //   System.out.println("FECHA LISTA VENTAS " +fecha);
             //   debug(" fecha "+fecha2);
                taskResult2= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.queryVentasByUsuarioFecha(ConnectionUtils.formatDate(fecha),ConnectionUtils.formatDate(fecha2),String.valueOf(id)));
                calculaTotal();
            VysorAdapterVentaUsuario adapterVendidos = new VysorAdapterVentaUsuario(ListaDeVentas.this,
                    R.layout.item_listaventa,ConnectionUtils.jsonToArray(taskResult2,"nombre"), taskResult2);
            listaVendidos.setAdapter(adapterVendidos);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    public void enviarventaClic(View e){
       // bt_enviar.setEnabled(false);
       // Connection c=new Connection();
       // c.execute();



    }
    public void calculaTotal(){
        double total=0d;
        int cantidadTotal=0;
        for(int i=0;i<taskResult2.length();i++){
            try {
                int cantidad=taskResult2.getJSONObject(i).getInt("cantidad");
                Double valor=taskResult2.getJSONObject(i).getDouble("monto");
                total=(valor)+total;
                cantidadTotal=cantidad+cantidadTotal;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        totalPrint=total;
        tv_total.setText("$"+total);
        tv_cantidad.setText(String.valueOf(cantidadTotal));
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

    /*
    public class callCheckIn extends AsyncTask {

        //Button bt_enviar=null;

        @Override
        protected Object doInBackground(Object... param) {


            CheckIn.checkInRunnable(actividad.getBaseContext(),actividad);


            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

                    bt_enviar.setEnabled(true);

        }
    }
    */
}
