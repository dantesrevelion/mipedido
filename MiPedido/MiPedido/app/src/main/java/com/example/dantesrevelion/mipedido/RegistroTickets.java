package com.example.dantesrevelion.mipedido;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterRegistroTickets;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;
import com.example.dantesrevelion.mipedido.Utils.ViewUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistroTickets extends BaseActivity implements View.OnClickListener{

    public static Button menos;
    public static Button mas;
    public static ImageView fecha;
    public static EditText inputFecha;
    public static String fechaCad="";
    public static ListView lista;
    public static TextView tv_total;
    private static Button bt_registrar;

    int n=0;
    List<String> items=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tickets);
        toolbar = (Toolbar) findViewById(R.id.tool_bar_registro);

        menos= (Button)findViewById(R.id.minus_registro);
        mas=(Button)findViewById(R.id.addmore_registro);
        fecha=(ImageView) findViewById(R.id.img_fecha_registro);
        inputFecha=(EditText) findViewById(R.id.fecha_registro) ;
        tv_total=(TextView)findViewById(R.id.total_registro);
        bt_registrar=(Button) findViewById(R.id.registrar_registro);
        lista = (ListView) findViewById(R.id.lista_registros);

        setSupportActionBar(toolbar);
        menos.setOnClickListener(this);
        mas.setOnClickListener(this);
        fecha.setOnClickListener(this);
        bt_registrar.setOnClickListener(this);




    }


    @Override
    public void onClick(View view) {
        TextView txtCant= (TextView) findViewById(R.id.cantidad_registro);
        switch (view.getId()){
            case R.id.addmore_registro:
                n++;
                items.add(""+n);
                txtCant.setText(""+n);
                setLista();
                break;
            case R.id.minus_registro:
                if(n>0){
                    n--;
                    items.remove(n);
                    setLista();
                }

                txtCant.setText(""+n);
                break;
            case R.id.img_fecha_registro:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Fecha Registro");


                break;

            case R.id.registrar_registro:
                for(int i=0;i<n;i++){
                    EditText ed_nombre=(EditText) ViewUtils.getViewByPosition((i),lista).findViewById(R.id.et_nombre_item_reg);
                    EditText ed_monto=(EditText) ViewUtils.getViewByPosition((i),lista).findViewById(R.id.et_monto_item_reg);
                    EditText ed_codigo=(EditText) ViewUtils.getViewByPosition((i),lista).findViewById(R.id.et_codigo_item_reg);
                    debug("Nombre: "+ed_nombre.getText()+" Monto: "+ed_monto.getText()+" Codigo: "+ed_codigo.getText());

                }

                break;
        }
    }

    public void setLista(){

        VysorAdapterRegistroTickets adapter = new VysorAdapterRegistroTickets(RegistroTickets.this, R.layout.item_registro_tickets, items);
        lista.setAdapter(adapter);


    }
    public static String getFechaCad() {
        return fechaCad;
    }

    public static void setFechaCad(Date fecha) {
        fechaCad=fecha.getYear()+"-"+fecha.getMonth()+"-"+fecha.getDate();
        inputFecha.setText("+ "+fechaCad);
    }


}
