package com.huawei.probation.packagedeliveryapp.network.model.responses;

import com.google.gson.annotations.SerializedName;
import com.huawei.probation.packagedeliveryapp.network.model.Routes;

import java.util.List;

public class DirectionsResponse {

    @SerializedName("routes")
    private List<Routes> routes;

    @SerializedName("returnCode")
    private String returnCode;

    @SerializedName("returnDesc")
    private String returnDesc;

    public DirectionsResponse(List<Routes> routes, String returnCode, String returnDesc) {
        this.routes = routes;
        this.returnCode = returnCode;
        this.returnDesc = returnDesc;
    }

    public List<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Routes> routes) {
        this.routes = routes;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }
}
