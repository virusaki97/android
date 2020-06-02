package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.lab5.api_tools.*;

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnLoginClick(View view) {
        EditText email = findViewById(R.id.login_email);
        EditText password = findViewById(R.id.login_password);
        if(login(email.getText().toString(), password.getText().toString()))
        {
            Intent home = new Intent(activity_login.this, activity_home.class);
            startActivity(home);
            finish();
        }

    }

    public void btnsignupfromlogin(View view) {

        Intent signup = new Intent(activity_login.this, activity_signup.class);
        startActivity(signup);
    }
}
