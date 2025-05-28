package com.example.focusmate.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.focusmate.CountdownTimerActivity;
import com.example.focusmate.R;
import com.example.focusmate.Session.SessionManager;
import com.example.focusmate.Session.SessionResponse;

public class SessionsFragment extends Fragment implements SessionManager.SessionCallback {
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);

        sessionManager = new SessionManager(this);

        Button testButton = view.findViewById(R.id.test_button);
        testButton.setOnClickListener(v -> testCreateSession());

        Button timerButton = view.findViewById(R.id.btn_timer);
        timerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CountdownTimerActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void testCreateSession() {
        sessionManager.createStudySession(1, 4, 90, "prueba", 4);
    }

    @Override
    public void onSessionCreated(SessionResponse response) {
        Toast.makeText(getContext(), "¡Sesión creada exitosamente!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSessionError(String error) {
        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sessionManager != null) {
            sessionManager.destroy();
        }
    }
}