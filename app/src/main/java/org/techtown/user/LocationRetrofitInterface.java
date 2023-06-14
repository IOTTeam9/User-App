package org.techtown.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LocationRetrofitInterface {

//    @POST("/location/send")
//    Call<NavigationResponse> sendLocation(@Body List<Location> myLocation);

    @POST("/location/send")
    Call<ReceiveResponse> sendLocation(@Body List<Location> myLocation);

}
