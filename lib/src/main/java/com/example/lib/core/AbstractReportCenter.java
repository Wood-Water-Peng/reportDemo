package com.example.lib.core;

import android.view.View;

import org.json.JSONObject;

public class AbstractReportCenter implements IAppTracker{
    @Override
    public void trackAppInstall() {

    }

    @Override
    public void trackAppInstall(JSONObject jsonObject) {

    }

    @Override
    public void trackAppFirstOpen() {

    }

    @Override
    public void trackAppFirstOpen(JSONObject jsonObject) {

    }

    @Override
    public void trackViewClicked(View view, JSONObject jsonObject) {

    }

    @Override
    public void trackViewClicked(View view) {

    }

    @Override
    public void trackViewShowed(View view, JSONObject jsonObject) {

    }

    @Override
    public void trackViewShowed(View view) {

    }
}
