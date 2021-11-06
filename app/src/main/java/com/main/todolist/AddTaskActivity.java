package com.main.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;

import database.Task;
import model.TaskViewModel;

public class AddTaskActivity extends AppCompatActivity {
    public static final String taskDescriptionKey = "com.main.todolist.AddTaskActivity.TASK_DESCRIPTION";
    public static final String taskPriorityKey = "com.main.todolist.AddTaskActivity.TASK_PRIORITY";
    public static final String taskCreatedTimeKey = "com.main.todolist.AddTaskActivity.TASK_CREATED_TIME";;
    public static final String taskFinishedTimeKey = "com.main.todolist.AddTaskActivity.TASK_FINISHED_TIME";

    private TextView taskDescription;
    private RadioGroup radioGroup;
    private TimePicker timePicker;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        this.taskDescription = findViewById(R.id.addTaskTaskDescription);
        this.radioGroup = findViewById(R.id.addTaskPriorities);

        this.timePicker = findViewById(R.id.addTaskTaskTime);
        this.timePicker.setIs24HourView(true);

        this.datePicker = findViewById(R.id.addTaskTaskDate);
    }

    public void addTask(View view){
        // get description
        if (this.taskDescription.getText().toString().isEmpty()){
            Toast.makeText(this, "Task description cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        }

        String description = this.taskDescription.getText().toString();

        // get priority
        RadioButton checkedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        int priority = Task.TASK_PRIORITY_LOW;
        switch (checkedRadioButton.getText().toString()){
            case "!":
                break;
            case "!!":
                priority = Task.TASK_PRIORITY_MEDIUM;
                break;
            case "!!!":
                priority = Task.TASK_PRIORITY_HIGH;
                break;
        }

        // get finished time
        LocalDateTime finishedTime = LocalDateTime.of(
                datePicker.getYear(),
                datePicker.getMonth() + 1,
                datePicker.getDayOfMonth(),
                timePicker.getHour(),
                timePicker.getMinute());

        // get created time
        LocalDateTime createdTime = LocalDateTime.now();
        Intent intent = new Intent();
        intent.putExtra(taskDescriptionKey, description);
        intent.putExtra(taskPriorityKey, String.valueOf(priority));
        intent.putExtra(taskCreatedTimeKey, createdTime.toString());
        intent.putExtra(taskFinishedTimeKey, finishedTime.toString());

        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}