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
import android.widget.Toast;
import android.app.AlertDialog;
import com.example.focusmate.Session.SessionConfigActivity;
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
            showMethodDetailsDialog(method);
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

    private void showMethodDetailsDialog(StudyMethod method) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflar el layout personalizado
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_method_details, null);

        // Configurar las vistas
        TextView nameText = dialogView.findViewById(R.id.tv_method_name);
        TextView descriptionText = dialogView.findViewById(R.id.tv_method_description);
        TextView studyTimeText = dialogView.findViewById(R.id.tv_study_time);
        TextView restTimeText = dialogView.findViewById(R.id.tv_rest_time);
        TextView repetitionsText = dialogView.findViewById(R.id.tv_repetitions);
        TextView finalRestText = dialogView.findViewById(R.id.tv_final_rest);
        TextView totalTimeText = dialogView.findViewById(R.id.tv_total_time);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
        Button startButton = dialogView.findViewById(R.id.btn_start_method);

        // Llenar los datos
        nameText.setText(method.getName());
        descriptionText.setText(method.getDescription());
        studyTimeText.setText(method.getStudyTime() + " min");
        restTimeText.setText(method.getRestTime() + " min");
        repetitionsText.setText(method.getRepetitions() + " ciclos");
        finalRestText.setText(method.getFinalRestTime() + " min");

        // Calcular tiempo total
        int totalMinutes = (method.getStudyTime() + method.getRestTime()) * method.getRepetitions()
                + method.getFinalRestTime() - method.getRestTime(); // Restar el último descanso normal
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        String totalTimeStr;
        if (hours > 0) {
            totalTimeStr = String.format("Tiempo total estimado: %d hora%s %d minutos",
                    hours, hours > 1 ? "s" : "", minutes);
        } else {
            totalTimeStr = String.format("Tiempo total estimado: %d minutos", minutes);
        }
        totalTimeText.setText(totalTimeStr);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Configurar botones
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        startButton.setOnClickListener(v -> {
            dialog.dismiss();
            startSessionWithMethod(method);
        });

        dialog.show();
    }

    // Método para iniciar sesión con el método seleccionado
    private void startSessionWithMethod(StudyMethod method) {
        Intent intent = new Intent(getActivity(), SessionConfigActivity.class);
        // Pasar el método pre-seleccionado
        intent.putExtra("preselected_method_id", method.getId());
        intent.putExtra("preselected_method_name", method.getName());
        intent.putExtra("preselected_study_time", method.getStudyTime());
        intent.putExtra("preselected_rest_time", method.getRestTime());
        intent.putExtra("preselected_repetitions", method.getRepetitions());
        intent.putExtra("preselected_final_rest", method.getFinalRestTime());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (methodManager != null) {
            methodManager.destroy();
        }
    }
}