package com.example.focusmate.Sesiones_usuario;

import java.sql.Timestamp;

public class SesionesUsuario {

    private int id;
    private int usuarioId;
    private int metodoId;
    private Timestamp fechaSesion;
    private int duracionMinutos;
    private String tipoTarea;
    private int nivelProductividad;
    private int mediaPulso;
    private int mediaMovimiento;
    private int nivelConcentracion;

    public SesionesUsuario(int nivelProductividad, int metodoId, int id, int usuarioId, Timestamp fechaSesion,
                           int duracionMinutos, String tipoTarea, int mediaPulso, int nivelConcentracion, int mediaMovimiento) {
        this.nivelProductividad = nivelProductividad;
        this.metodoId = metodoId;
        this.id = id;
        this.usuarioId = usuarioId;
        this.fechaSesion = fechaSesion;
        this.duracionMinutos = duracionMinutos;
        this.tipoTarea = tipoTarea;
        this.mediaPulso = mediaPulso;
        this.nivelConcentracion = nivelConcentracion;
        this.mediaMovimiento = mediaMovimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getMetodoId() {
        return metodoId;
    }

    public void setMetodoId(int metodoId) {
        this.metodoId = metodoId;
    }

    public Timestamp getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(Timestamp fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(String tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public int getNivelProductividad() {
        return nivelProductividad;
    }

    public void setNivelProductividad(int nivelProductividad) {
        this.nivelProductividad = nivelProductividad;
    }

    public int getMediaPulso() {
        return mediaPulso;
    }

    public void setMediaPulso(int mediaPulso) {
        this.mediaPulso = mediaPulso;
    }

    public int getMediaMovimiento() {
        return mediaMovimiento;
    }

    public void setMediaMovimiento(int mediaMovimiento) {
        this.mediaMovimiento = mediaMovimiento;
    }

    public int getNivelConcentracion() {
        return nivelConcentracion;
    }

    public void setNivelConcentracion(int nivelConcentracion) {
        this.nivelConcentracion = nivelConcentracion;
    }
}
