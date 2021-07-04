package com.example.lib.tasks;

import com.example.lib.R;
import com.example.lib.UploadStrategy;

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
    JSONObject jsonObject;

    public UploadEventTask(UploadStrategy strategy, String serverUrl, String rawMsg) {
        this.strategy = strategy;
        this.serverUrl = serverUrl;
        this.jsonObject = jsonObject;
    }

    @Override
    public UploadResult call() throws Exception {
        Thread.sleep(500);
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        OkHttpClient client = builder.build();
//        Request request = new Request.Builder().build();
//        Response response = client.newCall(request).execute();
        int i = new Random().nextInt(100);
        if (i % 2 == 0) {
            return new UploadResult(0, "发送成功");
        } else {
            return new UploadResult(-1, "发送失败");
        }
    }
}
