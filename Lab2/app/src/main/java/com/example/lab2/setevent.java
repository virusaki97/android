package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import static com.example.lab2.data_tools.*;

public class setevent extends AppCompatActivity {

    int day,month,year,hour,minute;
    long r_time = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setevent);

        Intent current_intent = getIntent();

        TimePicker  tp = findViewById(R.id.timePicker1);
        tp.setIs24HourView(true);

        if(current_intent.getBooleanExtra("UPDATE", false))
        {
            r_time = current_intent.getLongExtra("TIME", 0);
            tp = findViewById(R.id.timePicker1);
            tp.setHour(current_intent.getIntExtra("DATA_HOURS", 0));
            tp.setMinute(current_intent.getIntExtra("DATA_MINUTES", 0));
            EditText tmp = findViewById(R.id.editText);
            tmp.setText(current_intent.getStringExtra("DATA_MESSAGE"));
        }

        day = current_intent.getIntExtra("DATA_DAY", 0);
        month = current_intent.getIntExtra("DATA_MONTH", 0);
        year = current_intent.getIntExtra("DATA_YEAR", 0);

    }

    public void saveClicked(View view) {

        EditText txt = findViewById(R.id.editText);

        if(txt.getText().length() <= 0 || day <= 0 || month <=0 || year <= 0 )
        return;

        TimePicker  tp = findViewById(R.id.timePicker1);
        hour = tp.getHour();
        minute = tp.getMinute();

        long time = date_to_timestamp(year,month,day,hour,minute);
        long date = date_to_timestamp(year,month,day,0,0);

        if(r_time > 0) {
            remove_event(r_time);
        }

        add_event(time,date,txt.getText().toString());
        this.finish();
    }
}
