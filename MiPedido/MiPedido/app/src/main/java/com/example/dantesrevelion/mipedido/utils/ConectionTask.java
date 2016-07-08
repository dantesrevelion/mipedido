package com.example.dantesrevelion.mipedido.utils;

import android.os.AsyncTask;

import org.json.JSONArray;

/**
 * Created by Dantes Revelion on 07/07/2016.
 */
public class ConectionTask extends AsyncTask{
    JSONArray response=null;
    @Override
    protected Object doInBackground(Object[] objects) {
        // parametros: URL ,
        JSONArray response=null;
        ConnectionUtils cn=new ConnectionUtils();
        response = cn.connect(objects[0].toString());
        return null;
    }
}

