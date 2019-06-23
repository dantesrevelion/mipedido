package com.example.dantesrevelion.mipedido;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterVentaUsuario;
import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.VolleyS;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;


import org.json.JSONArray;
import org.json.JSONException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaDeVentas extends BaseActivity {
    RelativeLayout loadingScreen;
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
        loadingScreen=(RelativeLayout) findViewById(R.id.layoutLoadingVentas);

        actividad=this;
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();

        imprimitBtn= (Button) findViewById(R.id.bt_imprimir_lista);
        activityLista =this;
        SearchList.flagSerch=false;




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
        if(taskResult2.length()==0){
            return registros;
        }
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



    final Handler handlerImprimir = new Handler();
    int counterTrys=0;
    public void imprimir(final View v){
        Answers.getInstance().logCustom(new CustomEvent("Inicia pronceso Imprimir "));
        System.out.println("--------->Inicia proceso de impresion");
        imprimitBtn.setEnabled(false);
        loadingScreen.setVisibility(View.VISIBLE);
        counterTrys=0;
        if(utils.bluetoothIsOn(this)){

            if(!BluetoothUtils.alreadyConected){
                utils.searchDevices();
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            final String printerNameConfig=preferences.getString("config_printer","");

            handlerImprimir.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean flagOK=false;

                    for(BluetoothDevice deviceLocal:BluetoothUtils.devices){
                        Answers.getInstance().logCustom(new CustomEvent("Bandera de conexion").putCustomAttribute("Conected Flag","Flag: "+BluetoothUtils.printerConected));

                        if(printerNameConfig.equals(deviceLocal.getName()) ){
                            Answers.getInstance().logCustom(new CustomEvent("Encontro la impresora").putCustomAttribute("device","device: "+deviceLocal.getAddress()));
                            Log.d("Printing Progres", ">>>>>>>>>>>>>>Encontro la impresora "+deviceLocal.getName());
                            try {
                                if(getListToPrint().size()==0){
                                    Log.d("Printing Progres", ">>>>>>>>>>>>>>NO HAY ELEMENTOS PARA IMPRIMIR");
                                    return;
                                }
                                utils.openBT(getBaseContext(),deviceLocal);
                                utils.sendData(getListToPrint(),ConnectionUtils.getUsuarioApp());
                               // generarVenta(v);
                                Log.d("Printing Progres", ">>>>>>>>>>>>>>Venta Generada");
                                flagOK=true;
                                utils.closeBT();
                                imprimitBtn.setEnabled(true);
                                loadingScreen.setVisibility(View.GONE);
                                break;


                            } catch (IOException e) {
                                Log.d("Printing Progres", ">>>>>>>>>>>>>>No se realizo la conexion");
                                // Toast.makeText(getBaseContext(), "no se pudo imprimir", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                Answers.getInstance().logCustom(new CustomEvent("No se realizo la conexion ").putCustomAttribute("Error",e.getMessage()));
                                imprimitBtn.setEnabled(true);
                                loadingScreen.setVisibility(View.GONE);
                                break;
                            } catch (JSONException e) {
                                Log.d("Printing Progres", ">>>>>>>>>>>>>>No se realizo la conexion");
                                Answers.getInstance().logCustom(new CustomEvent("No se realizo la conexion ").putCustomAttribute("Error",e.getMessage()));
                                // Toast.makeText(getBaseContext(), "no se pudo imprimir", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                imprimitBtn.setEnabled(true);
                                loadingScreen.setVisibility(View.GONE);
                                break;
                            }


                        }
                    }


                    if( !flagOK && counterTrys<4){
                        Log.d("Printing Progres", ">>>>>>>>>>>>>>Busca en la lista de nuevo");
                        handlerImprimir.postDelayed(this,2000);
                    }else if(!flagOK){
                        Toast.makeText(getBaseContext(), "Impresora no disponible", Toast.LENGTH_SHORT).show();
                        imprimitBtn.setEnabled(true);
                        loadingScreen.setVisibility(View.GONE);
                    }
                    counterTrys++;

                }
            }, 3000);



        }else{
            imprimitBtn.setEnabled(true);
            loadingScreen.setVisibility(View.GONE);
        }

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





}
