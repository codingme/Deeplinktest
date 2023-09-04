package com.example.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.net.Uri;
import java.util.Map;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable shutdownRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // アクションバーを非表示にする
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Handlerの初期化
        handler = new Handler();

        // Runnableの初期化
        shutdownRunnable = new Runnable() {
            @Override
            public void run() {
                finishAndRemoveTask();
            }
        };

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.viewer:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new FileViewerFragment())
                            .commit();
                    return true;
                case R.id.settings:
                    // SettingsFragmentを表示するコード
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SettingsFragment())
                            .commit();
                    return true;
                default:
                    return false;
            }
        });

        // 画面の向きに応じてビューの調整
        adjustViewsForOrientation(getResources().getConfiguration().orientation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FileViewerFragment())
                    .commit();
        }

    }

    private void adjustViewsForOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape-specific adjustments
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Portrait-specific adjustments
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustViewsForOrientation(newConfig.orientation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.postDelayed(shutdownRunnable, 10 * 60 * 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(shutdownRunnable);
    }
}