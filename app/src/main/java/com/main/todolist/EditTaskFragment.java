package com.main.todolist;


import android.os.Bundle;


import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.time.LocalDateTime;

import database.Task;
import model.TaskViewModel;

public class EditTaskFragment extends DialogFragment {
    private Task task;
    private TaskViewModel taskViewModel;

    // widgets
    private EditText editTaskDescription;
    private RadioGroup taskPriorities;
    private TimePicker timePicker;
    private DatePicker datePicker;

    public EditTaskFragment(Task task, TaskViewModel taskViewModel) {
        this.task = task;
        this.taskViewModel = taskViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        setTaskValuesToViews(rootView);

        Button btnCancel = rootView.findViewById(R.id.editTaskCancelButton);
        btnCancel.setOnClickListener(v -> {
            if (getDialog() != null){
                getDialog().dismiss();
            }
        });

        Button btnEdit = rootView.findViewById(R.id.editTaskEditButton);
        btnEdit.setOnClickListener(v -> {
            String newDescription = editTaskDescription.getText().toString();
            RadioButton checkedRadioButton = rootView.findViewById(taskPriorities.getCheckedRadioButtonId());
            int newPriority = 0;
            switch (checkedRadioButton.getText().toString()) {
                case "!":
                    newPriority = Task.TASK_PRIORITY_LOW;
                    break;
                case "!!":
                    newPriority = Task.TASK_PRIORITY_MEDIUM;
                    break;
                case "!!!":
                    newPriority = Task.TASK_PRIORITY_HIGH;
                    break;
            }
            LocalDateTime newFinishedTime = LocalDateTime.of(
                    datePicker.getYear(),
                    datePicker.getMonth() + 1,
                    datePicker.getDayOfMonth(),
                    timePicker.getHour(),
                    timePicker.getMinute());

            updateTask(this.task.getId(), newDescription, newPriority, newFinishedTime);
            if (getDialog() != null){
                getDialog().dismiss();
                Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnDelete = rootView.findViewById(R.id.editTaskDeleteButton);
        btnDelete.setOnClickListener(v -> {
            taskViewModel.deleteById(task.getId());
            if (getDialog() != null){
                getDialog().dismiss();
            }
            Toast.makeText(getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    private void setTaskValuesToViews(View rootView){
        // set description
        editTaskDescription = rootView.findViewById(R.id.editTaskDescription);
        editTaskDescription.setText(task.getTaskDescription());

        // set priority
        taskPriorities = rootView.findViewById(R.id.editTaskPriority);
        RadioButton radioButton = null;

        switch (task.getPriority()) {
            case Task.TASK_PRIORITY_LOW:
                radioButton = rootView.findViewById(R.id.editTaskPriorityLow);
                break;
            case Task.TASK_PRIORITY_MEDIUM:
                radioButton = rootView.findViewById(R.id.editTaskPriorityMedium);
                break;
            case Task.TASK_PRIORITY_HIGH:
                radioButton = rootView.findViewById(R.id.editTaskPriorityHigh);
                break;
        }

        if (radioButton != null) {
            radioButton.setChecked(true);
        }

        // set time
        timePicker = rootView.findViewById(R.id.editTaskTime);
        timePicker.setIs24HourView(true);
        timePicker.setHour(task.getFinishedTime().getHour());
        timePicker.setMinute(task.getFinishedTime().getMinute());

        // set date
        datePicker = rootView.findViewById(R.id.editTaskDate);
        datePicker.updateDate(task.getFinishedTime().getYear(), task.getFinishedTime().getMonthValue(), task.getFinishedTime().getDayOfMonth());
    }

    private void updateTask(int id, String newDescription, int newPriority, LocalDateTime newFinishedTime){
        this.taskViewModel.update(id, newDescription, newPriority, newFinishedTime.toString());
    }
}