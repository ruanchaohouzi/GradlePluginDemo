package com.ruanchao.gradleplugindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private WebView ms_webview;
    private Button btn_clear_cache;
    private Button btn_refresh;

    private static final String APP_CACHE_DIRNAME = "/webcache"; // web 缓存目录
    private static final String SITE_URL = "https://www.twle.cn/static/i/android/demo_a.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showGradleText(View view) {
    }
}