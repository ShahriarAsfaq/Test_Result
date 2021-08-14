package com.example.test_result;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {
    //String sheeturl="https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/";
    @POST("/medicalRepresentativeLogin")
    Call<Void>executemedicLogin(@Body HashMap<String, String> loginParameters);

    @POST("/coronaResultUpdate")
    Call<Void>executeresult(@Body HashMap<String, String> results);

    /*@GET("/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1wyWse6RwXHx8L8xB4yfC_HcVADCtEivhIlx7l30kX5k&sheet=Sheet1")
    Call<PatientData> getData();*/

    @POST("/googleSheetUpdate")
    Call<Void>executePData(@Body HashMap<Integer, String> pData);
}
