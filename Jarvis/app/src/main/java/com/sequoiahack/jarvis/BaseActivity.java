package com.sequoiahack.jarvis;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sequoiahack.jarvis.core.JarvisRestClient;
import com.sequoiahack.jarvis.utils.SimpleSharedPreferences;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

import static com.sequoiahack.jarvis.utils.Api.END_POINT;
import static com.sequoiahack.jarvis.utils.AppConstants.DEBUG;
import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;


public abstract class BaseActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {
    protected FrameLayout mContentHolder;
    protected JarvisRestClient mRestClient;
    protected SimpleSharedPreferences mPref;
    private static final String TAG = "BaseActivity";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    protected double latitude = 0;
    protected double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerBus();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContentHolder = (FrameLayout) findViewById(R.id.main_content);
        mContentHolder.removeAllViews();
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .setLogLevel(DEBUG ? FULL : NONE)
                .build();
        mRestClient = restAdapter.create(JarvisRestClient.class);
        mPref = new SimpleSharedPreferences(getApplicationContext());
        // First we need to check availability of play services
        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            this.latitude = mLastLocation.getLatitude();
            this.longitude = mLastLocation.getLongitude();

        } else {
            this.latitude = 0;
            this.longitude = 0;
        }
    }



    protected void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        registerBus();
    }

    @Override
    public void onStop() {
        unregisterBus();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    protected EventBus getBus() {
        return EventBus.getDefault();
    }

    protected void registerBus() {
        registerBus(0);
    }

    protected void registerStickyBus() {
        registerStickyBus(0);
    }

    protected void registerBus(int priority) {
        unregisterBus();
        getBus().register(this, priority);
    }

    protected void registerStickyBus(int priority) {
        unregisterBus();
        getBus().registerSticky(this, priority);
    }

    protected void unregisterBus() {
        getBus().unregister(this);
    }

    protected void setLocalContentView(@LayoutRes int layoutResID) {
        View childView = this.getLayoutInflater().inflate(layoutResID, mContentHolder, false);
        mContentHolder.addView(childView);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected ProgressDialog showProgressBar(String title, String msg) {
        return ProgressDialog.show(this, title, msg, true, true);
    }

    protected void hideProgressDialog(ProgressDialog dialog) {
        if (dialog != null) {
            dialog.hide();
        }
    }
}
