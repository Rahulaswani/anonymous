package com.sequoiahack.jarvis;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sequoiahack.jarvis.fragments.FirstFragment;
import com.sequoiahack.jarvis.fragments.JarvisMapFragment;
import com.sequoiahack.jarvis.parsers.ResponseList;
import com.sequoiahack.jarvis.utils.AppConstants;
import com.sequoiahack.jarvis.utils.WaveView;

import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        startJarvis();
        getData();
    }

    private void initCurrentViews() {
        relativeLayoutAnimation = (RelativeLayout) findViewById(R.id.voice_recognition_holder);
        jarvisBackground = (ImageView) findViewById(R.id.background_image);
        jarvisCenter = (ImageView) findViewById(R.id.inner_image);
        jarvisCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopJarvis();
                jarvisBackground.setVisibility(View.GONE);
                jarvisCenter.setVisibility(View.GONE);
                moveJarvisUp(waveView);
               // replaceFragment(new FirstFragment(), "FIRST_FRAGMENT");
                replaceFragment(new JarvisMapFragment(), "MAP_FRAGMENT");
                collapse(relativeLayoutAnimation, waveView);
                //   overridePendingTransition(R.anim.slideup, R.anim.noanimation);
            }
        });
        startJarvis();
        waveView = (WaveView) findViewById(R.id.wave_view);
        waveView.setVoiceCircleColor(0x60dedede, 0x70dedede);
        waveView.setVolumeCallback(new WaveView.VolumeCallBack() {
            @Override
            public int getAmplitude() {
                return random.nextInt(100);
            }
        });
    }

    private void stopJarvis() {
        if (waveView != null) {
            handler.removeCallbacks(showWaveViewRunnable);
            waveView.stopAnimation();
            waveView.clearAnimation(); // setVisibility(View.GONE);
        }
        if (jarvisBackground.getVisibility() == View.VISIBLE && jarvisCenter.getVisibility() == View.VISIBLE) {
            jarvisBackground.clearAnimation();
            jarvisCenter.clearAnimation();
            jarvisBackground.setVisibility(View.GONE);
            jarvisCenter.setVisibility(View.GONE);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopJarvis();
    }

    private void startJarvis() {
        jarvisBackground.startAnimation(
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise_rotation));
        jarvisCenter.startAnimation(
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anticlockwise_rotation));
      //  handler.removeCallbacks(showWaveViewRunnable);
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
       //   replaceFragment(new MapFragment(), AppConstants.MAP_FRAGMENT);
       //   replaceFragment(new FirstFragment(), AppConstants.FIRST_FRAGMENT);
     //   collapse(waveView);
     //   overridePendingTransition(R.anim.slideup, R.anim.noanimation);
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

    private void collapse(final View fullView, final View jarvisView) {
        final int initialHeight = fullView.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime < 0.89) {
                    Log.d("MainActivity", interpolatedTime + " : " + jarvisView.getHeight());
                    fullView.getLayoutParams().height = jarvisView.getHeight(); //initialHeight - (int)(initialHeight * interpolatedTime);
                    fullView.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / fullView.getContext().getResources().getDisplayMetrics().density));
        //   a.setDuration(10000);
        fullView.startAnimation(a);
    }

    private void moveJarvisUp(View view) {
        // Prepare the View for the animation
        ViewPropertyAnimator a = view.animate().setDuration(700);
        jarvisCenter.setOnClickListener(null);
    }
}
