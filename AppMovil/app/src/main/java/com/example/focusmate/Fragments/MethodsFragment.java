package com.example.focusmate.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.R;
import com.example.focusmate.StudyMethods.StudyMethod;
import com.example.focusmate.StudyMethods.StudyMethodManager;
import com.example.focusmate.StudyMethods.StudyMethodAdapter;

import java.util.List;

public class MethodsFragment extends Fragment implements StudyMethodManager.StudyMethodsCallback {
    private RecyclerView recyclerView;
    private StudyMethodAdapter adapter;
    private StudyMethodManager methodManager;
    private ProgressBar progressBar;
    private Handler mainHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_methods, container, false);

        initViews(view);
        setupRecyclerView();
        loadMethods();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_methods);
        progressBar = view.findViewById(R.id.progress_bar);
        mainHandler = new Handler(Looper.getMainLooper());
        methodManager = new StudyMethodManager();
    }

    private void setupRecyclerView() {
        adapter = new StudyMethodAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnMethodClickListener(method -> {
            Toast.makeText(getContext(), "Método seleccionado: " + method.getName(), Toast.LENGTH_SHORT).show();
            // Aquí puedes añadir la lógica para abrir detalles del método o iniciarlo
        });
    }

    private void loadMethods() {
        progressBar.setVisibility(View.VISIBLE);
        methodManager.getStudyMethods(this);
    }

    @Override
    public void onMethodsLoaded(List<StudyMethod> methods) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            adapter.setMethods(methods);
        });
    }

    @Override
    public void onError(String error) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (methodManager != null) {
            methodManager.destroy();
        }
    }
}