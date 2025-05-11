package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> implements Filterable {

    public interface OnProgramClickListener {
        void onProgramClick(Program program, int position);
    }

    private List<Program> programList=new ArrayList<>();
    private List<Program> programListFull;
    private Context context;
    private OnProgramClickListener listener;    // <-- νέο πεδίο

    public ProgramAdapter(List<Program> programList, Context context) {
        this.programList = programList;
        this.programListFull = new ArrayList<>(programList);
        this.context = context;
    }
    public ProgramAdapter() {
        programList = new ArrayList<>();
        programListFull = new ArrayList<>();
    }
    /** Setter για να το καλέσουμε από την MainActivity */
    public void setOnProgramClickListener(OnProgramClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        Program program = programList.get(position);
        holder.programName.setText(program.getName());

        // ... υπολογισμός μυϊκών ομάδων (όπως πριν) ...
        Map<String, Integer> muscleCount = new HashMap<>();
        for (FullExercise ex : program.getFullExercises()) {
            for (String m : ex.getMuscle().split(",")) {
                String mm = m.trim();
                muscleCount.put(mm, muscleCount.getOrDefault(mm, 0) + 1);
            }
        }
        List<Map.Entry<String,Integer>> sorted = new ArrayList<>(muscleCount.entrySet());
        sorted.sort((a,b)->b.getValue()-a.getValue());
        List<String> top = new ArrayList<>();
        for (int i = 0; i < Math.min(2, sorted.size()); i++) top.add(sorted.get(i).getKey());
        holder.programMuscle.setText(String.join(", ", top));

        // Αντί για startActivity, καλούμε το listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProgramClick(program, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return programList != null ? programList.size() : 0;
    }


    public void addProgram(Program program) {
        if (programList == null) {
            programList = new ArrayList<>();
            programListFull=new ArrayList<>();
        }
        programList.add(program);
        programListFull.add(program);
        notifyItemInserted(programList.size() - 1);
    }

    public void removeProgram(int position) {
        Program toRemove = programList.get(position);
        programList.remove(position);
        programListFull.remove(toRemove);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        // (ο κώδικας του φίλτρου παραμένει ίδιος)
        return programFilter;
    }

    private Filter programFilter = new Filter() {
        @Override protected FilterResults performFiltering(CharSequence constraint) {
            List<Program> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(programListFull);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (Program p : programListFull) {
                    if (p.getName().toLowerCase().contains(pattern)) filteredList.add(p);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override protected void publishResults(CharSequence constraint, FilterResults results) {
            programList.clear();
            //noinspection unchecked
            programList.addAll((List<Program>)results.values);
            notifyDataSetChanged();
        }
    };

    static class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView programName, programMuscle;
        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            programName = itemView.findViewById(R.id.program_name);
            programMuscle = itemView.findViewById(R.id.muscle_text);
        }
    }
    public void setProgramList(ArrayList<Program> programList) {
        this.programList = programList;
        notifyDataSetChanged();
    }
}
