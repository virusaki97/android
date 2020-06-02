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
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.lab5.api_tools.Address;
import static com.example.lab5.api_tools.Birthday;
import static com.example.lab5.api_tools.Email;
import static com.example.lab5.api_tools.Fullname;
import static com.example.lab5.api_tools.Phone;
import static com.example.lab5.api_tools.Username;
import static com.example.lab5.api_tools.photo;


public class fragment_profile extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView name = view.findViewById(R.id.profile_name);
        TextView address = view.findViewById(R.id.profile_address);
        TextView birthday = view.findViewById(R.id.profile_birthday);
        TextView email = view.findViewById(R.id.profile_email);
        TextView username = view.findViewById(R.id.profile_username);
        TextView phone = view.findViewById(R.id.profile_phone);
        ImageView user_photo = view.findViewById(R.id.profile_photo);

        name.setText(Fullname);
        address.setText(Address);
        birthday.setText(Birthday);
        email.setText(Email);
        username.setText(Username);
        phone.setText(Phone);
        user_photo.setImageBitmap(photo);

    }
}
