package com.example.lab5;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class fragment_home extends Fragment {

    public interface fhClickInterface {
        void fhbuttonClicked();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_home, container, false);

        Button button = view.findViewById(R.id.fh_btn_request);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText name = view.findViewById(R.id.fh_name);
                EditText disease = view.findViewById(R.id.fh_disease);
                EditText address = view.findViewById(R.id.fh_address);
                EditText description = view.findViewById(R.id.fh_description);

                api_tools.request_data.Name = name.getText().toString();
                api_tools.request_data.Disease = disease.getText().toString();
                api_tools.request_data.Address = address.getText().toString();
                api_tools.request_data.Description = description.getText().toString();

                ((fhClickInterface ) getActivity()).fhbuttonClicked();
            }
        });

        return view;
    }

}
