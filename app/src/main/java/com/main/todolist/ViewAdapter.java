package com.main.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import database.Task;
import model.TaskViewModel;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder> {
    private List<Task> tasks;
    private Context context;
    private final FragmentManager fragmentManager;
    private final TaskViewModel taskViewModel;
    public ViewAdapter(List<Task> tasks , FragmentManager fragmentManager, TaskViewModel taskViewModel) {
        this.tasks = tasks;
        this.fragmentManager = fragmentManager;
        this.taskViewModel = taskViewModel;
    }

    public void updateTasks(List<Task> tasks){
        this.tasks = tasks;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.description.setText(tasks.get(position).getTaskDescription());
        holder.editButton.setOnClickListener(v -> {
            EditTaskFragment editTaskFragment = new EditTaskFragment(tasks.get(position), taskViewModel);
            editTaskFragment.show(fragmentManager, "Edit");
        });

        holder.checkbox.setOnClickListener(v -> {
            if(holder.checkbox.isChecked()){
                taskViewModel.deleteById(tasks.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox checkbox;
        public final TextView description;
        public final Button editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkBoxTask);
            description = itemView.findViewById(R.id.textViewTaskDescription);
            editButton = itemView.findViewById(R.id.taskEditButton);
        }
    }
}
