package com.huawei.probation.packagedeliveryapp.network.api;

import com.huawei.probation.packagedeliveryapp.network.model.requests.DirectionsRequest;
import com.huawei.probation.packagedeliveryapp.network.model.responses.DirectionsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DirectionsApis {
    @POST("routeService/{type}")
    public Call<DirectionsResponse> getDirectionsWithType(@Path(value = "type",encoded = true) String type, @Body DirectionsRequest directionsRequest);
}
