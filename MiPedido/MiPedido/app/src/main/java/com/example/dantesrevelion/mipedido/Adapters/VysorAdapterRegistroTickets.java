package com.example.dantesrevelion.mipedido.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dantesrevelion.mipedido.R;

import java.util.List;

/**
 * Created by Dantes Revelion on 30/04/2016.
 */
public class VysorAdapterRegistroTickets extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    int item_vysor;
    public VysorAdapterRegistroTickets(Context context, int item_vysor, List<String> values) {
        super(context, -1, values);
        this.context = context;
        this.item_vysor = item_vysor;
        System.out.println("derpingjoasdas "+values.size());
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(item_vysor, parent, false);
        TextView numero = (TextView) rowView.findViewById(R.id.tv_numero_item_reg);
        numero.setText(values.get(position));
        /*
        TextView name = (TextView) rowView.findViewById(R.id.txtName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.backgroundList);
        //   LinearLayout rlayout=(LinearLayout)rowView.findViewById(R.id.mylayout) ;
        name.setText(values[position]);
        */
        return rowView;
    }
}
