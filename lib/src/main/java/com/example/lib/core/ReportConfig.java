package com.example.lib.core;

import com.example.lib.net.ReportNetworkType;

//配置信息
public abstract class ReportConfig {
    /**
     * 本地缓存上限值，单位 byte，默认为 32MB：32 * 1024 * 1024
     */
    long mMaxCacheSize = 32 * 1024 * 1024L;

    /**
     * 两次数据发送的最小时间间隔，单位毫秒
     */
    int mFlushInterval;

    /**
     * 数据上报服务器地址
     */
    String mServerUrl;

    /**
     * 网络上传策略
     */
    int mNetworkTypePolicy = ReportNetworkType.TYPE_3G | ReportNetworkType.TYPE_4G | ReportNetworkType.TYPE_WIFI | ReportNetworkType.TYPE_5G;

    /**
     * 是否子进程上报数据
     */
    boolean isSubProcessFlushData = false;
}
