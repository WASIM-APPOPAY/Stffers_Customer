package com.stuffer.stuffers.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.stuffer.stuffers.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.map_marker, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);


        tvTitle.setText(marker.getTitle());


        return view;
    }
}