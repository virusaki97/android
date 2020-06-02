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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.lab5.api_tools.*;

public class fragment_doctorlist extends Fragment {

    public interface fdClickInterface {
        void fdbuttonClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_doctorlist, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView doctorlist = view.findViewById(R.id.doctorlist_listview);

        doctors lst = getDoctorList();

        doctorlist_adapter adapter = new doctorlist_adapter(this.getContext(), lst.doctor_photo,lst.doctor_name, lst.doctor_specialization,
                lst.doctor_address, lst.doctor_rating, lst.doctor_id);

        doctorlist.setAdapter(adapter);

        doctorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                request_data.DocID = arg1.getTag().toString();
                ((fragment_doctorlist.fdClickInterface) getActivity()).fdbuttonClicked();
            }
        });
    }
}
