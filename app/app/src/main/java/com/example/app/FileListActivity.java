package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class FileListActivity extends AppCompatActivity {

    private LinearLayout buttonContainer; // 動画ボタンを格納するコンテナ
    private TextView headerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // アクションバーを非表示にする
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // UIを設定
        setContentView(R.layout.activity_file_list);

        buttonContainer = findViewById(R.id.buttonContainer);
        headerTextView = findViewById(R.id.parameterTextView);

        // 各ボタンにリスナーを設定
        setButtonListeners();

        // ボトムナビゲーションのリスナーを設定
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        setBottomNavigationListener(bottomNav);

        // ボタンのテキストを更新
        updateButtonText();

        // Intentからデータを取得してヘッダーテキストビューを更新
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data != null) {
                String parameter = data.getLastPathSegment();
                if (parameter != null) {
                    headerTextView.setText(parameter);
                }
            }
        }
    }

    // 各ボタンにリスナーを設定するメソッド
    private void setButtonListeners() {
        Button btnExampleCom = findViewById(R.id.btnExampleCom);
        Button btnYouTube = findViewById(R.id.btnYouTube);
        Button btnExampleDotCom = findViewById(R.id.btnExampleDotCom);

        btnExampleCom.setOnClickListener(v -> openFileViewer("123"));
        btnYouTube.setOnClickListener(v -> openFileViewer("456"));
        btnExampleDotCom.setOnClickListener(v -> openFileViewer("789"));
    }

    // ボトムナビゲーションのリスナーを設定するメソッド
    private void setBottomNavigationListener(BottomNavigationView bottomNav) {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    // 設定タブが選択された場合、ボタンコンテナを非表示にし、設定フラグメントを表示
                    buttonContainer.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SettingsFragment())
                            .commit();
                    return true;
                case R.id.back_to_list:
                    // 一覧タブが選択された場合、ボタンコンテナを表示し、フラグメントを削除
                    buttonContainer.setVisibility(View.VISIBLE);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
                    transaction.commit();
                    return true;
                default:
                    return false;
            }
        });
    }

    // 各ボタンのテキストを更新するメソッド
    private void updateButtonText() {
        Button btnExampleCom = findViewById(R.id.btnExampleCom);
        Button btnYouTube = findViewById(R.id.btnYouTube);
        Button btnExampleDotCom = findViewById(R.id.btnExampleDotCom);

        btnExampleCom.setText(getString(R.string.video_format, getString(R.string.category_name1), getString(R.string.video_name1)));
        btnYouTube.setText(getString(R.string.video_format, getString(R.string.category_name2), getString(R.string.video_name2)));
        btnExampleDotCom.setText(getString(R.string.video_format, getString(R.string.category_name3), getString(R.string.video_name3)));
    }

    // 指定したパラメータに基づいてFileViewerを開くメソッド
    private void openFileViewer(String parameter) {
        Intent intent = new Intent(this, MainActivity.class);
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("123", "https://drive.google.com/file/d/1A0wlhjWGG1DLd8-rLI0FIYM6OYnhQjyr/preview");
        urlMap.put("456", "file:///android_asset/YouTube.html");
        urlMap.put("789", "file:///android_asset/example_com.html");

        String url = urlMap.get(parameter);
        if (url != null) {
            intent.putExtra("URL", url);
            startActivity(intent);
        }
    }
}