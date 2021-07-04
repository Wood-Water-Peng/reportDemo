package com.example.lib.core;

import androidx.annotation.NonNull;

import com.example.lib.net.ReportNetworkType;

import org.jetbrains.annotations.NotNull;

//测试配置类
public class TestReportConfig extends ReportConfig implements Cloneable {
    private TestReportConfig() {

    }

    public static final class Builder {
        long mMaxCacheSize = 32 * 1024 * 1024L;
        int mFlushInterval;
        String mServerUrl;
        int mNetworkTypePolicy = ReportNetworkType.TYPE_3G | ReportNetworkType.TYPE_4G | ReportNetworkType.TYPE_WIFI | ReportNetworkType.TYPE_5G;
        boolean isSubProcessFlushData = false;

        public Builder setMaxCacheSize(long mMaxCacheSize) {
            this.mMaxCacheSize = mMaxCacheSize;
            return this;
        }

        public Builder setFlushInterval(int mFlushInterval) {
            this.mFlushInterval = mFlushInterval;
            return this;
        }

        public Builder setServerUrl(String mServerUrl) {
            this.mServerUrl = mServerUrl;
            return this;
        }

        public Builder setNetworkTypePolicy(int mNetworkTypePolicy) {
            this.mNetworkTypePolicy = mNetworkTypePolicy;
            return this;
        }

        public Builder setSubProcessFlushData(boolean subProcessFlushData) {
            isSubProcessFlushData = subProcessFlushData;
            return this;
        }

        public Builder(String serverUrl) {
            this.mServerUrl = serverUrl;
        }

        public Builder(ReportConfig config) {
            this.mMaxCacheSize = config.mMaxCacheSize;
            this.mFlushInterval = config.mFlushInterval;
            this.mServerUrl = config.mServerUrl;
            this.mNetworkTypePolicy = config.mNetworkTypePolicy;
            this.isSubProcessFlushData = config.isSubProcessFlushData;
        }

        public TestReportConfig build() {
            TestReportConfig testReportConfig = new TestReportConfig();
            testReportConfig.mMaxCacheSize = mMaxCacheSize;
            testReportConfig.mFlushInterval = mFlushInterval;
            testReportConfig.mServerUrl = mServerUrl;
            testReportConfig.mNetworkTypePolicy = mNetworkTypePolicy;
            testReportConfig.isSubProcessFlushData = isSubProcessFlushData;
            return testReportConfig;
        }
    }

    @NonNull
    @Override
    protected TestReportConfig clone() {
        TestReportConfig copyObject = this;
        try {
            copyObject = (TestReportConfig) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return copyObject;
    }
}
