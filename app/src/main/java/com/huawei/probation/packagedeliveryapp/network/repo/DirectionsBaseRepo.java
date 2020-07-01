package com.huawei.probation.packagedeliveryapp.network.repo;

import com.huawei.probation.packagedeliveryapp.network.RetrofitClientInstance;
import com.huawei.probation.packagedeliveryapp.network.api.DirectionsApis;

public class DirectionsBaseRepo {
    private static DirectionsApis directionsApis = null;

    private DirectionsBaseRepo() {
        setMainApis();
    }

    public static DirectionsApis getInstance(){
        if(directionsApis == null)
            new DirectionsBaseRepo();
        return directionsApis;
    }

    private void setMainApis(){
        directionsApis = RetrofitClientInstance.createService(DirectionsApis.class);
    }
}
