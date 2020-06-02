package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.lab5.MainActivity.mytag;
import static com.example.lab5.api_tools.addConsultation;
import static com.example.lab5.api_tools.updateProfileInfo;

public class activity_home extends AppCompatActivity implements fragment_home.fhClickInterface,
        fragment_doctorlist.fdClickInterface, fragment_doctorcontacts.fdcClickInterface {

    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomview = findViewById(R.id.bottomnav);
        bottomview.setItemIconTintList(null);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark));

        Toolbar toolbar = findViewById(R.id.toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.home));
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();

        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);

        }

        bottomview.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new fragment_home()).commit();


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch(menuItem.getItemId())
            {
                case R.id.nav_home:
                {
                    selectedFragment = new fragment_home();
                    mTitle.setText(getString(R.string.home));
                    break;
                }
                case R.id.nav_notification:
                {
                    selectedFragment = new fragment_approved();
                    mTitle.setText(getString(R.string.notification));
                    break;
                }
                case R.id.nav_plus:
                {
                    selectedFragment = new fragment_home();
                    mTitle.setText(getString(R.string.home));
                    break;
                }
                case R.id.nav_schedule:
                {
                    return false;
                }
                case R.id.nav_profile:
                {
                    updateProfileInfo();
                    selectedFragment = new fragment_profile();
                    mTitle.setText(getString(R.string.profile));
                    break;
                }
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void fhbuttonClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new fragment_doctorlist()).commit();
        mTitle.setText(getString(R.string.doctorlist));
    }

    @Override
    public void fdbuttonClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new fragment_doctorcontacts()).commit();
        mTitle.setText(getString(R.string.doctordetails));

    }

    @Override
    public void fdcbuttonClicked() {
        if(addConsultation()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new fragment_approved()).commit();
            mTitle.setText(getString(R.string.notification));
            BottomNavigationView tmpview = findViewById(R.id.bottomnav);
            tmpview.setSelectedItemId(R.id.nav_notification);
        }
        else
            Toast.makeText(this, "Failed To Add Consultation.", Toast.LENGTH_LONG).show();
    }

}
