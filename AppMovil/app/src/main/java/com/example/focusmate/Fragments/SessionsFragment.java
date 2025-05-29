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

import com.example.focusmate.CountdownTimerActivity;
import com.example.focusmate.R;
import com.example.focusmate.Session.Session;
import com.example.focusmate.Session.SessionManager;
import com.example.focusmate.Session.SessionPostResponse;
import com.example.focusmate.Session.SessionAdapter;

import java.util.List;

public class SessionsFragment extends Fragment implements SessionManager.SessionCallback {
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

        Button testButton = view.findViewById(R.id.test_button);
        testButton.setOnClickListener(v -> testCreateSession());

        Button timerButton = view.findViewById(R.id.btn_timer);
        timerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CountdownTimerActivity.class);
            startActivity(intent);
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

    private void testCreateSession() {
        sessionManager.createStudySession(1, 4, 90, "prueba", 4);
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
            sessionsAdapter.setSessions(sessions);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sessionManager != null) {
            sessionManager.destroy();
        }
    }

}