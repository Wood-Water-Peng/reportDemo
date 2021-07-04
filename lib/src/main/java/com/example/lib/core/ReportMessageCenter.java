package com.example.lib.core;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.lib.ReportLog;
import com.example.lib.db.DBOperation;
import com.example.lib.db.EventEntity;
import com.example.lib.db.EventReportEntity;
import com.example.lib.utils.NetworkUtils;

import java.util.List;
import java.util.Random;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 8:45 AM
 * @Version 1.0
 * <p>
 * 1.接受其他线程发送过来的消息，放入内部子线程中的消息队列中
 * 2.将json格式的数据加入数据库
 * 3.定时批量上传数据库中的前50条数据
 */
public class ReportMessageCenter {
    private static final int UPLOAD_DATA = 1;
    private static final int DELETE_DATA = 2;
    //上传数据的间隙20s
    private static final int INTERVAL = 20000;
    private static final int LIMIT = 20;
    private final Context mContext;
    private Handler mHandler;
    private static volatile ReportMessageCenter sInstance;
    private ReportHandler reportHandler;
    DBOperation dbOperation;


    private ReportMessageCenter(Context context, ReportHandler reportHandler) {
        HandlerThread thread = new HandlerThread("ReportMessageCenter");
        thread.start();
        this.mHandler = new WorkHandler(thread.getLooper());
        this.reportHandler = reportHandler;
        this.dbOperation = DBOperation.getInstance(context);
        this.mContext = context;
    }

    public static ReportMessageCenter getInstance(Context context, ReportHandler reportHandler) {
        if (sInstance == null) {
            synchronized (ReportMessageCenter.class) {
                if (sInstance == null) {
                    sInstance = new ReportMessageCenter(context, reportHandler);
                }
            }
        }
        return sInstance;
    }

    public void enqueueMsg(int type, EventEntity entity) {
        //1.将eventJson加入数据库并获得游标值
        long cursor = dbOperation.insertEvent(entity);
        if (cursor < 0) {
            ReportLog.logD("enqueueMsg error->" + cursor);
            throw new IllegalStateException("error");
        }

        Message message = Message.obtain();
        message.what = UPLOAD_DATA;
        if (cursor > LIMIT) {
            //2.如果超过50条，发送消息执行网络请求
            mHandler.sendMessage(message);
        } else {
            //3.延迟发送消息，且消息队列中不含有类似消息
            if (!mHandler.hasMessages(message.what)) {
                mHandler.sendMessageDelayed(message, INTERVAL);
            }
        }
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
//                    sendDataAndDeleteDBRecord();
                    break;
            }
        }
    }
}
