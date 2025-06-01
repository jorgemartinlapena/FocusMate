package com.example.focusmate.Achievement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.Achievement.Achievement;
import com.example.focusmate.R;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private List<Achievement> allAchievements;
    private List<Achievement> userAchievements;
    private OnAchievementClickListener listener;

    public interface OnAchievementClickListener {
        void onAchievementClick(Achievement achievement, boolean isUnlocked);
    }

    public AchievementAdapter() {
        this.allAchievements = new ArrayList<>();
        this.userAchievements = new ArrayList<>();
    }

    public void setAllAchievements(List<Achievement> achievements) {
        this.allAchievements = achievements != null ? achievements : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setUserAchievements(List<Achievement> achievements) {
        this.userAchievements = achievements != null ? achievements : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnAchievementClickListener(OnAchievementClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = allAchievements.get(position);
        boolean isUnlocked = isAchievementUnlocked(achievement);
        holder.bind(achievement, isUnlocked);
    }

    @Override
    public int getItemCount() {
        return allAchievements.size();
    }

    private boolean isAchievementUnlocked(Achievement achievement) {
        if (userAchievements == null) return false;

        for (Achievement userAchievement : userAchievements) {
            if (userAchievement.getId() == achievement.getId()) {
                return true;
            }
        }
        return false;
    }

    class AchievementViewHolder extends RecyclerView.ViewHolder {
        private TextView iconText;
        private TextView nameText;
        private TextView descriptionText;
        private TextView requirementsText;
        private TextView statusText;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            iconText = itemView.findViewById(R.id.tv_achievement_icon);
            nameText = itemView.findViewById(R.id.tv_achievement_name);
            descriptionText = itemView.findViewById(R.id.tv_achievement_description);
            requirementsText = itemView.findViewById(R.id.tv_achievement_requirements);
            statusText = itemView.findViewById(R.id.tv_achievement_status);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Achievement achievement = allAchievements.get(position);
                        boolean isUnlocked = isAchievementUnlocked(achievement);
                        listener.onAchievementClick(achievement, isUnlocked);
                    }
                }
            });
        }

        public void bind(Achievement achievement, boolean isUnlocked) {
            // Configurar icono según el tipo de logro
            String icon = getAchievementIcon(achievement.getType());
            iconText.setText(icon);

            // Nombre del logro
            nameText.setText(achievement.getName());

            // Descripción
            descriptionText.setText(achievement.getDescription());

            // Requisitos
            String requirements = buildRequirementsText(achievement);
            requirementsText.setText(requirements);

            // Estado y estilo visual
            if (isUnlocked) {
                // Logro desbloqueado
                statusText.setText("✓ DESBLOQUEADO");
                statusText.setVisibility(View.VISIBLE);
                statusText.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));

                // Estilo normal
                itemView.setAlpha(1.0f);
                iconText.setBackgroundTintList(itemView.getContext().getColorStateList(android.R.color.holo_orange_light));
                nameText.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));

            } else {
                // Logro bloqueado
                statusText.setText("🔒 BLOQUEADO");
                statusText.setVisibility(View.VISIBLE);
                statusText.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray));

                // Estilo atenuado
                itemView.setAlpha(0.6f);
                iconText.setBackgroundTintList(itemView.getContext().getColorStateList(android.R.color.darker_gray));
                nameText.setTextColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray));
            }
        }

        private String getAchievementIcon(String type) {
            switch (type.toLowerCase()) {
                case "total":
                    return "📊"; // Estadísticas totales
                case "sesion":
                    return "⏱️"; // Sesión individual
                case "consistencia":
                    return "🔥"; // Racha/consistencia
                default:
                    return "🏆"; // Logro general
            }
        }

        private String buildRequirementsText(Achievement achievement) {
            StringBuilder requirements = new StringBuilder();

            // Añadir minutos si es necesario
            if (achievement.getMinutes() > 0) {
                if (achievement.getMinutes() >= 60) {
                    int hours = achievement.getMinutes() / 60;
                    int minutes = achievement.getMinutes() % 60;
                    if (minutes > 0) {
                        requirements.append(hours).append("h ").append(minutes).append("m");
                    } else {
                        requirements.append(hours).append(" hora").append(hours > 1 ? "s" : "");
                    }
                } else {
                    requirements.append(achievement.getMinutes()).append(" min");
                }
            }

            // Añadir días si es necesario
            if (achievement.getDays() > 0) {
                if (requirements.length() > 0) {
                    requirements.append(" durante ");
                }
                requirements.append(achievement.getDays()).append(" día").append(achievement.getDays() > 1 ? "s" : "");
            }

            // Añadir tipo
            if (requirements.length() > 0) {
                requirements.append(" • ");
            }

            switch (achievement.getType().toLowerCase()) {
                case "total":
                    requirements.append("Acumulado");
                    break;
                case "sesion":
                    requirements.append("Por sesión");
                    break;
                case "consistencia":
                    requirements.append("Consistencia");
                    break;
                default:
                    requirements.append(achievement.getType());
                    break;
            }

            return requirements.toString();
        }
    }
}