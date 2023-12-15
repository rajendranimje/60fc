package in.gov.cgg.alumni.trial.fcmMine;

import in.gov.cgg.alumni.fcmfinal.FinalFCMReq;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {


    @POST("send")
    Call<FCMResponse> sendMessage(@Body FinalFCMReq fcmRequest);


    @POST("send")
    Call<FCMResponse> sendMessage1(@Body FCMRequest fcmRequest);


// @POST("send")
//    Call<FCMResponse> sendMessage(@Header("Authorization") String username,
//                                  @Header("Content-Type") String ct,
//                     @Body FCMRequest fcmRequest);

}
