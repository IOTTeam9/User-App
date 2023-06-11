package org.techtown.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationRetrofitInterface {

    @POST("/wifis/send")
    Call<NavigationResponse> sendLocation(@Body List<Location> myLocation);

}
