package com.sequoiahack.jarvis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sequoiahack.jarvis.R;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

/**
 * Created by aswani on 30/08/15.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{


    private GoogleMap mGoogleMap;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mGoogleMap !=null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(12.966874, 77.595399), 16));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            if (googleMap != null && getActivity() != null) {
                MapFragment.this.mGoogleMap = googleMap;

                UiSettings mapSettings = mGoogleMap.getUiSettings();
                mapSettings.setZoomControlsEnabled(false);
                mapSettings.setCompassEnabled(false);
                mapSettings.setRotateGesturesEnabled(false);
                mapSettings.setTiltGesturesEnabled(false);
                mapSettings.setMyLocationButtonEnabled(false);
                mGoogleMap.setMyLocationEnabled(true);

            }

    }
}
