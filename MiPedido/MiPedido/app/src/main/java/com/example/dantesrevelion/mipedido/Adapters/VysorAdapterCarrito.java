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

    private boolean checked[];

    //View rowView;


    public boolean[] isChecked(){
        return checked;
    }
    public VysorAdapterCarrito(Context context, int item_vysor, String [] values, JSONArray jsonArray) {
        super(context, -1,values);
        this.context = context;
        this.item_vysor = item_vysor;
        this.values = jsonArray;
        checked=new boolean[jsonArray.length()];
        for(int i=0;i<checked.length;i++){
            checked[i]=false;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        /*
        TextView cantidad = (TextView) rowView.findViewById(R.id.txtcantidadcarrito);
        TextView id = (TextView) rowView.findViewById(R.id.txtidecarrito);
        TextView costo = (TextView) rowView.findViewById(R.id.txtcostodcarrito);
        TextView fecha = (TextView) rowView.findViewById(R.id.txtfechacarrito);
        TextView name = (TextView) rowView.findViewById(R.id.txtnamecarrito);
        holder.check    =(CheckBox)rowView.findViewById(R.id.check) ;
        */
        if(convertView==null){
            convertView = inflater.inflate(item_vysor, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        //convertView.setTag(holder);


        ImageView imageView = (ImageView) convertView.findViewById(R.id.backgroundList);


        System.out.println("        ----------VALUES "+values);

        try {
            JSONArray taskResult= ConnectionUtils.consultaSQLite(getContext(),"select * from productos where id="+values.getJSONObject(position).getString("id_producto"));

            holder.name.setText(taskResult.getJSONObject(0).getString("nombre"));
            holder.id.setText(values.getJSONObject(position).getString("id_venta"));
            holder.cantidad.setText(values.getJSONObject(position).getString("cantidad"));
            holder.costo.setText("$ "+taskResult.getJSONObject(0).getString("costo"));
            holder.fecha.setText(values.getJSONObject(position).getString("fecha"));

            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checked[position]=b;
                }
            });
            holder.check.setChecked(checked[position]);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return convertView;
    }

    static class ViewHolder {
        private CheckBox check;
        TextView cantidad;
        TextView id;
        TextView costo;
        TextView fecha;
        TextView name;

        public ViewHolder(View v){
             cantidad = (TextView) v.findViewById(R.id.txtcantidadcarrito);
             id = (TextView) v.findViewById(R.id.txtidecarrito);
             costo = (TextView) v.findViewById(R.id.txtcostodcarrito);
             fecha = (TextView) v.findViewById(R.id.txtfechacarrito);
             name = (TextView) v.findViewById(R.id.txtnamecarrito);
             check    =(CheckBox)v.findViewById(R.id.check) ;
        }

    }
}
