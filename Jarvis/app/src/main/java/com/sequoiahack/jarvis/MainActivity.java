package com.sequoiahack.jarvis;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sequoiahack.jarvis.fragments.FirstFragment;
import com.sequoiahack.jarvis.parsers.ResponseList;
import com.sequoiahack.jarvis.utils.AppConstants;
import com.sequoiahack.jarvis.utils.WaveView;

import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.sequoiahack.jarvis.utils.AppConstants.FIRST_FRAGMENT;

public class MainActivity extends BaseActivity {
    ProgressDialog mProgress;
    RelativeLayout relativeLayoutAnimation;
    ImageView jarvisBackground;
    ImageView jarvisCenter;
    ImageView jarvisEye;
    WaveView waveView;
    Handler handler = new Handler();
    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocalContentView(R.layout.activity_main);
        initCurrentViews();
      //  mProgress = showProgressBar("Getting Products", "Loading products...");
        getData();
    }

    private void initCurrentViews() {
        relativeLayoutAnimation = (RelativeLayout) findViewById(R.id.voice_recognition_holder);
        jarvisBackground = (ImageView) relativeLayoutAnimation.findViewById(R.id.background_image);
        jarvisCenter = (ImageView) relativeLayoutAnimation.findViewById(R.id.inner_image);
       // jarvisEye = (ImageView) findViewById(R.id.eye_image);
        waveView = (WaveView) findViewById(R.id.wave_view);
        waveView.setVoiceCircleColor(0x60dedede, 0x70dedede);
        //	waveView.setVoiceSurroundColor(0x80dedede);
        waveView.setVolumeCallback(new WaveView.VolumeCallBack() {
            @Override
            public int getAmplitude() {
                return random.nextInt(100);
            }
        });
    }

    private void stopJarvis(){
        if (waveView != null) {
            handler.removeCallbacks(showWaveViewRunnable);
            waveView.setVisibility(View.GONE);
        }
    }

    private Runnable showWaveViewRunnable = new Runnable() {
        @Override
        public void run() {
            waveView.setVisibility(View.VISIBLE);
            waveView.startAnimation();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        startJarvis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopJarvis();
    }

    private void startJarvis(){
        jarvisBackground.startAnimation(
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise_rotation));
        jarvisCenter.startAnimation(
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anticlockwise_rotation));
        handler.removeCallbacks(showWaveViewRunnable);
        handler.postDelayed(showWaveViewRunnable, 200);
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
