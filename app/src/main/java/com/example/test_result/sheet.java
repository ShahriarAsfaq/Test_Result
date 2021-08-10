package com.example.test_result;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class sheet extends AppCompatActivity {
    Intent intent= getIntent();
    WebView webView;
    Button submit;
    private ApiServices apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        webView= (WebView)findViewById(R.id.webView);
        submit=(Button) findViewById(R.id.submitsheet);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServices = retrofit.create(ApiServices.class);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://docs.google.com/spreadsheets/d/1wyWse6RwXHx8L8xB4yfC_HcVADCtEivhIlx7l30kX5k/edit?fbclid=IwAR1CABAv1dvS2x5erFOX2bSoLaEksJB32mQdLaVAlL76r9mxVCh6fgK4uhk#gid=0");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geturl();
            }
        });

    }
    private void geturl() {
        HashMap<String, String> url = new HashMap<>();
            url.put("url", "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1wyWse6RwXHx8L8xB4yfC_HcVADCtEivhIlx7l30kX5k&sheet=Sheet1");


        Call<Void> call = apiServices.executeurl(url);
        Toast.makeText(getApplicationContext(),"get post",Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(),"on response",Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    Toast.makeText(sheet.this, "Result submitted Successfully",
                            Toast.LENGTH_LONG).show();


                } else if (response.code() == 400) {
                    Toast.makeText(sheet.this, "Something Went Wrong, Retry!",
                            Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(sheet.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }
}