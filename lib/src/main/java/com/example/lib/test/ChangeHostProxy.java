package com.example.lib.test;

import android.webkit.WebResourceResponse;

public interface ChangeHostProxy extends TopStream {
    ChangeHostProxy DEFAULT = new ProxyBuilder().build(ChangeHostProxy.class);

    String interceptRequest(String url);
}
