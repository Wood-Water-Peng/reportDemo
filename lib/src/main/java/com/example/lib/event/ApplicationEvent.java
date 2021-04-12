package com.example.lib.event;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 11:07 AM
 * @Version 1.0
 */
public class ApplicationEvent extends Event {

    private String packageName;
    private long onCreateTimeStamp;
    private long onDestroyTimeStamp;

    public String getPackageName() {
        return packageName;
    }

    public long getOnCreateTimeStamp() {
        return onCreateTimeStamp;
    }

    public long getOnDestroyTimeStamp() {
        return onDestroyTimeStamp;
    }

    public ApplicationEvent(String tag) {
        this.tag = tag;
    }

    public static class Builder {
        private String packageName;
        private String tag;
        private long onCreateTimeStamp;
        private long onDestroyTimeStamp;

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder setOnCreateTimeStamp(long onCreateTimeStamp) {
            this.onCreateTimeStamp = onCreateTimeStamp;
            return this;
        }

        public Builder setOnDestroyTimeStamp(long onDestroyTimeStamp) {
            this.onDestroyTimeStamp = onDestroyTimeStamp;
            return this;
        }

        public ApplicationEvent build() {
            ApplicationEvent event = new ApplicationEvent(this.tag);
            event.packageName = this.packageName;
            event.onCreateTimeStamp = this.onCreateTimeStamp;
            event.onDestroyTimeStamp = this.onDestroyTimeStamp;
            return event;
        }
    }
}
