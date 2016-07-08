package com.example.dantesrevelion.mipedido.Utils;

import android.os.AsyncTask;

import com.example.dantesrevelion.mipedido.Beans.BeanResponses;

import org.json.JSONArray;

/**
 * Created by Dantes Revelion on 07/07/2016.
 */
public class ConectionTask extends AsyncTask{
    BeanResponses responses=new BeanResponses();

    @Override
    protected Object doInBackground(Object[] objects) {

        JSONArray response;
        ConnectionUtils cn=new ConnectionUtils();
        //obtiene tabla de usuarios
        response = cn.connect(objects[0].toString());
        responses.setResponseUsuarios(response);
        //obtiene tabla de ventas
        response = cn.connect(objects[0].toString());
        responses.setResponseVenta(response);
        //obtiene tabla de productos
        response = cn.connect(objects[0].toString());
        responses.setResponseProductos(response);

        return "OK";
    }
}

