package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> implements Filterable {

    private Context context;
    private List<Exercise> exerciseList;
    private List<Exercise> exerciseListFull;
    private ActivityResultLauncher<Intent> exerciseDetailLauncher;

    public ExerciseAdapter(Context context, List<Exercise> exerciseList, ActivityResultLauncher<Intent> exerciseDetailLauncher) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.exerciseListFull = new ArrayList<>(exerciseList);
        this.exerciseDetailLauncher = exerciseDetailLauncher;
    }

    @Override
    public Filter getFilter() {
        return exerciseFilter;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.exerciseNameTextView.setText(exercise.getName());
        holder.exerciseMuscleTextView.setText(exercise.getMuscle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExerciseDetailActivity.class);
            intent.putExtra("exercise_name", exercise.getName());
            intent.putExtra("exercise_description", exercise.getDescription());
            intent.putExtra("exercise_muscle", exercise.getMuscle());
            exerciseDetailLauncher.launch(intent);
        });

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseNameTextView;
        TextView exerciseMuscleTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exercise_name_text);
            exerciseMuscleTextView=itemView.findViewById(R.id.muscle_text);
        }
    }

    private Filter exerciseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Exercise> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exerciseListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Exercise item : exerciseListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exerciseList.clear();
            exerciseList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
