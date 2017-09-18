/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author cesar
 */
public class Bitacora {
    private String fecha;
    private String hora;
    private String sql;
    private String usuario;
    private String maquina;

    public Bitacora(String fecha, String hora, String sql, String usuario, String maquina) {
        this.fecha = fecha;
        this.hora = hora;
        this.sql = sql;
        this.usuario = usuario;
        this.maquina = maquina;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMaquina() {
        return maquina;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }
    
    
    
}
