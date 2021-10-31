package com.main.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.Task;
import model.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;

    private TaskViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recyclerView = findViewById(R.id.tasks);


        this.model = new ViewModelProvider(this).get(TaskViewModel.class);
        this.viewAdapter = new ViewAdapter(new ArrayList<>(), getSupportFragmentManager(), model);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final Observer<List<Task>> tasksObserver = new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                viewAdapter.updateTasks(tasks);
                recyclerView.setAdapter(viewAdapter);
            }
        };

        this.model.getAllTasks().observe(this, tasksObserver);

        Button addTaskBtn = findViewById(R.id.addTaskButton);
        addTaskBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTaskActivity.class));
        });
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
            case R.id.add_task_menu:
                startActivity(new Intent(this, AddTaskActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}