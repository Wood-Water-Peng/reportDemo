package com.example.lib.core;

import androidx.annotation.NonNull;

import com.example.lib.interceptor.IPropertyComposer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

//组装deviceInfo
public class DeviceInfoPropertyComposer implements IPropertyComposer {
    @Override
    public JSONObject compose(@NonNull @NotNull Chain chain) {
        JSONObject response = chain.navigate();
        JSONObject deviceInfo = new JSONObject(chain.getCenterApi().mDeviceInfo);
        try {
            response.put("deviceInfo", deviceInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //加入设备信息
        return chain.proceed(response);
    }

}
