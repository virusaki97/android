package com.example.lab5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Menu;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import static com.example.lab5.api_tools.*;

import static com.example.lab5.MainActivity.mytag;

public class activity_signup extends AppCompatActivity  {
    public static final int PICK_IMAGE = 1;
    String base64img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark));

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.register_toolbar_title));
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();

        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void btnSignupComplete(View view) {
        EditText name = findViewById(R.id.signup_name);
        EditText birthday = findViewById(R.id.signup_birthday);
        EditText email = findViewById(R.id.signup_email);
        EditText phone = findViewById(R.id.signup_phone);
        EditText address = findViewById(R.id.signup_address);
        EditText password = findViewById(R.id.signup_password);

        String szEmail = email.getText().toString();
        String username = szEmail.substring(0, szEmail.indexOf('@'));

        if(signup( name.getText().toString(), birthday.getText().toString(), szEmail,
              phone.getText().toString(), address.getText().toString(), username,
              password.getText().toString(), base64img))
        {
            Toast.makeText(this, "Your Account Has been Created!", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            }, Toast.LENGTH_SHORT);
        }
        else
        {
            Toast.makeText(this, "Error Creating Account. Check input Data.", Toast.LENGTH_SHORT).show();

        }



    }

    public void addphotosclick(View view) {

        chooseImage();

    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.addphotos);
                imageView.setImageBitmap(bitmap);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                base64img = encodeToBase64(Bitmap.createScaledBitmap(bitmap, 40, 40, true));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

}
