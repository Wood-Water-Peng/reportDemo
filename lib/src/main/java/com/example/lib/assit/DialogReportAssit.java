package com.example.lib.assit;

import android.app.Activity;
import android.app.Dialog;

import com.example.lib.ReportLog;
import com.example.lib.core.ReportCenterAPI;
import com.example.lib.event.Event;

import org.json.JSONException;
import org.json.JSONObject;

public class DialogReportAssit {
    ReportCenterAPI reportCenterAPI;

    public DialogReportAssit(ReportCenterAPI reportCenterAPI) {
        this.reportCenterAPI = reportCenterAPI;
    }

    public void handleDialogShowed(Dialog dialog) {
        ReportLog.logD("handleActivity appStart");
        JSONObject object = new JSONObject();

        reportCenterAPI.trackEvent(Event.APP_START, object);
    }

    public void handleAlertDialogClicked(Dialog dialog,int which) {
        ReportLog.logD("handleActivity appStart");
        JSONObject object = new JSONObject();

        reportCenterAPI.trackEvent(Event.APP_START, object);
    }
}
