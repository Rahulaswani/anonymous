package com.sequoiahack.jarvis.core;

import android.app.Application;
import android.content.Context;

import com.sequoiahack.jarvis.BuildConfig;

import de.greenrobot.event.EventBus;

public class JarvisApplication extends Application {
    private static Context mContext;
    private static JarvisApplication singleton;
    private static final String TYPEFACE_TYPE = "SERIF";

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        mContext = getApplicationContext();
        EventBus.builder()
                .logNoSubscriberMessages(BuildConfig.DEBUG)
                .sendNoSubscriberEvent(BuildConfig.DEBUG)
                .throwSubscriberException(BuildConfig.DEBUG)
                .installDefaultEventBus();
    }

    public static synchronized Context getContext() {
        return mContext;
    }

    public JarvisApplication getInstance() {
        return singleton;
    }
}
