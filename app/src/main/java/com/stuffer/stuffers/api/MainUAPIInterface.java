package com.stuffer.stuffers.api;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MainUAPIInterface<R extends Retrofit> {

    /**
     * @param param
     * @Below to get JWE Token API
     */
    @HTTP(method = "POST", path = Constants.POST_JWE, hasBody = true)

    @Headers({

            "Content-Type:application/json;charset=utf-8",
            "requestPath: /scis/switch/cardenrollment"
    })
    Call<JsonObject> getJWEToken(@Body RequestBody param);

    /**
     * @param param
     * @Below to get JWS Token
     */

    @HTTP(method = "POST", path = Constants.POST_JWS, hasBody = true)
    @Headers({

            "Content-Type:application/json;charset=utf-8",
            "requestPath: /scis/switch/cardenrollment"
    })
    Call<JsonObject> getJWSToken(@Body RequestBody param);

    /**
     * @Below for get card enrollment
     */

    @HTTP(method = "POST", path = Constants.POST_CARD_ENROLLMENT, hasBody = true)
    @Headers({
            "Content-Type:application/json;charset=utf-8",
            "requestPath: /scis/switch/cardenrollment"
    })
    Call<JsonObject> getCardEnrollment(@Body RequestBody param, @Header("authToken") String xAccessToken, @Header("accountNumber") String accountNumber);



    @Headers({
            "Accept: text/plain",
            "Content-Type: text/plain"
    })
    @POST(Constants.POST_JWE_CONTENT)
    Call<JsonObject> getJwsContent(@Body RequestBody param);

    @POST(Constants.POST_ENROLL_INFO)
    Call<JsonObject> getSavedCardUnion(@Header("accountNumber") String accno);

    @POST(Constants.POST_JWE_CONTENT)
    Call<JsonObject> getUnmaskRequestBody(@Body RequestBody body);

    @POST(Constants.CARD_FACE_DOWNLOAD)
    Call<JsonObject> getCardImage(@Body JsonObject param, @Header("requestPath") String requestPath, @Header("authToken") String authToken, @Header("Content-Type") String contentType);

    @POST(Constants.POST_JWS)
    Call<JsonObject> getJWSToken(@Body JsonObject param, @Header("requestPath") String requestPath, @Header("Content-Type") String contentType);

    @POST(Constants.POST_QR_BAR_CODE)
    Call<JsonObject> getQRandBardCode(@Body JsonObject param, @Header("requestPath") String requestPath, @Header("authToken") String authToken, @Header("Content-Type") String contentType);

    @POST(Constants.POST_JWS)
    Call<JsonObject> getJWSTokenImage(@Body JsonObject param, @Header("requestPath") String requestPath, @Header("Content-Type") String contentType);


}
