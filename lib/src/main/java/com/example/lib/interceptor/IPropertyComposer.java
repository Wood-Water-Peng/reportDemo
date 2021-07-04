package com.example.lib.interceptor;

import androidx.annotation.NonNull;

import com.example.lib.core.ReportCenterAPI;

import org.json.JSONObject;

//用于组装某一个属性
public interface IPropertyComposer {
    JSONObject compose(@NonNull Chain chain);

    interface Chain {
        JSONObject proceed(@NonNull JSONObject origin);

        void interrupt(@NonNull Throwable exception);

        JSONObject navigate();

        ReportCenterAPI getCenterApi();
    }
}
