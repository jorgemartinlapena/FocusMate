package com.example.focusmate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import com.example.focusmate.Session.SessionManager;
import com.example.focusmate.Session.SessionResponse;

public class MainActivity extends AppCompatActivity implements SessionManager.SessionCallback{
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);


        Button testButton = findViewById(R.id.test_button);
        testButton.setOnClickListener(v -> testCreateSession());

        Button timerButton = findViewById(R.id.btn_timer);
        timerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CountdownTimerActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void onTestButtonClick(View view) {
        testCreateSession();
    }
    private void testCreateSession() {
        sessionManager.createStudySession(
                1,
                4,
                90,
                "prueba",
                4

        );
    }


    public void onSessionCreated(SessionResponse response) {
        Toast.makeText(this, "¡Sesión creada exitosamente!", Toast.LENGTH_SHORT).show();

    }


    public void onSessionError(String error) {
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
    }


    protected void onDestroy() {
        super.onDestroy();
        if (sessionManager != null) {
            sessionManager.destroy();
        }
    }

}