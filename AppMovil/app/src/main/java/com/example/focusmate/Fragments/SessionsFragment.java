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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.R;
import com.example.focusmate.Session.Session;
import com.example.focusmate.Session.SessionAdapter;
import com.example.focusmate.Session.SessionConfigActivity;
import com.example.focusmate.Session.SessionManager;
import com.example.focusmate.Session.SessionPostResponse;

import java.util.List;

public class SessionsFragment extends Fragment implements SessionManager.SessionCallback {

    private RecyclerView recyclerView;
    private SessionAdapter adapter;
    private ProgressBar progressBar;
    private TextView textViewEmpty;
    private Button btnTimer;
    private Button btnRefresh;
    private SessionManager sessionManager;
    private Handler mainHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);
        initViews(view);
        setupListeners();
        loadSessions();
        return view;
    }

    private void initViews(View view) {
        sessionManager = new SessionManager(this, getContext()); // Pasar contexto
        mainHandler = new Handler(Looper.getMainLooper());

        // Usar los IDs correctos del layout
        recyclerView = view.findViewById(R.id.rv_sessions);
        progressBar = view.findViewById(R.id.progress_bar);
        textViewEmpty = view.findViewById(R.id.text_view_empty);
        btnTimer = view.findViewById(R.id.btn_timer);
        btnRefresh = view.findViewById(R.id.btn_refresh);

        adapter = new SessionAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnTimer.setOnClickListener(v -> {
            // Navegar a SessionConfigActivity para crear nueva sesión
            Intent intent = new Intent(getActivity(), SessionConfigActivity.class);
            startActivity(intent);
        });

        btnRefresh.setOnClickListener(v -> {
            loadSessions();
        });
    }

    private void loadSessions() {
        progressBar.setVisibility(View.VISIBLE);
        textViewEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Usar el nuevo método sin parámetros
        sessionManager.getUserSessions();
    }

    @Override
    public void onSessionsLoaded(List<Session> sessions) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            
            if (sessions.isEmpty()) {
                textViewEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                textViewEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setSessions(sessions);
            }
        });
    }

    @Override
    public void onSessionError(String error) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            textViewEmpty.setText("Error al cargar sesiones: " + error);
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        });
    }

    @Override
    public void onSessionCreated(SessionPostResponse response) {
        // Este método puede quedar vacío o manejar respuestas de creación si es necesario
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refrescar al volver al fragmento (por ejemplo, después de crear una sesión)
        loadSessions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sessionManager != null) {
            sessionManager.destroy();
        }
    }
}