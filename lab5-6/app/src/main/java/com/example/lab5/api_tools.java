package com.example.lab5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.lab5.MainActivity.mytag;

public class api_tools {

    static String Token = "";

    static String Fullname, Birthday,Email,Phone,Address,Username;
    static Bitmap photo;

    static class request_data
    {
        static String Name, Disease, Address, Description;
        static String DocID;

    }



    public static doctors getDoctorbyID()
    {
        OkHttpClient client  = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("81.180.72.17")
                .addPathSegment("api")
                .addPathSegment("Doctor")
                .addPathSegment("GetDoctor")
                .addPathSegment(request_data.DocID)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded/json")
                .addHeader("token", Token)
                .build();

        doctors doctor_obj = new doctors();
        try{
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()){


                JSONObject obj = new JSONObject(response.body().string());

                    doctor_obj.doctor_id.add(obj.getString("DocId"));
                    doctor_obj.doctor_name.add(obj.getString("FullName"));
                    doctor_obj.doctor_specialization.add(obj.getString("Specs"));
                    doctor_obj.doctor_address.add(obj.getString("Address"));
                    doctor_obj.doctor_rating.add(obj.getString("Stars"));
                    doctor_obj.doctor_photo.add(obj.getString("Photo"));
                    doctor_obj.doctor_about.add(obj.getString("About"));

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return doctor_obj;

    }




    public static doctors getDoctorList()
    {
        OkHttpClient client  = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("81.180.72.17")
                .addPathSegment("api")
                .addPathSegment("Doctor")
                .addPathSegment("GetDoctorList")
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded/json")
                .addHeader("token", Token)
                .build();

        doctors doctor_obj = new doctors();
        try{
            Response response = client.newCall(request).execute();

            Log.i(mytag, Token);

            if(response.isSuccessful()){


                JSONArray jArr = new JSONArray(response.body().string());

                for(int i=0;i<jArr.length();i++)
                {
                    doctor_obj.doctor_id.add(jArr.getJSONObject(i).getString("DocId"));
                    doctor_obj.doctor_name.add(jArr.getJSONObject(i).getString("FullName"));
                    doctor_obj.doctor_specialization.add(jArr.getJSONObject(i).getString("Specs"));
                    doctor_obj.doctor_address.add(jArr.getJSONObject(i).getString("Address"));
                    doctor_obj.doctor_rating.add(jArr.getJSONObject(i).getString("Stars"));
                    doctor_obj.doctor_photo.add(jArr.getJSONObject(i).getString("Photo"));

                }

            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return doctor_obj;

    }



    public static boolean updateProfileInfo()
    {
        OkHttpClient client  = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("81.180.72.17")
                .addPathSegment("api")
                .addPathSegment("Profile")
                .addPathSegment("GetProfile")
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded/json")
                .addHeader("token", Token)
                .build();

        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                JSONObject json = new JSONObject(response.body().string());

                Fullname = json.getString("FullName");
                Birthday = json.getString("Birthday");
                Email = json.getString("Email");
                Phone = json.getString("Phone");
                Address = json.getString("Address");
                Username = json.getString("Username");
                byte[] decodedString = Base64.decode(json.getString("Base64Photo"), Base64.DEFAULT);
                photo = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                return true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return false;

    }


    public static boolean addConsultation()
    {
        OkHttpClient client  = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("81.180.72.17")
                .addPathSegment("api")
                .addPathSegment("Doctor")
                .addPathSegment("AddConsultation")
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("Name", request_data.Name)
                .add("Disease", request_data.Disease)
                .add("DocId", request_data.DocID)
                .add("Description", request_data.Description)
                .add("Address", request_data.Address)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded/json")
                .addHeader("token", Token)
                .post(formBody)
                .build();

        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful() && response.code() == 200){
               return true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    public static boolean login(String email, String password)
    {
        OkHttpClient client  = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("81.180.72.17")
                .addPathSegment("api")
                .addPathSegment("Login")
                .addPathSegment("UserAuth")
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("Email", email)
                .add("Password", password)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded/json")
                .post(formBody)
                .build();

        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                JSONObject json = new JSONObject(response.body().string());

                if(json.getString("Status").equals("SUCCESS"))
                {
                    Token = json.getString("Message");
                    if(!Token.isEmpty())
                    {
                        return true;
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean signup(String name, String birthday, String email, String phone, String address, String username, String password, String photo)
    {
        OkHttpClient client  = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("81.180.72.17")
                .addPathSegment("api")
                .addPathSegment("Register")
                .addPathSegment("UserReg")
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("FullName", name)
                .add("Birthday", birthday)
                .add("Email", email)
                .add("Phone", phone)
                .add("Address", address)
                .add("Username", username)
                .add("Password", password)
                .add("Base64Photo", photo)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded/json")
                .post(formBody)
                .build();

        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful() && response.code() == 201){
                return true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
