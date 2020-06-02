package com.example.lab2;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import static com.example.lab2.MainActivity.TAG;

public class data_tools
{
    static class obj_event
    {
        private String textmsg;
        private long time;
        private long date;

        public obj_event(long time, long date, String msg)
        {
            this.textmsg = msg;
            this.time = time;
            this.date = date;
        }

        public String getTextMsg()
        {
            return this.textmsg;
        }

        public long getTime()
        {
            return this.time;
        }

        public long getDate()
        {
            return this.date;
        }

    }

    static SQLiteDatabase mydatabase;

    public static long date_to_timestamp(int year, int month, int day, int hour, int minute)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day, hour, minute, 0);
        return calendar.getTimeInMillis();
    }

    public static void loadDB()
    {
        Context tmp =  App.getContext();
        String path = tmp.getFilesDir().getAbsolutePath() + "/events";
        mydatabase = SQLiteDatabase.openOrCreateDatabase(path, null, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS events_table(time int PRIMARY KEY, date int, message VARCHAR);");
        String query = "DELETE FROM events_table WHERE time < " + (new Date().getTime()/ 1000L);
        mydatabase.execSQL(query);
    }

    public static void add_event(long time, long date, String msg)
    {
        String logmsg = "Added:" + time;
        Log.i(TAG, logmsg);
        String query = "INSERT INTO events_table VALUES(" + (time / 1000L) + "," + date + ", '" + msg +"');";
        mydatabase.execSQL(query);
    }

    public static List<obj_event>  get_events(long time, long date, String msg, boolean all)
    {
        time /= 1000L;
        List<obj_event> event_list = new ArrayList<obj_event>();

        String query;

        if(date != 0)
            query = "SELECT time, date, message FROM events_table WHERE date = " + date;
        else
            if(time != 0)
                    query = "SELECT time, date, message FROM events_table WHERE time = " + time;
            else
                if(!all)
                   query = "SELECT time, date, message FROM events_table WHERE instr(message, '"+msg+"') > 0;";
                else
                    query = "SELECT time, date, message FROM events_table;";



        Cursor res = mydatabase.rawQuery(query, null);

        if(res.getCount() > 0)
        {
            res.moveToFirst();
            do
            {
                event_list.add(new obj_event(
                        res.getLong(0) * 1000,
                        res.getLong(1),
                        res.getString(2)
                ));
            }
            while (res.moveToNext());
        }
        res.close();


        Collections.sort(event_list, new Comparator<obj_event>()
        {
            @Override
            public int compare(obj_event lhs, obj_event rhs) {

                return Long.compare(lhs.getTime(),rhs.getTime());
            }
        });

        return event_list;
    }

    public static void remove_event(long time)
    {
        String query = "DELETE FROM events_table WHERE time = " + (time / 1000L);
        mydatabase.execSQL(query);
    }

}
