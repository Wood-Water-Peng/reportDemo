package com.example.lib.interceptor;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

//最后一个组装器
public class LastPropertyComposer implements IPropertyComposer {
    @Override
    public JSONObject compose(@NonNull @NotNull Chain chain) {
        return chain.navigate();
    }

}
