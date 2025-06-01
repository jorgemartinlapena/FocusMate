package com.example.focusmate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.focusmate.Session.SessionManager;
import com.example.focusmate.Session.SessionPostResponse;

public class CountdownTimerActivity extends AppCompatActivity implements SessionManager.SessionCallback {

    private TextView timerText;
    private TextView statusText;
    private TextView methodNameText;
    private Button startButton;
    private Button pauseButton;
    private Button stopButton;
    private Button backButton;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private long timeLeftInMillis;

    // Datos de la sesión
    private int methodId;
    private String methodName;
    private int studyTime; // en minutos
    private int restTime; // en minutos
    private int repetitions;
    private int finalRestTime; // en minutos
    private String taskType;

    // Estado actual
    private int currentCycle = 1;
    private boolean isStudyPhase = true; // true = estudio, false = descanso
    private long sessionStartTime;
    private int totalStudyMinutes = 0;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        initViews();
        getSessionData();
        setupTimer();
        sessionManager = new SessionManager(this, this); // Pasar contexto
        sessionStartTime = System.currentTimeMillis();
    }

    private void initViews() {
        timerText = findViewById(R.id.tv_timer);
        statusText = findViewById(R.id.tv_status);
        methodNameText = findViewById(R.id.tv_method_name);
        startButton = findViewById(R.id.btn_start);
        pauseButton = findViewById(R.id.btn_pause);
        stopButton = findViewById(R.id.btn_stop);
        backButton = findViewById(R.id.btn_back);

        startButton.setOnClickListener(v -> startTimer());
        pauseButton.setOnClickListener(v -> pauseTimer());
        stopButton.setOnClickListener(v -> stopTimer());
        backButton.setOnClickListener(v -> finish());
    }

    private void getSessionData() {
        Intent intent = getIntent();
        methodId = intent.getIntExtra("method_id", 1);
        methodName = intent.getStringExtra("method_name");
        studyTime = intent.getIntExtra("study_time", 25);
        restTime = intent.getIntExtra("rest_time", 5);
        repetitions = intent.getIntExtra("repetitions", 4);
        finalRestTime = intent.getIntExtra("final_rest_time", 15);
        taskType = intent.getStringExtra("task_type");

        // Mostrar información del método
        methodNameText.setText(methodName != null ? methodName : "Método Personalizado");
        updateStatusText();
    }

    private void setupTimer() {
        // Empezar con tiempo de estudio
        timeLeftInMillis = studyTime * 60 * 1000; // convertir a milisegundos
        updateTimerDisplay();
    }

    private void startTimer() {
        if (!isRunning) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerDisplay();
                }

                @Override
                public void onFinish() {
                    onPhaseComplete();
                }
            }.start();

            isRunning = true;
            isPaused = false;
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
            isPaused = true;
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
        }
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Mostrar dialog de confirmación
        new AlertDialog.Builder(this)
                .setTitle("Detener Sesión")
                .setMessage("¿Estás seguro de que quieres detener la sesión actual?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Si hay tiempo de estudio completado, crear sesión parcial
                    if (totalStudyMinutes > 0) {
                        showProductivityDialog();
                    } else {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void onPhaseComplete() {
        isRunning = false;

        if (isStudyPhase) {
            // Completó fase de estudio
            totalStudyMinutes += studyTime;
            Toast.makeText(this, "¡Tiempo de estudio completado! Hora del descanso", Toast.LENGTH_SHORT).show();

            // Cambiar a fase de descanso
            isStudyPhase = false;

            // Determinar tiempo de descanso
            int breakTime;
            if (currentCycle == repetitions) {
                // Último ciclo - descanso final
                breakTime = finalRestTime;
            } else {
                // Descanso normal
                breakTime = restTime;
            }

            timeLeftInMillis = breakTime * 60 * 1000;

        } else {
            // Completó fase de descanso
            Toast.makeText(this, "¡Descanso completado!", Toast.LENGTH_SHORT).show();

            if (currentCycle >= repetitions) {
                // Sesión completa
                onSessionComplete();
                return;
            } else {
                // Siguiente ciclo
                currentCycle++;
                isStudyPhase = true;
                timeLeftInMillis = studyTime * 60 * 1000;
            }
        }

        updateStatusText();
        updateTimerDisplay();

        // Reiniciar automáticamente (opcional - puedes cambiarlo por confirmación manual)
        startTimer();
    }

    private void onSessionComplete() {
        Toast.makeText(this, "¡Sesión completada! ¡Excelente trabajo!", Toast.LENGTH_LONG).show();
        showProductivityDialog();
    }

    private void showProductivityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Evalúa tu Productividad");
        builder.setMessage("¿Cómo calificarías tu nivel de productividad en esta sesión?");

        // Crear input para el nivel de productividad
        final EditText input = new EditText(this);
        input.setHint("Nivel (1-5)");
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String productivityStr = input.getText().toString().trim();
            if (!productivityStr.isEmpty()) {
                try {
                    int productivity = Integer.parseInt(productivityStr);
                    if (productivity >= 1 && productivity <= 5) {
                        createSession(productivity);
                    } else {
                        Toast.makeText(this, "El nivel debe estar entre 1 y 5", Toast.LENGTH_SHORT).show();
                        showProductivityDialog(); // Mostrar de nuevo
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Por favor ingresa un número válido", Toast.LENGTH_SHORT).show();
                    showProductivityDialog(); // Mostrar de nuevo
                }
            } else {
                Toast.makeText(this, "Por favor ingresa un nivel de productividad", Toast.LENGTH_SHORT).show();
                showProductivityDialog(); // Mostrar de nuevo
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }

    private void createSession(int productivityLevel) {
        // Crear sesión sin pasar userId, SessionManager lo obtiene automáticamente
        sessionManager.createStudySession(
                methodId,
                totalStudyMinutes,
                taskType,
                productivityLevel
        );
    }

    private void updateTimerDisplay() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }

    private void updateStatusText() {
        String phase = isStudyPhase ? "ESTUDIO" : "DESCANSO";
        String status = String.format("Ciclo %d/%d - %s", currentCycle, repetitions, phase);
        statusText.setText(status);
    }

    // Implementar callbacks del SessionManager
    @Override
    public void onSessionCreated(SessionPostResponse response) {
        Toast.makeText(this, "¡Sesión guardada exitosamente!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onSessionError(String error) {
        Toast.makeText(this, "Error al guardar sesión: " + error, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onSessionsLoaded(java.util.List<com.example.focusmate.Session.Session> sessions) {
        // No necesario para esta actividad
    }
}
