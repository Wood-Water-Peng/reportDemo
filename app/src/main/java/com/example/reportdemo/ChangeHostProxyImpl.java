package com.example.reportdemo;

import android.webkit.WebResourceResponse;

import com.example.lib.test.ChangeHostProxy;

public class ChangeHostProxyImpl implements ChangeHostProxy {
    @Override
    public String interceptRequest(String url) {
        return "interceptRequest";
    }
}
