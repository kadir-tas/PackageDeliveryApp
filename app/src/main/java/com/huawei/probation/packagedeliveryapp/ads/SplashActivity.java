package com.huawei.probation.packagedeliveryapp.ads;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.splash.SplashAdDisplayListener;
import com.huawei.hms.ads.splash.SplashView;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.probation.packagedeliveryapp.MainActivity;
import com.huawei.probation.packagedeliveryapp.R;

public class SplashActivity extends AppCompatActivity {

    private static String splashID = "testd7c5cewoj6"; //Video Splash
    //private static String splashID = "testq6zq98hecj"; //Image

    private static String TAG = "SPLASH";
    // Ad display timeout interval, in milliseconds.
    private static final int AD_TIMEOUT = 15000;

    // Ad display timeout message flag.
    private static final int MSG_AD_TIMEOUT = 10000;
    /**
     * Pause flag.
     * On the splash ad screen:
     * Set this parameter to true when exiting the app to ensure that the app home screen is not displayed.
     * Set this parameter to false when returning to the splash ad screen from another screen to ensure that the app home screen can be displayed properly.
     */
    private boolean hasPaused = false;
    private HiAnalyticsInstance analyticsInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        HwAds.init(this);

        analyticsInstance = HiAnalytics.getInstance(this);
        analyticsInstance.setAnalyticsEnabled(true);

        loadAd();
    }

    // Callback handler used when the ad display timeout message is received.
    private Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (SplashActivity.this.hasWindowFocus()) {
                jump();
            }
            return false;
        }
    });

    private SplashView splashView;

    SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            // Call this method when an ad fails to be loaded.
            Log.i(TAG, "SplashAdLoadListener onAdFailedToLoad, errorCode: " + errorCode);
            Toast.makeText(SplashActivity.this, getString(R.string.status_load_ad_fail) + errorCode, Toast.LENGTH_SHORT).show();
            jump();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Log.i(TAG, "SplashAdLoadListener onAdLoaded.");
            Toast.makeText(SplashActivity.this, getString(R.string.status_load_ad_success), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdDismissed() {
            super.onAdDismissed();
            // Call this method when the ad display is complete.
            Log.i(TAG, "SplashAdLoadListener onAdDismissed.");
            Toast.makeText(SplashActivity.this, getString(R.string.status_ad_dismissed), Toast.LENGTH_SHORT).show();
            jump();
        }
    };

    SplashAdDisplayListener splashAdDisplayListener = new SplashAdDisplayListener(){
        @Override
        public void onAdShowed() {
            super.onAdShowed();
            // Call this method when an ad is displayed.
            Log.i(TAG, "SplashAdDisplayListener onAdShowed.");
        }

        @Override
        public void onAdClick() {
            super.onAdClick();
            // Call this method when an ad is clicked.
            Log.i(TAG, "SplashAdDisplayListener onAdClick.");
        }
    };

    private void loadAd() {
        Log.i(TAG, "Start to load ad");

        AdParam adParam = new AdParam.Builder().build();
        splashView = findViewById(R.id.splash_ad_view);//set SplashView
        splashView.setAdDisplayListener(splashAdDisplayListener);

        // Set a logo image.
        splashView.setLogoResId(R.mipmap.ic_launcher);
        // Set logo description.
        splashView.setMediaNameResId(R.string.app_name);
        // Set the audio focus type for a video splash ad.
        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);

        splashView.load(splashID, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, adParam, splashAdLoadListener);
        Log.i(TAG, "End to load ad");

        // Remove the timeout message from the message queue.
        // timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        // Send a delay message to ensure that the app home screen can be displayed when the ad display times out.
        timeoutHandler.sendEmptyMessageDelayed(MSG_AD_TIMEOUT, AD_TIMEOUT);
    }

    private void jump() {
        Log.i(TAG, "jump hasPaused: " + hasPaused);
        if (!hasPaused) {
            hasPaused = true;
            Log.i(TAG, "jump into application");

            startActivity(new Intent(SplashActivity.this, MainActivity.class));

            Handler mainHandler = new Handler();
            mainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }
    /**
     * Set this parameter to true when exiting the app to ensure that the app home screen is not displayed.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "SplashActivity onStop.");
        // Remove the timeout message from the message queue.
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        hasPaused = true;
        super.onStop();
    }

    /**
     * Call this method when returning to the splash ad screen from another screen to access the app home screen.
     */
    @Override
    protected void onRestart() {
        Log.i(TAG, "SplashActivity onRestart.");
        super.onRestart();
        hasPaused = false;
        jump();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "SplashActivity onDestroy.");
        super.onDestroy();
        if (splashView != null) {
            splashView.destroyView();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "SplashActivity onPause.");
        super.onPause();
        if (splashView != null) {
            splashView.pauseView();
        }
    }


    @Override
    protected void onResume() {
        Log.i(TAG, "SplashActivity onResume.");
        super.onResume();
        if (splashView != null) {
            splashView.resumeView();
        }
    }

}