package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class activity_welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark));

    }

    public void btnSignupClick(android.view.View view) {
        Intent signup = new Intent(activity_welcome.this, activity_signup.class);
        startActivity(signup);

    }

    public void btnLoginClickWelcome(View view) {
        Intent login = new Intent(activity_welcome.this, activity_login.class);
        startActivity(login);
    }
}
