package com.sequoiahack.jarvis;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.sequoiahack.jarvis.fragments.FirstFragment;
import com.sequoiahack.jarvis.parsers.ResponseList;
import com.sequoiahack.jarvis.utils.AppConstants;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.sequoiahack.jarvis.utils.AppConstants.FIRST_FRAGMENT;

public class MainActivity extends BaseActivity {
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocalContentView(R.layout.activity_main);
        mProgress = showProgressBar("Getting Products", "Loading products...");
        initCurrentViews();
        getData();
    }

    private void initCurrentViews() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        registerStickyBus();
    }

    public void onEvent(ResponseList responseList) {
        hideProgressDialog(mProgress);
        final String status = responseList.getStatus();
        if (status != null && status.equalsIgnoreCase(AppConstants.SUCCESS)) {
            Log.d("HomeActivity", "SUCCESS");
        } else {
            Log.d("HomeActivity", "ERROR");
        }
        replaceFragment(new FirstFragment(), FIRST_FRAGMENT);
    }

    private void getData() {
        mRestClient.search(null, null, null, null, "myCallBack", new Callback<ResponseList>() {
            @Override
            public void success(ResponseList responseList, Response response) {
                getBus().post(responseList);
            }

            @Override
            public void failure(RetrofitError error) {
                getBus().post(new ResponseList());
            }
        });
    }
}
