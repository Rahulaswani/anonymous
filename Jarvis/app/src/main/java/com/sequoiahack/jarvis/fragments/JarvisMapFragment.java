package com.sequoiahack.jarvis.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.sequoiahack.jarvis.R;

/**
 * Created by aswani on 30/08/15.
 */
public class JarvisMapFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;

    public JarvisMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMapIfNeeded();
    }

    /**
     * ** Sets up the map if it is possible to do so ****
     */
    public void setUpMapIfNeeded() {
        if (mMapFragment == null) {
            mMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.location_map);
            if (mMapFragment != null) {
                mMapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null && getActivity() != null) {
            this.mGoogleMap = googleMap;

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
