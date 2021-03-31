package com.getcapacitor;

import android.net.Uri;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.List;

public class BridgeWebViewClient extends WebViewClient {

    private Bridge bridge;

    public BridgeWebViewClient(Bridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return bridge.getLocalServer().shouldInterceptRequest(request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();
        return bridge.launchIntent(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return bridge.launchIntent(Uri.parse(url));
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        List<WebViewListener> webViewListeners = bridge.getWebViewListeners();

        if (webViewListeners != null && view.getProgress() == 100) {
            for (WebViewListener listener : bridge.getWebViewListeners()) {
                listener.onPageLoaded(view);
            }
        }
    }
}
