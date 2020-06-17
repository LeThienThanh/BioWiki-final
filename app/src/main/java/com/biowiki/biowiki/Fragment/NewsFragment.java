package com.biowiki.biowiki.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.biowiki.biowiki.R;

public class NewsFragment extends Fragment{
    private WebView webView;
    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_news, null);
        View v = inflater.inflate(R.layout.fragment_news, container, false);

        webView = (WebView) v.findViewById(R.id.webview);
        webView.setWebViewClient( new WebViewClient());
        webView.loadUrl("https://lecungtienzz.wixsite.com/biowikinews?utm_campaign=872e6cae-44cb-41a0-9844-fb48b13490db&utm_source=so");

        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return v;
    }
}
