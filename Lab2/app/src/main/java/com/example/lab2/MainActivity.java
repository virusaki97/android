package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import static android.provider.Settings.NameValueTable.VALUE;
import static com.example.lab2.data_tools.*;
import static java.sql.Types.TIMESTAMP;


public class MainActivity extends AppCompatActivity
{
    public static MainActivity mThis;

    public void UpdateTheList()
    {
        ListView lst = findViewById(R.id.eventlist);
        updateListView(lst, "", true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadDB();
                    ListView lst = findViewById(R.id.eventlist);
                    updateListView(lst, "", true);
                }
                break;
        }
    }

    public class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String szTime[];
        String szDate[];
        String szMessage[];
        String szTag[];

        MyAdapter (Context c, String szTime[], String szDate[],  String szMessage[], String szTag[]) {
            super(c, R.layout.list_items, R.id.textViewTime, szTime);
            this.context = c;
            this.szTime = szTime;
            this.szDate = szDate;
            this.szMessage = szMessage;
            this.szTag = szTag;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_items, parent, false);
            TextView mytime = row.findViewById(R.id.textViewTime);
            TextView mydate = row.findViewById(R.id.textViewDate);
            TextView mytext = row.findViewById(R.id.textViewMessage);

            mytime.setText(szTime[position]);
            mydate.setText(szDate[position]);
            mytext.setText(szMessage[position]);
            row.setTag( szTag[position]);
            return row;
        }
    }

    public static final String TAG = "DEBUG";

    Calendar cl;

    long g_time = 0;
    String g_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        cl = Calendar.getInstance();
        cl.setTimeInMillis(new Date().getTime());



        final CalendarView cl_view = findViewById(R.id.calendarView);
        cl_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cl.set(year,month,dayOfMonth);
            }
        });


        Intent intent = new Intent(this, data_processor.class);
        startService(intent);


        final SearchView search = findViewById(R.id.searchview);


        final ListView  eventlist = findViewById(R.id.eventlist);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        else { loadDB();  updateListView(eventlist, "", true);}

        eventlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                arg1.setSelected(true);

                g_time = Long.parseLong(arg1.getTag().toString());
                TextView tmp = arg1.findViewById(R.id.textViewMessage);
                g_msg = tmp.getText().toString();
             }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                updateListView(eventlist, newText, false);

                return true;
            }
        });
        mThis = this;

    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView lst = findViewById(R.id.eventlist);
        updateListView(lst, "", true);
    }

    public  void updateListView(ListView list, String newText, boolean all)
    {
        List<data_tools.obj_event> event_array = get_events(0 , 0, newText, all);
        int len = event_array.size();
        String[] time= new String[len];
        String[] date = new String[len];
        String[] msg = new String[len];
        String[] tag = new String[len];

        long tmp;

        for(int i = 0; i < len; i++)
        {
            tmp = event_array.get(i).getTime();
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(tmp);
            date[i] = DateFormat.format("dd-MM-yyyy", cal).toString();
            time[i] = DateFormat.format("HH:mm", cal).toString();
            msg[i] = event_array.get(i).getTextMsg();
            tag[i] = Long.toString(tmp);
        }


        MyAdapter adapter = new MyAdapter(MainActivity.this, date,time,msg, tag);
        list.setAdapter(adapter);
    }


    public void addClicked(View view) {

        Intent open_setevent = new Intent(this, setevent.class);
        open_setevent.putExtra("DATA_DAY", cl.get(Calendar.DAY_OF_MONTH));
        open_setevent.putExtra("DATA_MONTH", cl.get(Calendar.MONTH));
        open_setevent.putExtra("DATA_YEAR", cl.get(Calendar.YEAR));

        startActivity(open_setevent);
    }

    public void removeClicked(View view)
    {
        if(g_time == 0) return;
        remove_event(g_time);
        ListView lst = findViewById(R.id.eventlist);
        updateListView(lst, "", true);
    }
    public void updateClicked(View view)
    {
        if(g_time == 0) return;
        Intent open_setevent = new Intent(this, setevent.class);
        Calendar tmpcal = new GregorianCalendar();
        tmpcal.setTimeInMillis(g_time);

        open_setevent.putExtra("UPDATE", true);
        open_setevent.putExtra("TIME", g_time);
        open_setevent.putExtra("DATA_DAY", cl.get(Calendar.DAY_OF_MONTH));
        open_setevent.putExtra("DATA_MONTH", cl.get(Calendar.MONTH));
        open_setevent.putExtra("DATA_YEAR", cl.get(Calendar.YEAR));
        open_setevent.putExtra("DATA_HOURS", tmpcal.get(Calendar.HOUR_OF_DAY));
        open_setevent.putExtra("DATA_MINUTES", tmpcal.get(Calendar.MINUTE));
        open_setevent.putExtra("DATA_MESSAGE", g_msg);

        startActivity(open_setevent);
    }


}
