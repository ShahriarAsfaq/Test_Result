package com.example.test_result;

import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {
    @POST("/medicalRepresentativeLogin")
    Call<Void>executemedicLogin(@Body HashMap<String, String> loginParameters);

    @POST("/coronaResultUpdate")
    Call<Void>executeresult(@Body HashMap<String, String> results);
}
