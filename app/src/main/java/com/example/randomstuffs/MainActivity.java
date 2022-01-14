package com.example.randomstuffs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvQuote, tvLoading, tvNext, tvPrev, tvQuoteNo;
    List<String> quoteList;
    String url;
    int currentQuote, totalQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiating the string list
        quoteList = new ArrayList<String>();
        url = "https://catfact.ninja/fact";

        tvQuote = findViewById(R.id.tv_main_quote);
        tvLoading = findViewById(R.id.tv_main_loading);
        tvNext = findViewById(R.id.tv_main_next);
        tvPrev = findViewById(R.id.tv_main_prev);
        tvQuoteNo = findViewById(R.id.tv_main_quote_no);
        currentQuote = 0;
        totalQuote = 0;

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentQuote == totalQuote){
                    getNextQuote();
                }else{
                    currentQuote++;
                    tvQuote.setText(quoteList.get(currentQuote-1));
                    updateQuoteNo();
                }
            }
        });

        tvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentQuote > 1){
                    currentQuote--;
                    tvQuote.setText(quoteList.get(currentQuote-1));
                    updateQuoteNo();
                }else{
                    Toast.makeText(MainActivity.this, "No previous quote!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //function on app start
        tvLoading.setVisibility(View.VISIBLE);
        getNextQuote();

    }

    boolean connected(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            //not connected to the internet
            connected = false;
        }
        return connected;
    }

    void updateQuoteNo(){
        tvQuoteNo.setText(new StringBuilder().append(currentQuote).append("/").append(totalQuote).toString());
    }

    void getNextQuote(){

        if(!connected()){
            Toast.makeText(MainActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            tvLoading.setVisibility(View.INVISIBLE);
            return;
        }

        final String[] result = new String[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    result[0] = response.getString("fact");

                    totalQuote++;
                    currentQuote++;

                    tvQuote.setText(result[0]);
                    quoteList.add(result[0]);


                    updateQuoteNo();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

                tvLoading.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                tvLoading.setVisibility(View.INVISIBLE);
            }
        });

        tvLoading.setVisibility(View.VISIBLE);
        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Bhak bsdk " + new String(Character.toChars(0x1F621)), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onUserLeaveHint() {
        Toast.makeText(MainActivity.this, "Bhag gaye bsdk " + new String(Character.toChars(0x1F923)), Toast.LENGTH_SHORT).show();
    }
}