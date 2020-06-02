package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    public String apiKey = "c6729dc0-2a8f-470d-82f9-1cbf9a80e9d3";
    final static String TAG = "[DEBUG]";

    public class MyAdapter extends ArrayAdapter<String> {

        Context context;

        List<String> szCount = new ArrayList<>();
        List<String> szName = new ArrayList<>();
        List<String> szSymbol = new ArrayList<>();
        List<String> szPrice = new ArrayList<>();


        MyAdapter (Context c, List<String> szCount, List<String> szName,  List<String> szSymbol, List<String> szPrice) {
            super(c, R.layout.list_items, R.id.textViewCount, szCount);
            this.context = c;
            this.szCount = szCount;
            this.szName = szName;
            this.szSymbol = szSymbol;
            this.szPrice = szPrice;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_items, parent, false);

            TextView mycount = row.findViewById(R.id.textViewCount);
            TextView myname = row.findViewById(R.id.textViewName);
            TextView mysymbol = row.findViewById(R.id.textViewSymbol);
            TextView myprice = row.findViewById(R.id.textViewPrice);

            if(szCount.get(position).equals("#"))
            {
                mycount.setTypeface(null, Typeface.BOLD);
                myname.setTypeface(null, Typeface.BOLD);
                mysymbol.setTypeface(null, Typeface.BOLD);
                myprice.setTypeface(null, Typeface.BOLD);
                row.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            mycount.setText(szCount.get(position));
            myname.setText(szName.get(position));
            mysymbol.setText(szSymbol.get(position));
            myprice.setText(szPrice.get(position));



            return row;
        }
    }

    public void buttonUpdateClick(View view) {
        SearchView tmp_search = findViewById(R.id.searchView);
        String query = tmp_search.getQuery().toString();
        if(query.length() > 0)
            updateList(query);
        else
            updateList("");
    }

    public void updatelistview(String myResponse, String search_param)
    {
        try {

            ListView currency_list  = findViewById(R.id.listView);

            JSONObject json = new JSONObject(myResponse);


            JSONArray currency_array = json.getJSONArray("data");

            List<String> count = new ArrayList<>();
            List<String> name = new ArrayList<>();
            List<String> symbol =  new ArrayList<>();
            List<String> price =  new ArrayList<>();

            double tmp_price;
            String tmpname, tmpsymbol;

            count.add("#");
            name.add("Name");
            symbol.add("Symbol");
            price.add("Price");

            for(int i = 0; i < currency_array.length(); i++)
            {
                JSONObject tmpobj = currency_array.getJSONObject(i);
                tmpname = tmpobj.getString("name");
                tmpsymbol =  tmpobj.getString("symbol");

                if(tmpname.toLowerCase().contains(search_param) || tmpsymbol.toLowerCase().contains(search_param))
                {
                    count.add(String.valueOf(i + 1));

                    name.add(tmpname);
                    symbol.add(tmpsymbol);

                    tmp_price = Double.valueOf(tmpobj.getJSONObject("quote").getJSONObject("USD").getString("price"));
                    if (tmp_price < 1)
                        price.add(String.format(Locale.ENGLISH, "$%.6f", tmp_price));
                    else
                        price.add(String.format(Locale.ENGLISH, "$%.2f", tmp_price));
                }
            }

            MyAdapter adapter = new MyAdapter(MainActivity.this, count,name,symbol, price);

            currency_list.setAdapter(adapter);

        }catch(JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void updateList(final String search_param)
    {

        OkHttpClient client  = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("pro-api.coinmarketcap.com")
                .addPathSegment("v1")
                .addPathSegment("cryptocurrency")
                .addPathSegment("listings")
                .addPathSegment("latest")
                .addQueryParameter("start", "1")
                .addQueryParameter("limit", "50")
                .addQueryParameter("convert", "USD")
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Accept", "application/json")
                .addHeader("X-CMC_PRO_API_KEY", apiKey)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.i(TAG, "failed");
            }


            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful() && response.body() != null)
                {
                    final String myResponse = response.body().string();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            updatelistview(myResponse, search_param);

                        }
                    });

                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateList("");

        SearchView searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0)
                {
                    updateList(newText);
                }
                else
                {
                    updateList("");
                }
                return false;
            }
        });


    }

}
