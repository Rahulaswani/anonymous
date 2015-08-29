package com.sequoiahack.jarvis;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sequoiahack.jarvis.core.JarvisRestClient;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

import static com.sequoiahack.jarvis.utils.Api.END_POINT;
import static com.sequoiahack.jarvis.utils.AppConstants.DEBUG;
import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;

public abstract class BaseActivity extends AppCompatActivity {
    protected FrameLayout mContentHolder;
    protected JarvisRestClient mRestClient;

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
    }

    protected void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBus();
    }

    @Override
    public void onStop() {
        unregisterBus();
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

    protected ProgressDialog showProgressBar(String title, String msg) {
        return ProgressDialog.show(this, title, msg, true, true);
    }

    protected void hideProgressDialog(ProgressDialog dialog) {
        dialog.hide();
    }
}
