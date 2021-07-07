package com.example.lib.tasks;

import com.example.lib.R;
import com.example.lib.ReportLog;
import com.example.lib.UploadStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//上传事件到服务器
public class UploadEventTask implements Callable<UploadResult> {
    UploadStrategy strategy;
    String serverUrl;
    String jsonObject;

    public UploadEventTask(UploadStrategy strategy, String serverUrl, String rawMsg) {
        this.strategy = strategy;
        this.serverUrl = serverUrl;
        this.jsonObject = rawMsg;
    }

    @Override
    public UploadResult call() throws Exception {
        Thread.sleep(500);
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        OkHttpClient client = builder.build();
//        Request request = new Request.Builder().build();
//        Response response = client.newCall(request).execute();
        int i = new Random().nextInt(100);
        int size;
        String startId;
        String endId;
        if (jsonObject.startsWith("[")) {
            JSONArray array = new JSONArray(jsonObject);
            size = array.length();
            startId = array.getJSONObject(0).getString("_db_id");
            endId = array.getJSONObject(size - 1).getString("_db_id");
        } else {
            JSONObject object = new JSONObject(jsonObject);
            size = 1;
            startId = object.getString("_db_id");
            endId = object.getString("_db_id");
        }
        if (i % 2 == 0) {
            ReportLog.logJson("size: " + size + " startId: " + startId + " endId: " + endId, jsonObject);
            return new UploadResult(0, "发送成功");
        } else {
            return new UploadResult(-1, "发送失败");
        }
    }
}
