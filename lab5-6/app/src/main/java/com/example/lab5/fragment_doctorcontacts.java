package com.example.lab5;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.List;

import static com.example.lab5.api_tools.getDoctorList;
import static com.example.lab5.api_tools.getDoctorbyID;

public class fragment_doctorcontacts extends Fragment implements OnMapReadyCallback{

    MapView mapView;
    GoogleMap map;

    double latitude = 0;
    double longitude = 0;

    public interface fdcClickInterface {
        void fdcbuttonClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_doctorcontacts, container, false);


        Button button = view.findViewById(R.id.dc_btn_request);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((fragment_doctorcontacts.fdcClickInterface) getActivity()).fdcbuttonClicked();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        doctors doctor = getDoctorbyID();
        ListView doctorview = view.findViewById(R.id.dc_listview);

        doctorlist_adapter adapter = new doctorlist_adapter(this.getContext(), doctor.doctor_photo,doctor.doctor_name, doctor.doctor_specialization,
                doctor.doctor_address, doctor.doctor_rating, doctor.doctor_id);

        doctorview.setAdapter(adapter);

        TextView aboutdoctor = view.findViewById(R.id.dc_about);
        aboutdoctor.setText(doctor.doctor_about.get(0));

        TextView doctoraddress = view.findViewById(R.id.dc_address);
        doctoraddress.setText(doctor.doctor_address.get(0));


        mapView = view.findViewById(R.id.dc_mapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(this);


        Geocoder geocoder = new Geocoder(this.getContext());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(doctor.doctor_address.get(0), 1);
            if(addresses.size() > 0) {
                latitude= addresses.get(0).getLatitude();
                longitude= addresses.get(0).getLongitude();
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }


        LatLng doctor_location = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(  doctor_location , 15);
        map.addMarker(new MarkerOptions().position(doctor_location));
        map.animateCamera(cameraUpdate);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
