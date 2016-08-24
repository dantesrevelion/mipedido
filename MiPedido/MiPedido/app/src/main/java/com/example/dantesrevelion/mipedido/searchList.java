package com.example.dantesrevelion.mipedido;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dantesrevelion.mipedido.Adapters.VysorAdapterSearchList;

import java.util.ArrayList;
import java.util.List;


public class SearchList extends Fragment {

    public static ListView lista ;
    public static List<String> items;
    public static VysorAdapterSearchList adapter;
    public static Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_list, null);
        items=new ArrayList<>();
        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items=new ArrayList<>();
        context=getActivity().getBaseContext();
        initSearchList();

    }

    public void initSearchList(){
        lista = (ListView) getActivity().findViewById(R.id.lista_search_list);
        adapter = new VysorAdapterSearchList(context, R.layout.item_search_list, items);
        lista.setAdapter(adapter);
    }

    public void addToSearchList(String nombre){
        if(null!=nombre) {
            items.add(nombre);
            adapter = new VysorAdapterSearchList(context, R.layout.item_search_list, items);
            lista.setAdapter(adapter);
        }

    }

}
