package com.main.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import database.Task;
import model.TaskViewModel;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd ");

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
            EditTaskDialog editTaskDialog = new EditTaskDialog(tasks.get(position), taskViewModel);
            editTaskDialog.show(fragmentManager, "Edit");
        });
        holder.finishedTime.setText(tasks.get(position).getFinishedTime().format(DATE_TIME_FORMATTER));

        holder.checkbox.setOnClickListener(v -> {
            if(holder.checkbox.isChecked()){
                taskViewModel.deleteById(tasks.get(position).getId());
            }
        });
        String priority = "";
        switch (tasks.get(position).getPriority()){
            case Task.TASK_PRIORITY_LOW:
                priority = "!";
                break;
            case Task.TASK_PRIORITY_MEDIUM:
                priority = "!!";
                break;
            case Task.TASK_PRIORITY_HIGH:
                priority = "!!!";
                break;
        }
        holder.priority.setText(priority);

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox checkbox;
        public final TextView description;
        public final Button editButton;
        public final TextView finishedTime;
        public final TextView priority;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkBoxTask);
            description = itemView.findViewById(R.id.textViewTaskDescription);
            editButton = itemView.findViewById(R.id.taskEditButton);
            finishedTime = itemView.findViewById(R.id.taskDeadline);
            priority = itemView.findViewById(R.id.taskPriority);
        }
    }
}
