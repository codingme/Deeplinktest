package com.example.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    // コンストラクタ
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // このフラグメントのレイアウトをインフレートする
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // バージョン情報を取得し、TextViewにセットする
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            String version = info.versionName;

            // ここでログ出力
            Log.d("SettingsFragment", "Version: " + version);

            TextView versionInfoTextView = view.findViewById(R.id.versionInfo);
            versionInfoTextView.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return view;
    }
}
