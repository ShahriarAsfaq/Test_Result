package com.example.test_result;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    List<String> nid_list,result_list;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button submit;
    EditText patient_nid;
    Button xlUrl;
    CheckBox legal_concent;

    String result,nid,url;
    private ApiServices apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radioGroup);
        patient_nid=findViewById(R.id.patient_nid);
        xlUrl=findViewById(R.id.xl);
        legal_concent=findViewById(R.id.checkBox);
        submit=findViewById(R.id.submit);
        submit.setEnabled(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServices = retrofit.create(ApiServices.class);


        nid_list= new ArrayList<>();
        result_list= new ArrayList<>();

        xlUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,sheet.class);
                startActivity(intent);
            }
        });
        legal_concent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(legal_concent.isChecked()){
                    submit.setEnabled(true);
                }
                else{
                    submit.setEnabled(false);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                result= radioButton.getText().toString();
                nid=patient_nid.getText().toString();

                Toast.makeText(MainActivity.this,url, Toast.LENGTH_SHORT).show();
                /*if(nid!=""&&result!="") {
                    nid_list.add(nid);
                    result_list.add(result);
                }*/
                getPosts();
            }
        });





    }
    private void getPosts() {
        HashMap<String, String> results = new HashMap<>();
            results.put("nid", nid);
            results.put("result", result);


        Call<Void> call = apiServices.executeresult(results);
        Toast.makeText(getApplicationContext(),"get post",Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(),"on response",Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Result submitted Successfully",
                            Toast.LENGTH_LONG).show();
                      Intent intent=new Intent(MainActivity.this,MainActivity.class);
                      startActivity(intent);

                } else if (response.code() == 400) {
                    Toast.makeText(MainActivity.this, "Something Went Wrong, Retry!",
                            Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }
}