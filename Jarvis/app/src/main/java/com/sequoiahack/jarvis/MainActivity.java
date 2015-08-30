package com.sequoiahack.jarvis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sequoiahack.jarvis.fragments.FirstFragment;
import com.sequoiahack.jarvis.parsers.ResponseList;
import com.sequoiahack.jarvis.utils.Api;
import com.sequoiahack.jarvis.utils.AppConstants;
import com.sequoiahack.jarvis.utils.WaveView;
import com.sequoiahack.jarvis.widget.JarvisTextView;

import java.util.ArrayList;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity {
    private ProgressDialog mProgress;
    private RelativeLayout relativeLayoutAnimation;
    private ImageView jarvisBackground;
    private ImageView jarvisCenter;
    private WaveView waveView;
    private Handler handler = new Handler();
    private Random random = new Random();
    private JarvisTextView jarvisTextView;
    private SpeechRecognizer mRecognizer;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocalContentView(R.layout.activity_main);
        initCurrentViews();
        startJarvis();
    }

    private void initCurrentViews() {
        relativeLayoutAnimation = (RelativeLayout) findViewById(R.id.voice_recognition_holder);
        jarvisBackground = (ImageView) findViewById(R.id.background_image);
        jarvisCenter = (ImageView) findViewById(R.id.inner_image);
        jarvisTextView = (JarvisTextView) findViewById(R.id.jarvis_speech);
        jarvisBackground.startAnimation(
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise_rotation));
        jarvisCenter.startAnimation(
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anticlockwise_rotation));
        jarvisCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopJarvis();
                jarvisBackground.setVisibility(View.GONE);
                jarvisCenter.setVisibility(View.GONE);
                moveJarvisUp(waveView);
                replaceFragment(new FirstFragment(), "FIRST_FRAGMENT");
                collapse(relativeLayoutAnimation, waveView);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        startJarvis();
                        jarvisSpeaks("Yes Master. How may i Assist you !");
                    }

                }, 1000);
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
        startVoiceRecognition();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopJarvis();
    }

    private void startJarvis() {
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
            mPref.putString(Api.Params.META, responseList.getMeta());
            mPref.putString(Api.Params.SID, responseList.getSid());
        } else {
            Log.d("HomeActivity", "ERROR");
        }
    }

    private void getData(String query, double lat, double lon, String sId, String meta) {
        mRestClient.search(query, String.valueOf(lat), String.valueOf(lon), sId, meta, "myCallBack", new Callback<ResponseList>() {
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
                if (jarvisView.getHeight() < 450) {
                    // DO nothing.
                } else {
                    fullView.getLayoutParams().height = jarvisView.getHeight() + jarvisTextView.getHeight(); //initialHeight - (int)(initialHeight * interpolatedTime);
                    fullView.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / fullView.getContext().getResources().getDisplayMetrics().density));
        fullView.startAnimation(a);
    }

    private void moveJarvisUp(View view) {
        ViewPropertyAnimator a = view.animate().setDuration(700);
        jarvisCenter.setOnClickListener(null);

    }

    private void jarvisSpeaks(String messageToPrint) {
        jarvisTextView.setVisibility(View.VISIBLE);
        jarvisTextView.setCharacterDelay(70);
        jarvisTextView.animateText(messageToPrint);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                jarvisTextView.setVisibility(View.GONE);
            }

        }, 15000);
    }


    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask Jarvis...");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-IN");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this.getApplicationContext());
        final RecognitionListener mListener = new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults == null) {
                    showAlert("Unable to identify your speech, please repeat again.");
                } else {
                    Log.d(TAG, "Printing matches: ");
                    for (String match : voiceResults) {
                        Log.d(TAG, match);
                    }
                    getDataFromServer(voiceResults.get(0));
                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(MainActivity.this, "Ask Jarvis.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error) {
                Log.d(TAG, "Error listening for speech: " + error);
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "Speech starting");
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub
            }
        };
        mRecognizer.setRecognitionListener(mListener);
        mRecognizer.startListening(intent);
    }

    private void getDataFromServer(String query) {

        final double lati = latitude != 0 ? latitude : 12.96702;
        final double longi = longitude != 0 ? longitude : 77.595437;
        final String sid = mPref.getString(Api.Params.SID, null);
        final String meta = mPref.getString(Api.Params.META, null);

        getData(query, lati, longi, sid, meta);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mRecognizer.cancel();
        mRecognizer.stopListening();
        super.onDestroy();
    }


    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

}
