package com.example.focusmate.Session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {
    private List<Session> sessions;
    private OnSessionClickListener listener;

    public interface OnSessionClickListener {
        void onSessionClick(Session session);
    }

    public SessionAdapter() {
        this.sessions = new ArrayList<>();
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    public void setOnSessionClickListener(OnSessionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.bind(session);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    class SessionViewHolder extends RecyclerView.ViewHolder {
        private TextView dateText;
        private TextView timeText;
        private TextView durationText;
        private TextView taskTypeText;
        private TextView productivityText;
        private TextView methodText;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.tv_session_date);
            timeText = itemView.findViewById(R.id.tv_session_time);
            durationText = itemView.findViewById(R.id.tv_session_duration);
            taskTypeText = itemView.findViewById(R.id.tv_task_type);
            productivityText = itemView.findViewById(R.id.tv_productivity_level);
            methodText = itemView.findViewById(R.id.tv_method_id);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onSessionClick(sessions.get(position));
                    }
                }
            });
        }

        public void bind(Session session) {
            // Formatear fecha y hora por separado
            String[] dateTime = formatDateTime(session.getSession_timestamp());
            dateText.setText(dateTime[0]); // Fecha
            timeText.setText(dateTime[1]); // Hora

            // Duración
            durationText.setText(session.getDuration_minutes() + " min");

            // Tipo de tarea
            taskTypeText.setText("Tipo: " + session.getTask_type());

            // Nivel de productividad
            productivityText.setText(session.getProductivity_level() + "/5");

            // ID del método (podrías mejorarlo obteniendo el nombre del método)
            methodText.setText("Método " + session.getMethod_id());
        }

        private String[] formatDateTime(String timestamp) {
            try {
                // Manejar tanto formato con T como sin T
                SimpleDateFormat inputFormat;
                if (timestamp.contains("T")) {
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                } else {
                    inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                }

                Date date = inputFormat.parse(timestamp);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(date);

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String formattedTime = timeFormat.format(date);

                return new String[]{formattedDate, formattedTime};

            } catch (ParseException e) {
                if (timestamp.contains("T")) {
                    String[] parts = timestamp.split("T");
                    if (parts.length == 2) {
                        String datePart = parts[0];
                        String timePart = parts[1].substring(0, Math.min(5, parts[1].length())); // Solo HH:mm
                        return new String[]{datePart, timePart};
                    }
                }
                return new String[]{timestamp, ""};
            }
        }
    }
}
