package com.example.dantesrevelion.mipedido;

import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;

import java.util.Calendar;

public class Producto extends BaseActivity {


    Button minus=null;
    Button addmore=null;
    int n=1;
    String idu="",idp="",c="";
    RelativeLayout layoutLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_producto);
        setSupportActionBar(toolbar);
        TextView txttitulo1=(TextView) findViewById(R.id.txtNombreProd);
        TextView txttitulo2=(TextView) findViewById(R.id.txtTipoProd);
        idu=getIntent().getExtras().getString("id_usuario");
        idp=getIntent().getExtras().getString("id_producto");
        String n=getIntent().getExtras().getString("nombre");
        c=getIntent().getExtras().getString("costo");
        String d=getIntent().getExtras().getString("denominacion");
        txttitulo1.setText(n+" ");
        txttitulo2.setText(d+" "+c+"$");
        System.out.println( "INICIA ON CREATE PRODUCTO");
        minus=(Button) findViewById(R.id.minus);
        addmore=(Button) findViewById(R.id.addmore);
        layoutLoading=(RelativeLayout) findViewById(R.id.layoutLoading);
       // requestUpdate();

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
       // requestUpdate();
        startUpdates(15,1000,null);

    }

    public void onclickMinus(View view){
        if(n>1){
            n--;
        }
        TextView txtCant= (TextView) findViewById(R.id.cantidad);
        System.out.println("CLICK 1"+txtCant.getText());
        txtCant.setText(""+n);
   //     micontador.setText(n);
    }
    public void onclickAddMore(View view){
        n++;

        TextView txtCant= (TextView) findViewById(R.id.cantidad);
        System.out.println("CLICK 2"+txtCant.getText());
        txtCant.setText(""+n);
    //    micontador.setText(n);
    }

    public void addtoCarritoOLD(View v){

        String monto=""+(n*Double.parseDouble(c));
        JSONArray taskResult= ConnectionUtils.consultaSQLite(this,ConnectionUtils.insertCarrito(idp,idu,""+n,monto,"P","",""));
        Toast toast1 = Toast.makeText(getApplicationContext(),
                "Añadidos al Carrito", Toast.LENGTH_SHORT);
        toast1.show();
        onBackPressed();
    }
    public void addtoCarrito(View v){

        if(!isGPSEnabled()){
            Toast.makeText(this, "Activar GPS para realizar la venta", Toast.LENGTH_SHORT).show();
            return ;
        }
        layoutLoading.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        final Runnable run = new Runnable() {
            public void run() {
                //stopUpdate();

                if(currentLocation==null){
                    stopUpdate();
                    requestUpdate();
                    handler.postDelayed(this,2000);
                }else{
                    String monto=""+(n*Double.parseDouble(c));
                    JSONArray taskResult= ConnectionUtils.consultaSQLite(getBaseContext(),ConnectionUtils.insertCarrito(idp,idu,""+n,monto,"P",
                            String.valueOf(currentLocation.getLatitude()),String.valueOf(currentLocation.getLongitude())));
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "Añadidos al Carrito", Toast.LENGTH_SHORT);
                    toast1.show();
                    layoutLoading.setVisibility(View.GONE);
                    onBackPressed();

                }
            }
        };
        handler.postDelayed(run,2000);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopUpdate();
    }
}
