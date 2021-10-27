package com.main.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;

import database.Task;
import views.TaskViewModel;

public class AddTaskActivity extends AppCompatActivity {
    private TextView taskDescription;
    private RadioGroup radioGroup;
    private TimePicker timePicker;
    private DatePicker datePicker;

    TaskViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        this.taskDescription = findViewById(R.id.taskDescription);
        this.radioGroup = findViewById(R.id.priorities);

        this.timePicker = findViewById(R.id.timePicker);
        this.timePicker.setIs24HourView(true);

        this.datePicker = findViewById(R.id.datePicker);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addTask(View view){
        // get description
        if (this.taskDescription.getText().toString().isEmpty()){
            Toast.makeText(this, "Task description cannot be empty!", Toast.LENGTH_LONG).show();
            finish();
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

        Task task = new Task(description, priority, createdTime, finishedTime);
//        viewModel.insert(task);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}