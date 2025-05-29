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
        private TextView durationText;
        private TextView taskTypeText;
        private TextView productivityText;
        private TextView methodText;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.tv_session_date);
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
            String formattedDate = formatDate(session.getSession_timestamp());
            dateText.setText(formattedDate);


            durationText.setText(session.getDuration_minutes() + " min");
            taskTypeText.setText("Tipo: " + session.getTask_type());
            productivityText.setText(session.getProductivity_level() + "/5");
            methodText.setText("Método " + session.getMethod_id());
        }

        private String formatDate(String timestamp) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = inputFormat.parse(timestamp);

                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                return outputFormat.format(date);
            } catch (ParseException e) {
                return timestamp;
            }
        }
    }
}
