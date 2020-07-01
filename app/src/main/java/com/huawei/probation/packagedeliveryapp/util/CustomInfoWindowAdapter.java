package com.huawei.probation.packagedeliveryapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.Marker;
import com.huawei.probation.packagedeliveryapp.R;

public class CustomInfoWindowAdapter implements HuaweiMap.InfoWindowAdapter {
    private final View mWindow;

    public CustomInfoWindowAdapter(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindow = inflater.inflate(R.layout.custom_info_window, null);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtvTitle = mWindow.findViewById(R.id.txtv_titlee);
        TextView txtvSnippett = mWindow.findViewById(R.id.txtv_snippett);
        txtvTitle.setText(marker.getTitle());
        txtvSnippett.setText(marker.getSnippet());
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}