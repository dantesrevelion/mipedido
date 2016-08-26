package com.example.dantesrevelion.mipedido;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterSearchList;
import com.example.dantesrevelion.mipedido.Utils.BluetoothUtils;

import java.util.ArrayList;
import java.util.List;


public class SearchList extends Fragment {

    public static ListView lista ;
    public static List<String> items;
    public static List<String> adress;
    public static VysorAdapterSearchList adapter;
    public static Context context;
    public static List<BluetoothDevice> devices;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_list, null);
        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items=new ArrayList<>();
        adress=new ArrayList<>();
        devices=new ArrayList<>();
        context=getActivity().getBaseContext();
        initSearchList();

    }

    public void initSearchList(){
        lista = (ListView) getActivity().findViewById(R.id.lista_search_list);
        adapter = new VysorAdapterSearchList(context, R.layout.item_search_list, items,adress);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(itemclick);
    }
    public void setDevices(List<BluetoothDevice> list){
        this.devices=list;
    }

    AdapterView.OnItemClickListener itemclick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("----------> Click on item device"+position+" name "+devices.get(position).getName());
           // BluetoothUtils util=new BluetoothUtils(context);
            BluetoothUtils.pairDevice(devices.get(position));

           // CarritoCompra c=new CarritoCompra();
            CarritoCompra.hideSearchList();
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            System.out.println("------------>IS SEARCHING "+mBluetoothAdapter.isDiscovering());
           // items=new ArrayList<>();
           // adress=new ArrayList<>();
           // devices=new ArrayList<>();
            /**TODO finalizar busqueda cuando se haga clic*/
           // adapter = new VysorAdapterSearchList(context, R.layout.item_search_list, items,adress);
           // lista.setAdapter(adapter);

        }
    };
    public void addToSearchList(String nombre,String adress){
        if(null!=nombre) {
            items.add(nombre);
            this.adress.add(adress);
            adapter = new VysorAdapterSearchList(context, R.layout.item_search_list, items,this.adress);
            lista.setAdapter(adapter);
        }

    }

}
