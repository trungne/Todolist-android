package com.main.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addTask(View view){
        TextView taskDescription = findViewById(R.id.taskDescription);
        RadioGroup radioGroup = findViewById(R.id.priorities);
        RadioButton checkedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        TimePicker timePicker = findViewById(R.id.timePicker);
        DatePicker datePicker = findViewById(R.id.datePicker);
        String time = timePicker.getHour() + ": " + timePicker.getMinute();
        String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
//        Toast.makeText(this, taskDescription.getText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, checkedRadioButton.getText().toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
    }
}