package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import java.util.Map;

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

        // WebViewの設定と初期化
        WebView webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // カテゴリと動画名を表示するTextViewの取得
        TextView categoryTextView = findViewById(R.id.fileViewer);
        TextView videoNameTextView = findViewById(R.id.parameter);

        // Intentからデータの取得
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        // ボトムナビゲーションの取得
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        // URLの取得と設定
        String url = null;
        if (intent.hasExtra("URL")) {
            url = intent.getStringExtra("URL");
        } else if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String parameter = data.getLastPathSegment();

            // URLとそのIDのマッピング
            Map<String, String> urlMap = new HashMap<>();
            urlMap.put("123", "http://example.com");
            urlMap.put("456", "file:///android_asset/YouTube.html");
            urlMap.put("789", "file:///android_asset/example_com.html");

            url = urlMap.get(parameter);
        }

        // WebViewにURLをロードして、カテゴリと動画名をセット
        if (url != null) {
            webView.loadUrl(url);
            switch (url) {
                case "http://example.com":
                    categoryTextView.setText(getString(R.string.category_name1));
                    videoNameTextView.setText(getString(R.string.video_name1));
                    break;
                case "file:///android_asset/YouTube.html":
                    categoryTextView.setText(getString(R.string.category_name2));
                    videoNameTextView.setText(getString(R.string.video_name2));
                    break;
                case "file:///android_asset/example_com.html":
                    categoryTextView.setText(getString(R.string.category_name3));
                    videoNameTextView.setText(getString(R.string.video_name3));
                    break;
            }
            bottomNav.setSelectedItemId(R.id.viewer);
        }

        // アプリ終了用のハンドラとRunnableの設定
        handler = new Handler();
        shutdownRunnable = new Runnable() {
            @Override
            public void run() {
                finishAndRemoveTask();
            }
        };

        // ヘッダーの色を設定
        LinearLayout header = findViewById(R.id.header);
        header.setBackgroundColor(Color.parseColor("#FF5733"));

        // ボトムナビゲーションのアイテム選択リスナー設定
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.back:
                    Intent backIntent = new Intent(MainActivity.this, FileListActivity.class);
                    startActivity(backIntent);
                    return true;
                case R.id.viewer:
                    findViewById(R.id.webview).setVisibility(View.VISIBLE);
                    findViewById(R.id.fragment_container).setVisibility(View.GONE);
                    return true;
                case R.id.settings:
                    findViewById(R.id.webview).setVisibility(View.GONE);
                    findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                    // SettingsFragmentを表示
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SettingsFragment())
                            .commit();
                    return true;
                default:
                    return false;
            }
        });

        // 戻るボタンのクリックリスナー設定
        TextView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent backToListIntent = new Intent(MainActivity.this, FileListActivity.class);
            startActivity(backToListIntent);
        });

        // 画面の向きに応じてビューの調整
        adjustViewsForOrientation(getResources().getConfiguration().orientation);
    }

    // 画面の向きに応じてビューを表示・非表示にするメソッド
    private void adjustViewsForOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.header).setVisibility(View.VISIBLE);
            findViewById(R.id.parameter).setVisibility(View.GONE);
            findViewById(R.id.fragment_container).setVisibility(View.GONE);
            findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
            findViewById(R.id.back).setVisibility(View.VISIBLE);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            findViewById(R.id.header).setVisibility(View.VISIBLE);
            findViewById(R.id.parameter).setVisibility(View.VISIBLE);
            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
            findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
            findViewById(R.id.back).setVisibility(View.VISIBLE);
        }
    }

    // 画面の向きが変わった時の動作
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustViewsForOrientation(newConfig.orientation);
    }

    // アプリがバックグラウンドに行った時の動作
    @Override
    protected void onPause() {
        super.onPause();
        handler.postDelayed(shutdownRunnable, 10 * 60 * 1000);
    }

    // アプリがフォアグラウンドに来た時の動作
    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(shutdownRunnable);
    }
}