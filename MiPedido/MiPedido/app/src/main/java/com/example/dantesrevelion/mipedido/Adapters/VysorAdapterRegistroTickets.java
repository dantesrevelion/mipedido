package com.example.dantesrevelion.mipedido.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dantesrevelion.mipedido.R;
import com.example.dantesrevelion.mipedido.RegistroTickets;
import com.example.dantesrevelion.mipedido.Utils.ViewUtils;

import java.util.List;

/**
 * Created by Dantes Revelion on 30/04/2016.
 */
public class VysorAdapterRegistroTickets extends ArrayAdapter<String> implements View.OnFocusChangeListener {
    private final Context context;
    private final List<String> values;
    public static Double sumaMonto=0.0;
    int item_vysor;
    int position;
    public VysorAdapterRegistroTickets(Context context, int item_vysor, List<String> values) {
        super(context, -1, values);
        this.context = context;
        this.item_vysor = item_vysor;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(item_vysor, parent, false);
        TextView numero = (TextView) rowView.findViewById(R.id.tv_numero_item_reg);
        EditText monto = (EditText) rowView.findViewById(R.id.et_monto_item_reg);
        this.position=position;

        monto.setOnFocusChangeListener(this);
        numero.setText(values.get(position));
        /*
        TextView name = (TextView) rowView.findViewById(R.id.txtName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.backgroundList);
        //   LinearLayout rlayout=(LinearLayout)rowView.findViewById(R.id.mylayout) ;
        name.setText(values[position]);
        */
        return rowView;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        System.out.println("FOCUS CHANGE-------->");
        switch (v.getId()){
            case R.id.et_monto_item_reg:
                sumaMonto=0.0d;
               for (int i=0;i<values.size();i++){
                   EditText ed_monto=(EditText) ViewUtils.getViewByPosition((i),RegistroTickets.lista).findViewById(R.id.et_monto_item_reg);
                   if(ed_monto.length()>0) {
                       Double monto = Double.parseDouble(ed_monto.getText().toString());
                       sumaMonto = sumaMonto + monto;
                       RegistroTickets.tv_total.setText("$"+sumaMonto);

                   }
               }   System.out.println("valores----->" + sumaMonto);

                break;
        }

    }

    /**end of the class*/
}