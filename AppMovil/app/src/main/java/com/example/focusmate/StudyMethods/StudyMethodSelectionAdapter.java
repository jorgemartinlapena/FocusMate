package com.example.focusmate.StudyMethods;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusmate.R;
import com.example.focusmate.StudyMethods.StudyMethod;

import java.util.ArrayList;
import java.util.List;

public class StudyMethodSelectionAdapter extends RecyclerView.Adapter<StudyMethodSelectionAdapter.MethodViewHolder> {
    private List<StudyMethod> methods;
    private int selectedPosition = -1;
    private OnMethodSelectedListener listener;

    public interface OnMethodSelectedListener {
        void onMethodSelected(StudyMethod method);
    }

    public StudyMethodSelectionAdapter() {
        this.methods = new ArrayList<>();
    }

    public void setMethods(List<StudyMethod> methods) {
        this.methods = methods;
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    public void setOnMethodSelectedListener(OnMethodSelectedListener listener) {
        this.listener = listener;
    }

    public StudyMethod getSelectedMethod() {
        if (selectedPosition >= 0 && selectedPosition < methods.size()) {
            return methods.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public MethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_method_selection, parent, false);
        return new MethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MethodViewHolder holder, int position) {
        StudyMethod method = methods.get(position);
        holder.bind(method, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return methods.size();
    }

    class MethodViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private TextView nameText;
        private TextView detailsText;

        public MethodViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_method);
            nameText = itemView.findViewById(R.id.tv_method_name);
            detailsText = itemView.findViewById(R.id.tv_method_details);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onMethodSelected(methods.get(position));
                    }
                }
            });
        }

        public void bind(StudyMethod method, boolean isSelected) {
            radioButton.setChecked(isSelected);
            nameText.setText(method.getName());
            detailsText.setText(method.getStudyTime() + " min estudio / " +
                    method.getRestTime() + " min descanso (" +
                    method.getRepetitions() + " ciclos)");
        }
    }
}