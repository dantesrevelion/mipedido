package com.example.dantesrevelion.mipedido.orm;

/**
 * Created by Dantes Revelion on 16/10/2016.
 */

public class DatosGastos {
    private String idv;
    private String nombre;
    private String codigo;
    private String monto;
    private String paramFecha;

    public String getIdv() {
        return idv;
    }

    public void setIdv(String idv) {
        this.idv = idv;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getParamFecha() {
        return paramFecha;
    }

    public void setParamFecha(String paramFecha) {
        this.paramFecha = paramFecha;
    }
}
