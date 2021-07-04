package com.example.lib.interceptor;

import androidx.annotation.NonNull;

import com.example.lib.core.ReportCenterAPI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

//组装LibInfo
public class LibInfoPropertyComposer implements IPropertyComposer {
    @Override
    public JSONObject compose(@NonNull @NotNull Chain chain) {
        JSONObject response = chain.navigate();
        ReportCenterAPI centerApi = chain.getCenterApi();
        String lib_version = centerApi.VERSION;
        String app_version = centerApi.mDeviceInfo.containsKey("$app_version") ? (String) centerApi.mDeviceInfo.get("$app_version") : "";
        JSONObject libProperties = new JSONObject();
        try {
            libProperties.put("$lib_method", "autoTrack");
            libProperties.put("$lib", "Android");
            libProperties.put("$lib_version", lib_version);
            libProperties.put("$app_version", app_version);
            response.put("lib", libProperties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chain.proceed(response);
    }

}
