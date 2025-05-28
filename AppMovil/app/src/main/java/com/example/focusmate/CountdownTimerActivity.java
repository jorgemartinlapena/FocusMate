package com.example.focusmate;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;


public class CountdownTimerActivity extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnStart, btnPause, btnReset, btnSetTime;
    private EditText etMinutes;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeLeftInMillis = 3000000; // 50 minutos por defecto (50 * 60 * 1000)
    private long initialTimeInMillis = 3000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);
        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> {
            finish(); // Cierra la actividad y vuelve a la anterior
        });

        initViews();
        updateTimerText();
        setupClickListeners();


    }

    private void initViews() {
        tvTimer = findViewById(R.id.tv_timer);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnReset = findViewById(R.id.btn_reset);
        btnSetTime = findViewById(R.id.btn_set_time);
        etMinutes = findViewById(R.id.et_minutes);

        // Configurar texto inicial
        etMinutes.setText("50");
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());
        btnSetTime.setOnClickListener(v -> setCustomTime());
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timeLeftInMillis = 0;
                updateTimerText();
                onTimerFinished();
                updateButtons();
            }
        }.start();

        isTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = initialTimeInMillis;
        isTimerRunning = false;
        updateTimerText();
        updateButtons();
    }

    private void setCustomTime() {
        String input = etMinutes.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Ingresa los minutos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int minutes = Integer.parseInt(input);
            if (minutes < 1) {
                Toast.makeText(this, "Los minutos deben ser mayor a 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (minutes > 999) {
                Toast.makeText(this, "Máximo 999 minutos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pausar el timer si está corriendo
            if (isTimerRunning) {
                pauseTimer();
            }

            // Establecer nuevo tiempo
            initialTimeInMillis = minutes * 60 * 1000L;
            timeLeftInMillis = initialTimeInMillis;
            updateTimerText();
            updateButtons();

            Toast.makeText(this, "Tiempo establecido: " + minutes + " minutos", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingresa un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTimerText() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted;
        if (hours > 0) {
            timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        tvTimer.setText(timeFormatted);

        // Cambiar color cuando quedan menos de 5 minutos
        if (timeLeftInMillis < 300000 && timeLeftInMillis > 0) { // 5 minutos en milisegundos
            tvTimer.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        } else {
            tvTimer.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        }
    }

    private void updateButtons() {
        if (isTimerRunning) {
            btnStart.setEnabled(false);
            btnPause.setEnabled(true);
            btnSetTime.setEnabled(false);
        } else {
            btnStart.setEnabled(timeLeftInMillis > 0);
            btnPause.setEnabled(false);
            btnSetTime.setEnabled(true);
        }

        btnReset.setEnabled(timeLeftInMillis < initialTimeInMillis || !isTimerRunning);
    }

    private void onTimerFinished() {
        // Reproducir sonido/vibración cuando termine el timer
        playNotificationSound();
        showTimerFinishedDialog();
    }

    private void playNotificationSound() {
        try {
            // Reproducir sonido de notificación
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();

            // Vibrar
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(1000);
                }
            }
        } catch (Exception e) {
            Log.e("Timer", "Error al reproducir sonido", e);
        }
    }

    private void showTimerFinishedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("¡Tiempo terminado!")
                .setMessage("El cronómetro ha llegado a cero.")
                .setPositiveButton("OK", (dialog, which) -> {
                    resetTimer();
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // Guardar estado cuando se rota la pantalla
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("timeLeft", timeLeftInMillis);
        outState.putBoolean("timerRunning", isTimerRunning);
        outState.putLong("initialTime", initialTimeInMillis);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeLeftInMillis = savedInstanceState.getLong("timeLeft");
        isTimerRunning = savedInstanceState.getBoolean("timerRunning");
        initialTimeInMillis = savedInstanceState.getLong("initialTime");

        updateTimerText();
        updateButtons();

        if (isTimerRunning) {
            startTimer();
        }
    }
}
