package com.example.dantesrevelion.mipedido.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dantesrevelion.mipedido.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Dantes Revelion on 30/04/2016.
 */
public class VysorAdapterProducto extends ArrayAdapter<String> {
    private final Context context;
    private final JSONArray values;
    int item_vysor;

    public VysorAdapterProducto(Context context, int item_vysor,String [] values,JSONArray jsonArray) {
        super(context, -1,values);
        this.context = context;
        this.item_vysor = item_vysor;
        this.values = jsonArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(item_vysor, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.txtName);
        TextView tipo = (TextView) rowView.findViewById(R.id.txtTipo);
        TextView precio = (TextView) rowView.findViewById(R.id.txtPrecio);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.backgroundList);

        System.out.println("        ----------VALUES "+values);

        try {
            name.setText(values.getJSONObject(position).getString("nombre"));
            tipo.setText(values.getJSONObject(position).getString("denominacion"));
            precio.setText(values.getJSONObject(position).getString("costo"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}
