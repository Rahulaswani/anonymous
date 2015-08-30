package com.sequoiahack.jarvis.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class JarvisReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent service = new Intent(context, VoiceRecognitionService.class);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            context.stopService(service);
        } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            context.startService(service);
        }
    }
}
