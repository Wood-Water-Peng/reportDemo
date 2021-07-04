package com.example.lib.core;

import android.view.View;

import org.json.JSONObject;

public interface IAppTracker {
    void trackAppInstall();

    void trackAppInstall(JSONObject jsonObject);

    void trackAppFirstOpen();

    void trackAppFirstOpen(JSONObject jsonObject);

    void trackViewClicked(View view, JSONObject jsonObject);

    void trackViewClicked(View view);

    void trackViewShowed(View view, JSONObject jsonObject);

    void trackViewShowed(View view);
}
