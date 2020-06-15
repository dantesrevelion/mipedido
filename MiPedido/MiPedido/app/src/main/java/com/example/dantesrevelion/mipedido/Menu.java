package com.example.dantesrevelion.mipedido;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.orm.PositionData;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Menu extends BaseActivity {
  //  JSONArray jsonArray;

   // LocationManager mlocManager;
   // MyLocationListener mlocListener;
  //  Location currentLocation;
    Handler handlerStart;
    Handler handlerStop;
    Runnable runStart;
    boolean running=false;
    RelativeLayout layoutLoading;
    final public static int MY_PERMISSIONS_REQUEST_LOCATION =423;

    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        layoutLoading=(RelativeLayout) findViewById(R.id.layoutLoading);
        utils= new BluetoothUtils(getBaseContext());
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        this.registerReceiver(mReceiver, filter);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        layoutLoading.setVisibility(View.VISIBLE);
        startUpdates(18,1000,layoutLoading);
        /*
        requestUpdate();



        handlerStop = new Handler();
        final Runnable runStop = new Runnable() {
            public void run() {
                stopUpdate();
                if(currentLocation==null) {
                    handlerStart.postDelayed(runStart, 500);
                }else{
                    startUpdates(15,1000,layoutLoading);
                }
            }
        };


        handlerStart = new Handler();

        runStart = new Runnable() {
            public void run() {
                requestUpdate();
                running=true;
                handlerStop.postDelayed(runStop, 3000);
            }
        };

        if(!running) {
            boolean test1=ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
            boolean test2=ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
            if (test1 && test2) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }else{
                handlerStart.postDelayed(runStart, 1000);
            }

            //
        }
*/

        //requestUpdate();






    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    handlerStart.postDelayed(runStart, 5000);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /*
    private void requestUpdate(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("resquest");
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mlocListener);
       // System.out.println(" LAT LONG " + MyLocationListener.getLocation());
        if (MyLocationListener.getLocation() != null) {
            System.out.println(" LAT LONG " + MyLocationListener.getLocation());
            currentLocation= MyLocationListener.getLocation();
            //Toast toast = Toast.makeText(getBaseContext(), "> "+MyLocationListener.getLocation(), Toast.LENGTH_SHORT);
            //toast.show();
            sendLocation(String.valueOf(currentLocation.getLatitude()),String.valueOf(currentLocation.getLongitude()));
        }
    }
    */
    /*
    private void stopUpdate(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocManager.removeUpdates(mlocListener);
    }
    */

    private void sendLocation(String lat,String longt){
        ConnectionUtils.createConection(getBaseContext());
        List<PositionData> listaLocation=new ArrayList<>();
        PositionData positionData=new PositionData();
        positionData.setIdUsuario(getIntent().getExtras().getString("id"));
        positionData.setLatitude(lat);
        positionData.setLongitude(longt);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String fecha=year+"-"+(month+1)+"-"+day+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        String formated=ConnectionUtils.formatDateGeneral(fecha,"yyyy-MM-dd HH:mm:ss");
        positionData.setFecha(formated);
        positionData.setSession(ConnectionUtils.getSession());
        listaLocation.add(positionData);

        JSONArray requestLocation=ConnectionUtils.parseBeantoJsonArray(listaLocation);
        makePostLocation(ConnectionUtils.getLocationURL(),requestLocation);
    }

    @Override
    public  void onBackPressed(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdate();
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
