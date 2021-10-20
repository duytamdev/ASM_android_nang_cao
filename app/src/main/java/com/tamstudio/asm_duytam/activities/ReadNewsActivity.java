package com.tamstudio.asm_duytam.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.tamstudio.asm_duytam.R;

public class ReadNewsActivity extends AppCompatActivity {
    WebView webView;
    Button btnShare;
    String link;

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);
        link = getIntent().getStringExtra("linkNews");
        webView = findViewById(R.id.web_view);
        btnShare = findViewById(R.id.btn_share_fb);
        initWebView();
        initShare();
        eventShare();

    }

    private void initShare() {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("--------->", "onSuccess: ");
            }

            @Override
            public void onCancel() {
                Log.d("-------->", "onCancel: ");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d("------->", "onError: ");
            }
        });
    }

    private void eventShare() {
        btnShare.setOnClickListener(view -> {
            if(shareDialog.canShow(ShareLinkContent.class)){
                ShareLinkContent content  = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(link))
                        .setQuote("Tuyệt quá!!!")
                        .build();
                shareDialog.show(content);
            }
        });
    }

    private void initWebView() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient(){
            ProgressDialog dialog = new ProgressDialog(ReadNewsActivity.this);

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.setTitle("Đang tải ....");
                dialog.setMessage("Vui lòng chờ");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}