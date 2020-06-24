package com.example.safekeeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReportFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_report,container,false);
        WebView webView = (WebView)v.findViewById(R.id.wv_report);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new TestBrowser());
        webView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLScWpA2UocU2bYxOQ58ZllhtZc1pKL6g7tA9nfDnucrJFJAcYA/viewform");
        return  v;

    }
    private class TestBrowser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
