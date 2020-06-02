package com.example.lab5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class doctorlist_adapter extends ArrayAdapter<String> {

        Context context;

        List<String> szPhoto = new ArrayList<>();
        List<String> szName = new ArrayList<>();
        List<String> szSpecialization = new ArrayList<>();
        List<String> szAddress = new ArrayList<>();
        List<String> szRating = new ArrayList<>();
     List<String> szID = new ArrayList<>();


    doctorlist_adapter (Context c, List<String> szPhoto, List<String> szName,  List<String> szSpecialization, List<String> szAddress,List<String> szRating,List<String> szID ) {
            super(c, R.layout.doctor_listitem, R.id.doctor_photo, szPhoto);
            this.context = c;
            this.szPhoto = szPhoto;
            this.szName = szName;
            this.szSpecialization = szSpecialization;
            this.szAddress = szAddress;
            this.szRating = szRating;
            this.szID = szID;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.doctor_listitem, parent, false);

            ImageView photo = row.findViewById(R.id.doctor_photo);
            TextView name = row.findViewById(R.id.doctor_name);
            TextView specialization = row.findViewById(R.id.doctor_specialization);
            TextView address = row.findViewById(R.id.doctor_address);
            TextView rating = row.findViewById(R.id.doctor_rating);

            row.setTag(szID.get(position));

            name.setText(szName.get(position));
            specialization.setText(szSpecialization.get(position));
            address.setText(szAddress.get(position));
            rating.setText(szRating.get(position));

            byte[] decodedString = Base64.decode(szPhoto.get(position), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photo.setImageBitmap(decodedByte);

            return row;
        }

}
