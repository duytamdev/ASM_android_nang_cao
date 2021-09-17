package com.tamstudio.asm_duytam.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tamstudio.asm_duytam.R;

public class ReadNewsActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        webView = findViewById(R.id.web_view);
        String linkExample = "https://caodang.fpt.edu.vn/tin-tuc-poly/together-we-are-strong-sinh-vien-poly-gui-loi-cam-on-den-cac-chien-si-tuyen-dau-chong-dich.html";
        webView.loadUrl(linkExample);
        webView.setWebViewClient(new WebViewClient());
    }
}