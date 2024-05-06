package com.stuffer.stuffers.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MainLoanInterface<R extends Retrofit> {
    @POST(Constants.IS_USER_REGISTER_OR_PROFILE)
    Call<JsonObject> getIsUserLogin_Or_Profile(
            @Body JsonObject param,
            @Header("Authorization") String header
    );

    @POST(Constants.LOAN_CITY_LIST)
    Call<JsonObject> getCityList(
            @Body JsonObject param,
            @Header("Authorization") String header
    );

    @POST(Constants.LOAN_RIGESTER)
    Call<JsonObject> getLoanRegister(
            @Body JsonObject param,
            @Header("Authorization") String header
    );

    @POST(Constants.LOAN_SET_PROFILE)
    Call<JsonObject> setLoanProfilePhoto(
            @Body JsonObject param,
            @Header("Authorization") String header
    );

    @POST(Constants.LOAN_SET_ID)
    Call<JsonObject> setLoanIdCard(
            @Body JsonObject param,
            @Header("Authorization") String header
    );

    @POST(Constants.LOAN_SET_PAYSLIP)
    Call<JsonObject> setLoanPaySlip(
            @Body JsonObject param,
            @Header("Authorization") String header
    );


}
