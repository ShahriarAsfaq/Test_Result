package com.example.test_result;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class sheet extends AppCompatActivity {
    Intent intent= getIntent();
    WebView webView;
    Button submitsheet;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> statuslist= new ArrayList<>();
    private ApiServices apiServices;
    //private ApiServices apiServices1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        webView= (WebView)findViewById(R.id.webView);
        submitsheet=(Button) findViewById(R.id.submitsheet);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServices = retrofit.create(ApiServices.class);



        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://docs.google.com/spreadsheets/d/1wyWse6RwXHx8L8xB4yfC_HcVADCtEivhIlx7l30kX5k/edit?fbclid=IwAR1CABAv1dvS2x5erFOX2bSoLaEksJB32mQdLaVAlL76r9mxVCh6fgK4uhk#gid=0");

        submitsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getSheet();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });*/
                new GetDataTask().execute();

            }

        });
    }
    class GetDataTask extends AsyncTask<Void, Void, Void> {

        //ProgressDialog dialog;
        int jIndex;
        int x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

            x=list.size();

            if(x==0)
                jIndex=0;
            else
                jIndex=x;

           /* dialog = new ProgressDialog(test.MainActivity.this);
            dialog.setTitle("Hey Wait Please..."+x);
            dialog.setMessage("I am getting your JSON");
            dialog.show();*/
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            JSONObject jsonObject = JSONParser.getDataFromWeb();

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {
                    /**
                     * Check Length...
                     */
                    if(jsonObject.length() > 0) {
                        /**
                         * Getting Array named "contacts" From MAIN Json Object
                         */
                        JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);

                        /**
                         * Check Length of Array...
                         */


                        int lenArray = array.length();
                        if(lenArray > 0) {
                            for( ; jIndex < lenArray; jIndex++) {

                                /**
                                 * Creating Every time New Object
                                 * and
                                 * Adding into List
                                 */
                                PatientData model = new PatientData();

                                /**
                                 * Getting Inner Object from contacts array...
                                 * and
                                 * From that We will get Name of that Contact
                                 *
                                 */
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String nid = innerObject.getString(Keys.KEY_NID);
                                String status = innerObject.getString(Keys.KEY_STATUS);

                                /**
                                 * Getting Object from Object "phone"
                                 */
                                //JSONObject phoneObject = innerObject.getJSONObject(Keys.KEY_PHONE);
                                //String phone = phoneObject.getString(Keys.KEY_MOBILE);

                                model.setNID(nid);
                                model.setStatus(status);


                                /**
                                 * Adding name and phone concatenation in List...
                                 */

                                list.add(nid);
                                list.add(status);
                                Log.d("nid",list.toString());
                                Log.d("status",list.toString());
                                if(jIndex==lenArray-1){
                                    geturl();
                                }
                            }

                        }
                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(JSONParser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

    }

    private void geturl() {
        Log.d("listsize",String.valueOf(list.size()));
        HashMap<Integer, String> pData = new HashMap<>();
            for(int i=0;i<list.size();i++){
                pData.put(i,list.get(i));
                Log.d("list",list.get(i).toString());
                if(i==list.size()-1){
                    Call<Void> call = apiServices.executePData(pData);
                    //Toast.makeText(getApplicationContext(),"get post",Toast.LENGTH_SHORT).show();
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                           // Toast.makeText(getApplicationContext(),"on response",Toast.LENGTH_SHORT).show();
                            if (response.code() == 200) {
                                Toast.makeText(sheet.this, "Result submitted Successfully",
                                        Toast.LENGTH_LONG).show();


                            } else if (response.code() == 400) {
                              //  Toast.makeText(sheet.this, "Something Went Wrong, Retry!",
                                //        Toast.LENGTH_LONG).show();
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



    }

}