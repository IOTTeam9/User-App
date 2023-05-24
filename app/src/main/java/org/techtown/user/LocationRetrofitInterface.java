package org.techtown.user;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationRetrofitInterface {

    @POST("/wifis/create")
    Call<NavigationResponse> sendLocation(@Body Location myLocation);

}
