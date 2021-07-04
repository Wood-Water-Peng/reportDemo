package com.example.lib.core;

import androidx.annotation.NonNull;

import com.example.lib.interceptor.IPropertyComposer;
import com.example.lib.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;

//通用信息
public class CommonInfoPropertyComposer implements IPropertyComposer {
    @Override
    public JSONObject compose(@NonNull @NotNull Chain chain) {
        JSONObject response = chain.navigate();
        SecureRandom random = new SecureRandom();
        long eventTime = System.currentTimeMillis();
        try {
            response.put("_track_id", random.nextInt());
            response.put("time", eventTime);
            String networkType = NetworkUtils.networkType(chain.getCenterApi().mContext);
            response.put("$wifi", "WIFI".equals(networkType));
            response.put("$network_type", networkType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chain.proceed(response);
    }

}
