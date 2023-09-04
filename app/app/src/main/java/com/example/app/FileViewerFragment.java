package com.example.app;

import android.content.Intent;
import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.Map;

public class FileViewerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = view.findViewById(R.id.fileViewerWebview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // カテゴリと動画名を表示するTextViewの取得
        TextView categoryTextView = view.findViewById(R.id.fileViewer);
        TextView videoNameTextView = view.findViewById(R.id.parameter);

        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        String url = null;
        if (intent.hasExtra("URL")) {
            url = intent.getStringExtra("URL");
        } else if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String parameter = data.getLastPathSegment();
            url = UrlMappingHelper.getUrl(parameter);
        }

        // WebViewにURLをロードして、カテゴリと動画名をセット
        if (url != null) {
            webView.loadUrl(url);
            switch (url) {
                case "https://drive.google.com/file/d/1A0wlhjWGG1DLd8-rLI0FIYM6OYnhQjyr/preview":
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
        }

        // 戻るボタンのクリックリスナー設定
        TextView backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity != null) {
                Intent backToListIntent = new Intent(activity, FileListActivity.class);
                startActivity(backToListIntent);
            }
        });
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustViewsForOrientation(newConfig.orientation);
    }

    private void adjustViewsForOrientation(int orientation) {
        View header = getView().findViewById(R.id.header);
        View parameter = getView().findViewById(R.id.parameter);
        View bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        View backBtn = getView().findViewById(R.id.back);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            header.setVisibility(View.VISIBLE);
            parameter.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            backBtn.setVisibility(View.VISIBLE);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            header.setVisibility(View.VISIBLE);
            parameter.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            backBtn.setVisibility(View.VISIBLE);
        }
    }
}
