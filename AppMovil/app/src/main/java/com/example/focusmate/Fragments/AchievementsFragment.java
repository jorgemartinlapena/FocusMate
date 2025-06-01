package com.example.focusmate.Fragments;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.Achievement.Achievement;
import com.example.focusmate.Achievement.AchievementManager;
import com.example.focusmate.R;
import com.example.focusmate.Achievement.AchievementAdapter;

import java.util.List;

public class AchievementsFragment extends Fragment implements AchievementManager.AchievementCallback {

    private AchievementManager achievementManager;
    private RecyclerView recyclerViewAchievements;
    private AchievementAdapter achievementsAdapter;
    private ProgressBar progressBar;
    private TextView statsText;
    private Button refreshButton;
    private Handler mainHandler;

    private List<Achievement> allAchievements;
    private List<Achievement> userAchievements;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);

        initViews(view);
        setupRecyclerView();
        loadAchievements();

        return view;
    }

    private void initViews(View view) {
        achievementManager = new AchievementManager(this, getContext()); // Pasar contexto
        mainHandler = new Handler(Looper.getMainLooper());

        // Usar los IDs correctos según los recursos compilados
        recyclerViewAchievements = view.findViewById(R.id.rv_achievements);
        progressBar = view.findViewById(R.id.progress_bar_achievements);
        statsText = view.findViewById(R.id.tv_achievement_stats);
        refreshButton = view.findViewById(R.id.btn_refresh_achievements);

        refreshButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Actualizando logros...", Toast.LENGTH_SHORT).show();
            loadAchievements();
        });
    }

    private void setupRecyclerView() {
        achievementsAdapter = new AchievementAdapter();
        recyclerViewAchievements.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAchievements.setAdapter(achievementsAdapter);

        achievementsAdapter.setOnAchievementClickListener((achievement, isUnlocked) -> {
            String status = isUnlocked ? "DESBLOQUEADO" : "BLOQUEADO";
            Toast.makeText(getContext(),
                    achievement.getName() + " - " + status,
                    Toast.LENGTH_SHORT).show();
            // Aquí podrías mostrar un dialog con más detalles
        });
    }

    private void loadAchievements() {
        progressBar.setVisibility(View.VISIBLE);

        // Cargar todos los logros primero
        achievementManager.getAllAchievements();
    }

    @Override
    public void onAchievementsLoaded(List<Achievement> achievements) {
        allAchievements = achievements;
        
        // Pasar los logros al adapter INMEDIATAMENTE
        if (achievementsAdapter != null) {
            achievementsAdapter.setAllAchievements(achievements);
        }
        
        // Ahora cargar logros del usuario
        achievementManager.getUserAchievements();
    }

    @Override
    public void onUserAchievementsLoaded(List<Achievement> achievements) {
        mainHandler.post(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            
            this.userAchievements = achievements;
            
            // Asegurarse de que el adapter tiene ambas listas
            if (achievementsAdapter != null) {
                // Volver a establecer allAchievements por si acaso
                if (allAchievements != null) {
                    achievementsAdapter.setAllAchievements(allAchievements);
                }
                // Establecer logros del usuario
                achievementsAdapter.setUserAchievements(achievements);
            }
            
            updateStatsText();
        });
    }

    @Override
    public void onError(String error) {
        mainHandler.post(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
        });
    }

    private void updateStatsText() {
        if (allAchievements != null && userAchievements != null) {
            int total = allAchievements.size();
            int unlocked = userAchievements.size();
            int percentage = total > 0 ? (unlocked * 100) / total : 0;

            String statsMessage = String.format("Progreso: %d/%d logros (%d%%)",
                    unlocked, total, percentage);
            statsText.setText(statsMessage);
        } else {
            statsText.setText("Cargando estadísticas...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refrescar al volver al fragmento
        loadAchievements();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (achievementManager != null) {
            achievementManager.destroy();
        }
    }
}