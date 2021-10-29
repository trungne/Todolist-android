package com.main.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import database.Task;
import model.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    private LinearLayout tasksLayout;
    ViewGroup.LayoutParams params;

    private TaskViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tasksLayout = findViewById(R.id.tasks);

        // params for task display
        this.params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        findViewById(R.id.AddTaskButton).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivity(intent);
        });

        this.model = new ViewModelProvider(this).get(TaskViewModel.class);

        final Observer<List<Task>> tasksObserver = new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                tasksLayout.removeAllViews();
                for (Task task: tasks){
                    String description = task.getTaskDescription();
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(description);
                    tasksLayout.addView(textView);
                }
            }
        };

        this.model.getAllTasks().observe(this, tasksObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.calendar_menu:
                Toast.makeText(this, "Calendar selected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}