package com.example.lib.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.lib.ReportLog;
import com.example.lib.UploadStrategy;
import com.example.lib.db.DBParams;
import com.example.lib.db.EventEntity;
import com.example.lib.db.EventReportEntity;
import com.example.lib.tasks.UploadEventTask;
import com.example.lib.tasks.UploadResult;
import com.example.lib.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

//事件的处理类
//1.事件的缓存
//2.事件的上报
//
public class EventHandlerCenter {
    private static final int LIMIT = 20;
    private final WorkHandler mHandler;
    ReportCenterAPI reportCenterAPI;
    private final DBAdapter mDbAdapter;
    private static final int UPLOAD_DATA = 1;

    public EventHandlerCenter(ReportCenterAPI reportCenterAPI) {
        this.reportCenterAPI = reportCenterAPI;
        mDbAdapter = DBAdapter.getInstance();
        //开启一个子线程，接受消息
        HandlerThread thread = new HandlerThread("ReportMessageCenter");
        thread.start();
        this.mHandler = new WorkHandler(thread.getLooper());
    }

    void enqueueEventMessage(String type, JSONObject eventJson) {
        //1.将数据缓存到db中
        int insertStatus = mDbAdapter.addJSON(eventJson);
        if (insertStatus < 0) {
            String error = "Failed to enqueue the event: " + eventJson;
            if (reportCenterAPI.isDebugMode()) {
                throw new IllegalStateException(error);
            } else {
                //打印日志
                ReportLog.logD(error);
            }
        }
        final Message m = Message.obtain();
        m.what = UPLOAD_DATA;
        if (insertStatus == DBParams.DB_OUT_OF_MEMORY_ERROR) {
            //数据库快满了，立刻开启上报任务
            mHandler.sendMessage(m);
        } else {
            if (!mHandler.hasMessages(m.what)) {
                final int interval = reportCenterAPI.getFlushInterval();
                mHandler.sendMessageDelayed(m, interval);
            }
        }

        //2.发送消息，尝试取出db中的数据并上报
    }

    //当这个方法执行的时候，数据库中的消息条目是不确定的
    //该方法要确保将DB中的数据上传完成
    private void sendDataAndDeleteDBRecord() {
        //从数据库中最多取出50条数据
        //上传
        //成功--删除50条数据
        //失败--不删除
        //所以这个方法必须是子线程中的串行操作
        //无网络
        if (!NetworkUtils.isNetworkAvailable(reportCenterAPI.mContext)) {
            ReportLog.logD("网络未连接，暂停上传操作");
            return;
        }
        //每次从数据库中取50条，知道处理完毕
        boolean hasPendingEvent = true;

        while (hasPendingEvent) {
            boolean deleteEvents = true;
            String[] eventsData = mDbAdapter.queryEvents(LIMIT);
            if (eventsData == null) {
                ReportLog.logD("queryEvents 发生异常");
                return;
            }
            //上传数据，模拟网络上传
            final String lastId = eventsData[0];
            final String rawMessage = eventsData[1];
            final String gzip = eventsData[2];
            String data = rawMessage;
            if (DBParams.GZIP_DATA_EVENT.equals(gzip)) {
//                data = encodeData(rawMessage);
            }
            try {
                UploadEventTask test = new UploadEventTask(new UploadStrategy(), "test", data);
                UploadResult uploadResult = test.call();
                if (uploadResult.getCode() == 0) {
                    ReportLog.logD("reportEvent 成功 id->" + uploadResult.getMsg());
                } else {
                    deleteEvents = false;
                    ReportLog.logD("reportEvent 失败 id->" + uploadResult.getMsg());
                }
            } catch(Exception e) {
                deleteEvents = false;
                e.printStackTrace();
                ReportLog.logD("sendData 出错->" + e.getMessage());
                e.printStackTrace();
            } finally {
                if (deleteEvents) {
                    long remainCount = mDbAdapter.deleteEvents(lastId);
                    if (remainCount == 0) {
                        ReportLog.logD("db has clean");
                        hasPendingEvent = false;
                    }
                } else {
                    //暂停此次上传
                    hasPendingEvent = false;
                }
            }
        }
    }

    void onSendEventsSuccess() {

    }

    void onSendEventsFailed() {

    }

    private class WorkHandler extends Handler {
        public WorkHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPLOAD_DATA:
                    //上传数据
                    sendDataAndDeleteDBRecord();
                    break;
            }
        }
    }
}
