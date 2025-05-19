package com.example.focusmate.Logros;

public class Logros {
    private int id;
    private String nombre;
    private String descripcion;
    private int dias;
    private int minutos;
    private String tipo;

    public Logros(String nombre, int id, String descripcion, int dias, int minutos, String tipo) {
        this.nombre = nombre;
        this.id = id;
        this.descripcion = descripcion;
        this.dias = dias;
        this.minutos = minutos;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
