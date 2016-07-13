package com.example.dantesrevelion.mipedido;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Producto extends BaseActivity {


    Button minus=null;
    Button addmore=null;
    int n=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_producto);
        TextView txttitulo1=(TextView) findViewById(R.id.txtNombreProd);
        TextView txttitulo2=(TextView) findViewById(R.id.txtTipoProd);
        String n=getIntent().getExtras().getString("nombre");
        String c=getIntent().getExtras().getString("costo");
        String d=getIntent().getExtras().getString("denominacion");
        txttitulo1.setText(n+" ");
        txttitulo2.setText(d+" "+c+"$");
        System.out.println( "INICIA ON CREATE PRODUCTO");
        minus=(Button) findViewById(R.id.minus);
        addmore=(Button) findViewById(R.id.addmore);

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
}
