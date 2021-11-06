package com.main.todolist;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import database.Task;
import model.TaskViewModel;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "TASK_NOTIFICATION_CHANNEL";
    private ActivityResultLauncher<Intent> mStartForResult;

    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    private TaskViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent == null){
                        return;
                    }
                    addTaskFromIntent(intent);
                }
            }
        });


        createNotificationChannel();

        this.recyclerView = findViewById(R.id.tasks);
        this.model = new ViewModelProvider(this).get(TaskViewModel.class);
        this.viewAdapter = new ViewAdapter(new ArrayList<>(), getSupportFragmentManager(), model);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final Observer<List<Task>> tasksObserver = this::displayTasks;
        this.model.getAllTasks().observe(this, tasksObserver);

        Button addTaskBtn = findViewById(R.id.addTaskButton);
        addTaskBtn.setOnClickListener(this::addTaskBtnEvent);
    }

    private void addTaskFromIntent(Intent intent){
        String description = intent.getStringExtra(AddTaskActivity.taskDescriptionKey);
        int priority = Integer.parseInt(intent.getStringExtra(AddTaskActivity.taskPriorityKey));
        LocalDateTime createdTime = LocalDateTime.parse(intent.getStringExtra(AddTaskActivity.taskCreatedTimeKey));
        LocalDateTime finishedTime = LocalDateTime.parse(intent.getStringExtra(AddTaskActivity.taskFinishedTimeKey));
        Task task = new Task(description, priority, createdTime, finishedTime);
        model.insert(task);
    }



    public void addTaskBtnEvent(View view){
        mStartForResult.launch(new Intent(this, AddTaskActivity.class));
    }

    // https://developer.android.com/training/notify-user/build-notification
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sortByFinishedTime){
            this.model.setTaskSortingProperty(TaskViewModel.SORTED_BY_FINISH_TIME);
            return true;
        }
        else if (item.getItemId() == R.id.sortByPriority){
            this.model.setTaskSortingProperty(TaskViewModel.SORTED_BY_PRIORITY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayTasks(List<Task> tasks){
        viewAdapter.updateTasks(tasks);
        recyclerView.setAdapter(viewAdapter);
    }
}