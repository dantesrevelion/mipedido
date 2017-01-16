package com.example.dantesrevelion.mipedido.orm;

/**
 * Created by Dantes Revelion on 14/01/2017.
 */

public class Usuario {
    private String idUsuario;
    private Session session;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
