package com.example.focusmate.Session;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.CountdownTimerActivity;
import com.example.focusmate.R;
import com.example.focusmate.StudyMethods.*;

import java.util.List;

public class SessionConfigActivity extends AppCompatActivity implements StudyMethodManager.StudyMethodsCallback {

    private Spinner spinnerTaskType;
    private RecyclerView recyclerViewMethods;
    private StudyMethodSelectionAdapter methodsAdapter;
    private Button btnStartSession;
    private Button btnCancel;
    private ProgressBar progressBar;

    private StudyMethodManager methodManager;
    private Handler mainHandler;
    private StudyMethod selectedMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_config);

        initViews();
        setupSpinner();
        setupRecyclerView();
        loadStudyMethods();
    }

    private void initViews() {
        spinnerTaskType = findViewById(R.id.spinner_task_type);
        recyclerViewMethods = findViewById(R.id.rv_study_methods);
        btnStartSession = findViewById(R.id.btn_start_session);
        btnCancel = findViewById(R.id.btn_cancel);
        progressBar = findViewById(R.id.progress_bar);

        methodManager = new StudyMethodManager();
        mainHandler = new Handler(Looper.getMainLooper());

        // Listeners de botones
        btnStartSession.setOnClickListener(v -> startSession());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupSpinner() {
        String[] taskTypes = {
                "Seleccionar tipo...",
                "Estudio",
                "Trabajo",
                "Lectura",
                "Investigación",
                "Programación",
                "Redacción",
                "Matemáticas",
                "Idiomas",
                "Otros"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, taskTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTaskType.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        methodsAdapter = new StudyMethodSelectionAdapter();
        recyclerViewMethods.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMethods.setAdapter(methodsAdapter);

        methodsAdapter.setOnMethodSelectedListener(method -> {
            selectedMethod = method;
            updateStartButtonState();
        });
    }

    private void loadStudyMethods() {
        progressBar.setVisibility(View.VISIBLE);
        methodManager.getStudyMethods(this);
    }

    private void updateStartButtonState() {
        boolean canStart = selectedMethod != null &&
                spinnerTaskType.getSelectedItemPosition() > 0;
        btnStartSession.setEnabled(canStart);
    }

    private void startSession() {
        if (selectedMethod == null) {
            Toast.makeText(this, "Por favor selecciona un método de estudio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerTaskType.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Por favor selecciona un tipo de tarea", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskType = spinnerTaskType.getSelectedItem().toString();

        // Enviar datos al cronómetro
        Intent intent = new Intent(this, CountdownTimerActivity.class);
        intent.putExtra("method_id", selectedMethod.getId());
        intent.putExtra("method_name", selectedMethod.getName());
        intent.putExtra("study_time", selectedMethod.getStudyTime());
        intent.putExtra("rest_time", selectedMethod.getRestTime());
        intent.putExtra("repetitions", selectedMethod.getRepetitions());
        intent.putExtra("final_rest_time", selectedMethod.getFinalRestTime());
        intent.putExtra("task_type", taskType);

        startActivity(intent);
        finish();
    }

    @Override
    public void onMethodsLoaded(List<StudyMethod> methods) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            methodsAdapter.setMethods(methods);
            checkForPreselectedMethod();
        });
    }

    @Override
    public void onError(String error) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (methodManager != null) {
            methodManager.destroy();
        }
    }

    private void checkForPreselectedMethod() {
        Intent intent = getIntent();
        if (intent.hasExtra("preselected_method_id")) {
            int methodId = intent.getIntExtra("preselected_method_id", -1);
            String methodName = intent.getStringExtra("preselected_method_name");
            int studyTime = intent.getIntExtra("preselected_study_time", 25);
            int restTime = intent.getIntExtra("preselected_rest_time", 5);
            int repetitions = intent.getIntExtra("preselected_repetitions", 4);
            int finalRest = intent.getIntExtra("preselected_final_rest", 15);
            String description = intent.getStringExtra("preselected_description");
            int totalTime = studyTime * repetitions;

            selectedMethod = new StudyMethod(methodId, methodName, repetitions, studyTime, restTime, finalRest, description, totalTime);

            updateStartButtonState();
        }
    }
}