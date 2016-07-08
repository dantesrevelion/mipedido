package com.example.dantesrevelion.mipedido.Beans;

import org.json.JSONArray;

/**
 * Created by pmruiz on 08/07/2016.
 */
public class BeanResponses {


    private JSONArray responseUsuarios;
    private JSONArray responseVenta;
    private JSONArray responseProductos;

    public JSONArray getResponseUsuarios() {
        return responseUsuarios;
    }

    public void setResponseUsuarios(JSONArray responseUsuarios) {
        this.responseUsuarios = responseUsuarios;
    }

    public JSONArray getResponseVenta() {
        return responseVenta;
    }

    public void setResponseVenta(JSONArray responseVenta) {
        this.responseVenta = responseVenta;
    }

    public JSONArray getResponseProductos() {
        return responseProductos;
    }

    public void setResponseProductos(JSONArray responseProductos) {
        this.responseProductos = responseProductos;
    }
}
