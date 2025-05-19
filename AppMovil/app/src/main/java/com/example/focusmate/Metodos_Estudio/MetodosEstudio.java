package com.example.focusmate.Metodos_Estudio;

public class MetodosEstudio {
    private int id;
    private String nombre;
    private int repeticiones;
    private int tiempoEstudio;
    private int tiempoDescanso;
    private int tiempoDescansoFinal;
    private String descripcion;
    private int tiempoTotalEstudio;

    public MetodosEstudio(int id, int repeticiones, String nombre, int tiempoEstudio, int tiempoDescanso,
                   int tiempoDescansoFinal, String descripcion, int tiempoTotalEstudio) {
        this.id = id;
        this.repeticiones = repeticiones;
        this.nombre = nombre;
        this.tiempoEstudio = tiempoEstudio;
        this.tiempoDescanso = tiempoDescanso;
        this.tiempoDescansoFinal = tiempoDescansoFinal;
        this.descripcion = descripcion;
        this.tiempoTotalEstudio = tiempoTotalEstudio;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public int getTiempoEstudio() {
        return tiempoEstudio;
    }

    public int getTiempoDescansoFinal() {
        return tiempoDescansoFinal;
    }

    public int getTiempoDescanso() {
        return tiempoDescanso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTiempoTotalEstudio() {
        return tiempoTotalEstudio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTiempoEstudio(int tiempoEstudio) {
        this.tiempoEstudio = tiempoEstudio;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public void setTiempoDescanso(int tiempoDescanso) {
        this.tiempoDescanso = tiempoDescanso;
    }

    public void setTiempoDescansoFinal(int tiempoDescansoFinal) {
        this.tiempoDescansoFinal = tiempoDescansoFinal;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTiempoTotalEstudio(int tiempoTotalEstudio) {
        this.tiempoTotalEstudio = tiempoTotalEstudio;
    }
}
