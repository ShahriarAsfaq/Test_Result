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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Workbook workbook;
    AsyncHttpClient asyncHttpClient;
    List<String> nid_list,result_list;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button submit;
    EditText patient_nid;
    EditText xlUrl;
    CheckBox legal_concent;

    String result,nid,url;
    private ApiServices apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radioGroup);
        patient_nid=findViewById(R.id.patient_nid);
        xlUrl=findViewById(R.id.xlUrl);
        legal_concent=findViewById(R.id.checkBox);
        submit=findViewById(R.id.submit);
        submit.setEnabled(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServices = retrofit.create(ApiServices.class);

        asyncHttpClient = new AsyncHttpClient();
        nid_list= new ArrayList<>();
        result_list= new ArrayList<>();


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
                url = xlUrl.getText().toString();
                if(nid!=""&&result!="") {
                    nid_list.add(nid);
                    result_list.add(result);
                }

                asyncHttpClient.get(url, new FileAsyncHttpResponseHandler(MainActivity.this) {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        Toast.makeText(MainActivity.this, "Error in Downloading Excel File", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setGCDisabled(true);
                        if(file != null){
                            Toast.makeText(MainActivity.this, "Success, DO something with the file.", Toast.LENGTH_SHORT).show();

                            try {
                                workbook = Workbook.getWorkbook(file);
                                Sheet sheet = workbook.getSheet(0);
                                //Cell[] row = sheet.getRow(1);
                                //text.setText(row[0].getContents());
                                for(int i = 0;i< sheet.getRows();i++){
                                    Cell[] row = sheet.getRow(i);
                                    nid_list.add(row[0].getContents());
                                    result_list.add( row[1].getContents());
                                    //thumbImages.add(row[2].getContents());
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (BiffException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                Toast.makeText(MainActivity.this,result_list.get(0), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this,nid_list.get(2), Toast.LENGTH_SHORT).show();
                getPosts();

            }
        });





    }
    private void getPosts() {
        HashMap<String, String> results = new HashMap<>();
        for(int i=0;i<nid_list.size();i++) {
            results.put("nid"+i, nid_list.get(i));
            results.put("result"+i, result_list.get(i));
        }

        Call<Void> call = apiServices.executeresult(results);
        Toast.makeText(getApplicationContext(),"get post",Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getApplicationContext(),"on response",Toast.LENGTH_SHORT).show();
                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Result submitted Successfully",
                            Toast.LENGTH_LONG).show();
                   // Intent intent=new Intent(MainActivity.this,MainActivity.class);
                  //  startActivity(intent);

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