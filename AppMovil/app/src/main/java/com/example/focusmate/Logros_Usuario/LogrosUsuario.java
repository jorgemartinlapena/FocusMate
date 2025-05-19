package com.example.focusmate.Logros_Usuario;

import java.sql.Timestamp;

public class LogrosUsuario {
    private int id;
    private int usuarioId;
    private String logroId;
    private Timestamp tiempoDesbloqueo;

    public LogrosUsuario(int id, int usuarioId, String logroId, Timestamp tiempoDesbloqueo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.logroId = logroId;
        this.tiempoDesbloqueo = tiempoDesbloqueo;
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

    public String getLogroId() {
        return logroId;
    }

    public void setLogroId(String logroId) {
        this.logroId = logroId;
    }

    public Timestamp getTiempoDesbloqueo() {
        return tiempoDesbloqueo;
    }

    public void setTiempoDesbloqueo(Timestamp tiempoDesbloqueo) {
        this.tiempoDesbloqueo = tiempoDesbloqueo;
    }
}
