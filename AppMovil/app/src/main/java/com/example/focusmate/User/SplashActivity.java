package com.example.focusmate.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.focusmate.MainActivity;
import com.example.focusmate.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 segundos
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthStatus, SPLASH_DELAY);
    }
    
    private void checkAuthStatus() {
        AuthService authService = new AuthService(this);
        
        if (authService.isLoggedIn()) {
            // Usuario ya logueado, ir a MainActivity
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Usuario no logueado, ir a LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
        }
        
        finish();
    }
}
