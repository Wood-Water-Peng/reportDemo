package com.example.lib.core;

import android.content.Context;

import com.example.lib.EventReportEngine;
import com.example.lib.ReportLog;
import com.example.lib.ReportMessageCenter;
import com.example.lib.TrackEventManager;
import com.example.lib.TrackEventManagerThread;
import com.example.lib.db.EventEntity;
import com.example.lib.event.Event;
import com.example.lib.executor.ReportExecutor;
import com.example.lib.utils.DeviceUtils;
import com.example.lib.utils.JSONUtils;
import com.example.lib.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:13 PM
 * @Version 1.0
 */
public class ReportHandler {
    EventReportEngine reportEngine;
    //监听任务的任务
    protected TrackEventManagerThread mTrackTaskManagerThread;
    protected TrackEventManager mTrackTaskManager;
    ReportMessageCenter messageCenter;
    Context mContext;

    public EventReportEngine getReportEngine() {
        return reportEngine;
    }

    private ReportHandler() {
    }

    public static ReportHandler getInstance() {
        return ReportHandlerHolder.INSTANCE;
    }

    private static class ReportHandlerHolder {
        static final ReportHandler INSTANCE = new ReportHandler();
    }

    private volatile boolean _hasInit = false;

    public void init(Context context) {
        if (_hasInit) {
            throw new IllegalStateException("has already inited");
        }
        this.mContext = context;
        reportEngine = new EventReportEngine(new ReportExecutor());
        _hasInit = true;
        if (mTrackTaskManagerThread == null) {
            mTrackTaskManagerThread = new TrackEventManagerThread();
            new Thread(mTrackTaskManagerThread).start();
            ReportLog.logD("Data collection thread has been started");
        }
        mTrackTaskManager = TrackEventManager.getsInstance();
        messageCenter = ReportMessageCenter.getInstance(context, this);
    }

    public void trackViewClick(Event event, JSONObject object) {
        track(event, object);
    }

    public void track(final Event event) {
        track(event, null);
    }

    public void track(final Event event, final JSONObject object) {
        mTrackTaskManager.addTrackEventTask(new Runnable() {
            @Override
            public void run() {
                _trackEvent(event, object);
            }
        });
    }

    private void _trackEvent(Event event, JSONObject object) {
        //1.将事件添加到数据库

        //2.根据数据库中当前的条目，立刻上传或者延迟20s之后执行上传任务

        //3.将数据库操作和上传任务放在一个线程中执行，只有上传成功后才删除数据库的记录


        //生成一个json格式的str,通过handler发送到子线程
        long eventTime = System.currentTimeMillis();
        //公共属性
        JSONObject publicProperties = new JSONObject();

        try {
            publicProperties.put("$os_version", DeviceUtils.getOS());
            publicProperties.put("$model", DeviceUtils.getModel());
            publicProperties.put("$manufacturer", DeviceUtils.getModel());
            // 当前网络状况
            String networkType = NetworkUtils.networkType(mContext);
            publicProperties.put("$wifi", "WIFI".equals(networkType));
            publicProperties.put("$network_type", networkType);

            if (object != null) {
                Map<String, String> map = JSONUtils.json2Map(object);
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    publicProperties.put(key, map.get(key));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //最外层的
        final JSONObject dataObj = new JSONObject();

        try {
            SecureRandom random = new SecureRandom();
            dataObj.put("_track_id", random.nextInt());
            dataObj.put("time", eventTime);
            dataObj.put("type", event.tag);
            dataObj.put("properties", publicProperties);
        } catch (Exception e) {
            // ignore
        }

        EventEntity entity = new EventEntity();
        entity.setName(event.tag);
        entity.setData(dataObj.toString());
        entity.setCreate_time(System.currentTimeMillis());
        messageCenter.enqueueMsg(0, entity);
        ReportLog.logD("track event->\n" + JSONUtils.formatJson(dataObj.toString()));
    }

    /**
     * 网络类型
     */
    public final class NetworkType {
        public static final int TYPE_NONE = 0;//NULL
        public static final int TYPE_2G = 1;//2G
        public static final int TYPE_3G = 1 << 1;//3G
        public static final int TYPE_4G = 1 << 2;//4G
        public static final int TYPE_WIFI = 1 << 3;//WIFI
        public static final int TYPE_5G = 1 << 4;//5G
        public static final int TYPE_ALL = 0xFF;//ALL
    }
}
