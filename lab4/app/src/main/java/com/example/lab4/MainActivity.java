package com.example.lab4;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GifImageView progressBar =findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        TextView txtprogress = findViewById(R.id.textView3);
        txtprogress.setVisibility(View.GONE);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

    }

    public void GenerateClick(View view) {


        EditText txt = findViewById(R.id.editText);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(txt.getText().toString());
    }

    private class Permutation {

        FileWriter writer;

        private void Init()
        {
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "Wordlists");
                if (!root.exists()) {
                    root.mkdirs();
                }
                File gpxfile = new File(root, "generated.txt");
                writer = new FileWriter(gpxfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void CloseWriter()
        {
            try {
                writer.flush();
                writer.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        private void permute(String str, int l, int r)
        {
            if (l == r) {

                try {
                    writer.append(str);
                    writer.append("\n");
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else {
                for (int i = l; i <= r; i++) {
                    str = swap(str, l, i);
                    permute(str, l + 1, r);
                    str = swap(str, l, i);
                }
            }
        }

        private String swap(String a, int i, int j)
        {
            char temp;
            char[] charArray = a.toCharArray();
            temp = charArray[i];
            charArray[i] = charArray[j];
            charArray[j] = temp;
            return String.valueOf(charArray);
        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            Permutation permutation = new Permutation();
            permutation.Init();
            permutation.permute(params[0], 0, params[0].length() - 1);
            permutation.CloseWriter();
            return "";
        }


        @Override
        protected void onPostExecute(String result) {

            GifImageView progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.GONE);
            TextView txtprogress = findViewById(R.id.textView3);
            txtprogress.setVisibility(View.GONE);

            Toast.makeText(MainActivity.this, "Results Saved to file /Wordlists/generated.txt!", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected void onPreExecute() {

            GifImageView progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);
            TextView txtprogress = findViewById(R.id.textView3);
            txtprogress.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

}