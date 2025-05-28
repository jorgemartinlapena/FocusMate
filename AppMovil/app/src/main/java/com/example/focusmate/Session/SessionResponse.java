package com.example.focusmate.Session;

public class SessionResponse {
    private String message;
    private boolean success;

    public SessionResponse() {}

    // Getters y setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
