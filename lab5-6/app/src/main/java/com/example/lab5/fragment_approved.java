package com.example.lab5;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.lab5.api_tools.getDoctorbyID;


public class fragment_approved extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_approved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView name = view.findViewById(R.id.fdp_name);
        TextView address = view.findViewById(R.id.fdp_address);
        TextView disease = view.findViewById(R.id.fdp_disease);
        TextView description = view.findViewById(R.id.fdp_description);
        ListView doctor_view =  view.findViewById(R.id.fdp_listview);

        name.setText(api_tools.request_data.Name);
        address.setText(api_tools.request_data.Address);
        disease.setText(api_tools.request_data.Disease);
        description.setText(api_tools.request_data.Description);

        doctors doctor = getDoctorbyID();

        doctorlist_adapter adapter = new doctorlist_adapter(this.getContext(), doctor.doctor_photo,doctor.doctor_name, doctor.doctor_specialization,
                doctor.doctor_address, doctor.doctor_rating, doctor.doctor_id);

        doctor_view.setAdapter(adapter);

    }
}
