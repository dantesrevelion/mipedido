package com.example.dantesrevelion.mipedido;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class RegistroTickets extends BaseActivity implements View.OnClickListener{

    Button menos;
    Button mas;
    ImageView fecha;
    public static EditText inputFecha;
    public static String fechaCad="";
    int n=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tickets);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_registro);
        setSupportActionBar(toolbar);
        menos= (Button)findViewById(R.id.minus_registro);
        mas=(Button)findViewById(R.id.addmore_registro);
        fecha=(ImageView) findViewById(R.id.img_fecha_registro);
        inputFecha=(EditText) findViewById(R.id.fecha_registro) ;
        menos.setOnClickListener(this);
        mas.setOnClickListener(this);
        fecha.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        TextView txtCant= (TextView) findViewById(R.id.cantidad_registro);
        switch (view.getId()){
            case R.id.addmore_registro:
                n++;

                txtCant.setText(""+n);
                break;
            case R.id.minus_registro:
                if(n>1){
                    n--;
                }
                txtCant.setText(""+n);
                break;
            case R.id.img_fecha_registro:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Fecha Registro");


                break;
        }
    }

    public static String getFechaCad() {
        return fechaCad;
    }

    public static void setFechaCad(Date fecha) {
        fechaCad=fecha.getYear()+"-"+fecha.getMonth()+"-"+fecha.getDate();
        inputFecha.setText("+ "+fechaCad);
    }
}
