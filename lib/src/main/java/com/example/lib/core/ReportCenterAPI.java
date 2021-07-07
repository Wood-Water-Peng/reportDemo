package com.example.lib.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.lib.BuildConfig;
import com.example.lib.TrackEventManager;
import com.example.lib.TrackEventManagerThread;
import com.example.lib.assit.ActivityIntoAssit;
import com.example.lib.interceptor.IPropertyComposer;
import com.example.lib.interceptor.LastPropertyComposer;
import com.example.lib.interceptor.LibInfoPropertyComposer;
import com.example.lib.interceptor.PropertyComposeChain;
import com.example.lib.utils.AppInfoUtils;
import com.example.lib.utils.DeviceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//核心类
public class ReportCenterAPI extends AbstractReportCenter {
    private static ReportConfig mReportConfig;
    private final TrackEventManager mTrackTaskManager;
    private final TrackEventManagerThread mTrackTaskManagerRunnable;
    EventHandlerCenter mEventHandlerCenter;
    Context mContext;
    public Map<String, Object> mDeviceInfo;
    protected static final Map<Context, ReportCenterAPI> sInstanceMap = new HashMap<>();
    private boolean mIsDebugMode = true;
    private ActivityIntoAssit activityIntoAssit;
    // Session 时长
    protected int mSessionTime = 5 * 1000;
    public ActivityIntoAssit getActivityIntoAssit() {
        return activityIntoAssit;
    }

    public int getSessionTime() {
        return mSessionTime;
    }

    ReportCenterAPI(Context context, String serverUrl) {
        this.mContext = context;
        final String packageName = mContext.getApplicationContext().getPackageName();
        initConfigInternal(serverUrl, packageName);
        //开启接受事件的线程
        mTrackTaskManager = TrackEventManager.getsInstance();
        mTrackTaskManagerRunnable = new TrackEventManagerThread();
        new Thread(mTrackTaskManagerRunnable).start();
        DBAdapter.getInstance(mContext, packageName);
        //初始化事件处理类
        mEventHandlerCenter = new EventHandlerCenter(this);
        //获取设备信息
        mDeviceInfo = setupDeviceInfo();
        activityIntoAssit = ActivityIntoAssit.getInstance(this);
    }

    /**
     * 初始化SDK
     *
     * @param context App 的 Context
     * @param config  SDK 的配置项
     */
    public static void initWithConfigOptions(Context context, TestReportConfig config) {
        if (context == null || config == null) {
            throw new NullPointerException("Context、SAConfigOptions 不可以为 null");
        }
        mReportConfig = config.clone();
        ReportCenterAPI sensorsDataAPI = getInstance(context, config.mServerUrl);

    }

    public static ReportCenterAPI sharedInstance() {

        synchronized (sInstanceMap) {
            if (sInstanceMap.size() > 0) {
                Iterator<ReportCenterAPI> iterator = sInstanceMap.values().iterator();
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
            return null;
        }
    }

    public static ReportCenterAPI getInstance(Context context, String serverURL) {
        if (null == context) {
            throw new NullPointerException("Context 不可以为 null");
        }

        synchronized (sInstanceMap) {
            final Context appContext = context.getApplicationContext();
            ReportCenterAPI instance = sInstanceMap.get(appContext);
            if (null == instance) {
                instance = new ReportCenterAPI(appContext, serverURL);
                sInstanceMap.put(appContext, instance);
            }
            return instance;
        }
    }

    protected void initConfigInternal(String serverUrl, String packageName) {

        Bundle configBundle = null;
        try {
            final ApplicationInfo appInfo = mContext.getApplicationContext().getPackageManager()
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            configBundle = appInfo.metaData;
        } catch (final PackageManager.NameNotFoundException e) {
            //打印异常
        }

        if (null == configBundle) {
            configBundle = new Bundle();
        }

        if (mReportConfig == null) {
            mReportConfig = new TestReportConfig.Builder(serverUrl).build();
        }
        TestReportConfig.Builder builder = new TestReportConfig.Builder(mReportConfig);
        //从清单文件中取出mete-data数据
        if (mReportConfig.mFlushInterval == 0) {
            builder.setFlushInterval(configBundle.getInt("com.example.reportdemo.FlushInterval",
                    5_000));
        }
        mReportConfig = builder.build();
    }

    /**
     * @param sendProperties 事件属性
     */
    public void trackInternal(final JSONObject sendProperties) {
        //将组装好的数据入队
        mEventHandlerCenter.enqueueEventMessage(null, sendProperties);
    }

    public void trackAutoEvent(final String eventName, final JSONObject properties) {
        trackEvent(eventName, properties);
    }

    public void trackEvent(final String eventName, final JSONObject properties) {
        mTrackTaskManager.addTrackEventTask(new Runnable() {
            @Override
            public void run() {
                try {
                    List<IPropertyComposer> composers = new ArrayList<>();
                    composers.add(new CommonInfoPropertyComposer());
                    composers.add(new DeviceInfoPropertyComposer());
                    composers.add(new LibInfoPropertyComposer());
                    composers.add(new LastPropertyComposer());
                    properties.put("event", eventName);
                    PropertyComposeChain chain = new PropertyComposeChain(composers, ReportCenterAPI.this);
                    JSONObject response = chain.proceed(properties);
                    trackInternal(response);
                } catch (Exception e) {

                }
            }
        });
    }

    public static final String VERSION = BuildConfig.SDK_VERSION;

    protected Map<String, Object> setupDeviceInfo() {
        final Map<String, Object> deviceInfo = new HashMap<>();
        deviceInfo.put("$lib", "Android");
        deviceInfo.put("$lib_version", VERSION);
        deviceInfo.put("$os", "Android");
        deviceInfo.put("$os_version", DeviceUtils.getOS());
        deviceInfo.put("$manufacturer", DeviceUtils.getManufacturer());
        deviceInfo.put("$model", DeviceUtils.getModel());
        deviceInfo.put("$app_version", AppInfoUtils.getAppVersionName(mContext));
        int[] size = DeviceUtils.getDeviceSize(mContext);
        deviceInfo.put("$screen_width", size[0]);
        deviceInfo.put("$screen_height", size[1]);
        deviceInfo.put("$app_id", AppInfoUtils.getProcessName(mContext));
        deviceInfo.put("$app_name", AppInfoUtils.getAppName(mContext));
        return Collections.unmodifiableMap(deviceInfo);
    }

    public boolean isDebugMode() {
        return mIsDebugMode;
    }

    public int getFlushInterval() {
        return mReportConfig.mFlushInterval;
    }

    @Override
    public void trackAppInstall(JSONObject jsonObject) {
        super.trackAppInstall(jsonObject);
    }

    @Override
    public void trackAppInstall() {

    }
}
