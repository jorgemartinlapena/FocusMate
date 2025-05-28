package com.example.focusmate.StudyMethods;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.R;
import com.example.focusmate.StudyMethods.StudyMethod;

import java.util.ArrayList;
import java.util.List;

public class StudyMethodAdapter extends RecyclerView.Adapter<StudyMethodAdapter.MethodViewHolder> {
    private List<StudyMethod> methods;
    private OnMethodClickListener listener;

    public interface OnMethodClickListener {
        void onMethodClick(StudyMethod method);
    }

    public StudyMethodAdapter() {
        this.methods = new ArrayList<>();
    }

    public void setMethods(List<StudyMethod> methods) {
        this.methods = methods;
        notifyDataSetChanged();
    }

    public void setOnMethodClickListener(OnMethodClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_study_method, parent, false);
        return new MethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MethodViewHolder holder, int position) {
        StudyMethod method = methods.get(position);
        holder.bind(method);
    }

    @Override
    public int getItemCount() {
        return methods.size();
    }

    class MethodViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView descriptionText;
        private TextView studyTimeText;
        private TextView repetitionsText;

        public MethodViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tv_method_name);
            descriptionText = itemView.findViewById(R.id.tv_method_description);
            studyTimeText = itemView.findViewById(R.id.tv_study_time);
            repetitionsText = itemView.findViewById(R.id.tv_repetitions);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onMethodClick(methods.get(position));
                    }
                }
            });
        }

        public void bind(StudyMethod method) {
            nameText.setText(method.getName());
            descriptionText.setText(method.getDescription());
            studyTimeText.setText(method.getStudyTime() + " min estudio / " + method.getRestTime() + " min descanso");
            repetitionsText.setText(method.getRepetitions() + " repeticiones");
        }
    }
}