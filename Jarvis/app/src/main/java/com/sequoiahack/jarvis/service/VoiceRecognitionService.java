package com.sequoiahack.jarvis.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sequoiahack.jarvis.MainActivity;

import java.util.ArrayList;

public class VoiceRecognitionService extends Service {

    private static final String TAG = "VoiceRecognitionService";

    private final IBinder mBinder = new LocalBinder();
    private SpeechRecognizer mRecognizer;

    public class LocalBinder extends Binder {
        VoiceRecognitionService getService() {
            return VoiceRecognitionService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        startVoiceRecognition();
        return START_STICKY;
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
                    Log.e(TAG, "No voice results");
                } else {
                    Log.d(TAG, "Printing matches: ");
                    boolean matched = false;
                    for (String match : voiceResults) {
                        if (match.equalsIgnoreCase("Hi Jarvis") ||
                                match.equalsIgnoreCase("Hey Jarvis") ||
                                match.equalsIgnoreCase("Hello Jarvis")) {
                            matched = true;
                            break;
                        }
                        Log.d(TAG, match);
                    }

                    if (matched) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("FROM_SERVICE", true);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "Ready for speech");
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

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mRecognizer.cancel();
        mRecognizer.stopListening();
        super.onDestroy();
    }
}
