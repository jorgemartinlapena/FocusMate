package com.example.focusmate.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.R;
import com.example.focusmate.Session.Session;
import com.example.focusmate.Session.SessionManager;
import com.example.focusmate.Session.SessionPostResponse;
import com.example.focusmate.Session.SessionConfigActivity;
import com.example.focusmate.Session.SessionAdapter;

import java.util.List;

public class SessionsFragment extends Fragment implements SessionManager.SessionCallback {
    private static final int SESSION_CONFIG_REQUEST = 1001;

    private SessionManager sessionManager;
    private RecyclerView recyclerViewSessions;
    private SessionAdapter sessionsAdapter;
    private ProgressBar progressBar;
    private Handler mainHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);

        initViews(view);
        setupRecyclerView();
        loadUserSessions();

        return view;
    }

    private void initViews(View view) {
        sessionManager = new SessionManager(this);
        mainHandler = new Handler(Looper.getMainLooper());

        Button refreshButton = view.findViewById(R.id.btn_refresh);
        refreshButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Actualizando sesiones...", Toast.LENGTH_SHORT).show();
            loadUserSessions();
        });

        Button timerButton = view.findViewById(R.id.btn_timer);
        timerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SessionConfigActivity.class);
            startActivityForResult(intent, SESSION_CONFIG_REQUEST);
        });

        recyclerViewSessions = view.findViewById(R.id.rv_sessions);
        progressBar = view.findViewById(R.id.progress_bar_sessions);
    }

    private void setupRecyclerView() {
        sessionsAdapter = new SessionAdapter();
        recyclerViewSessions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSessions.setAdapter(sessionsAdapter);

        sessionsAdapter.setOnSessionClickListener(session -> {
            Toast.makeText(getContext(),
                    "Sesión del " + session.getSession_timestamp() + " - " + session.getDuration_minutes() + " min",
                    Toast.LENGTH_SHORT).show();

        });
    }

    private void loadUserSessions() {
        progressBar.setVisibility(View.VISIBLE);
        int userId = 1;
        sessionManager.getUserSessions(userId);
    }
    @Override
    public void onSessionCreated(SessionPostResponse response) {
        Toast.makeText(getContext(), "¡Sesión creada exitosamente!", Toast.LENGTH_SHORT).show();
        loadUserSessions();
    }

    @Override
    public void onSessionError(String error) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onSessionsLoaded(List<Session> sessions) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            if (sessions != null && !sessions.isEmpty()) {
                sessionsAdapter.setSessions(sessions);
            } else {
                sessionsAdapter.setSessions(sessions != null ? sessions : new java.util.ArrayList<>());
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SESSION_CONFIG_REQUEST) {
            loadUserSessions();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserSessions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sessionManager != null) {
            sessionManager.destroy();
        }
    }
}