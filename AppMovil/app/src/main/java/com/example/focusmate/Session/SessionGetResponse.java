package com.example.focusmate.Session;

import java.util.List;

public class SessionGetResponse {
    private List<Session> sesiones;

    public SessionGetResponse() {}

    public List<Session> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Session> sesiones) {
        this.sesiones = sesiones;
    }
}