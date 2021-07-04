package com.example.lib.interceptor;

import androidx.annotation.NonNull;

import com.example.lib.core.ReportCenterAPI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

//组装事件的各个属性
public class PropertyComposeChain implements IPropertyComposer.Chain {
    //组装器集合
    @NonNull
    private final List<IPropertyComposer> composers;
    private int index = 0;
    private JSONObject response;
    private final ReportCenterAPI reportCenterAPI;

    public PropertyComposeChain(@NonNull List<IPropertyComposer> composers, ReportCenterAPI reportCenterAPI) {
        this.composers = composers;
        this.reportCenterAPI = reportCenterAPI;
    }

    @Override
    public JSONObject proceed(@NonNull @NotNull JSONObject origin) {
        this.response = origin;
        if (index >= composers.size()) {
            throw new IllegalStateException("index->" + index + " is larger than interceptor size");
        }
        IPropertyComposer composer = composers.get(index++);
        return composer.compose(this);
    }

    @Override
    public void interrupt(@NonNull @NotNull Throwable exception) {

    }

    @Override
    public JSONObject navigate() {
        return this.response;
    }

    @Override
    public ReportCenterAPI getCenterApi() {
        return this.reportCenterAPI;
    }
}
