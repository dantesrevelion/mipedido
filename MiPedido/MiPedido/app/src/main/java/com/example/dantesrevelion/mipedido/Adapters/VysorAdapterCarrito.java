package com.example.dantesrevelion.mipedido.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dantesrevelion.mipedido.R;
import com.example.dantesrevelion.mipedido.Utils.ConnectionUtils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Dantes Revelion on 30/04/2016.
 */
public class VysorAdapterCarrito extends ArrayAdapter<String> {
    private final Context context;
    private final JSONArray values;
    int item_vysor;
    CheckBox check;


    public VysorAdapterCarrito(Context context, int item_vysor, String [] values, JSONArray jsonArray) {
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

        TextView cantidad = (TextView) rowView.findViewById(R.id.txtcantidadcarrito);
        TextView id = (TextView) rowView.findViewById(R.id.txtidecarrito);
        TextView costo = (TextView) rowView.findViewById(R.id.txtcostodcarrito);
        TextView fecha = (TextView) rowView.findViewById(R.id.txtfechacarrito);
        TextView name = (TextView) rowView.findViewById(R.id.txtnamecarrito);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.backgroundList);


        System.out.println("        ----------VALUES "+values);

        try {
            JSONArray taskResult= ConnectionUtils.consultaSQLite(getContext(),"select * from productos where id="+values.getJSONObject(position).getString("id_producto"));

            name.setText(taskResult.getJSONObject(0).getString("nombre"));
            id.setText(values.getJSONObject(position).getString("id_venta"));
            cantidad.setText(values.getJSONObject(position).getString("cantidad"));
            costo.setText("$ "+taskResult.getJSONObject(0).getString("costo"));
            fecha.setText(values.getJSONObject(position).getString("fecha"));


        } catch (JSONException e) {
            e.printStackTrace();
        }




        return rowView;
    }
}
