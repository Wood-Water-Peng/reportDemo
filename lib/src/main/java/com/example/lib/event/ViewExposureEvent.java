package com.example.lib.event;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 11:07 AM
 * @Version 1.0
 */
public class ViewExposureEvent extends Event {

    private String activityName;
    private String fragmentName;
    private String viewPath;
    private long onCreateTimeStamp;
    private long onDestroyTimeStamp;
    /**
     * View在整个视图树中的唯一id
     * 1.root->viewgroup->viewgroup->view
     */

    private String viewId;

    public String getActivityName() {
        return activityName;
    }

    public long getOnCreateTimeStamp() {
        return onCreateTimeStamp;
    }

    public long getOnDestroyTimeStamp() {
        return onDestroyTimeStamp;
    }

    public ViewExposureEvent(String tag) {
        this.tag = tag;
    }

    public static class Builder {
        private String activityName;
        private String tag;
        private String fragmentName;
        private String viewPath;
        private long onCreateTimeStamp;
        private long onDestroyTimeStamp;

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setViewPath(String viewPath) {
            this.viewPath = viewPath;
            return this;
        }

        public Builder setActivityName(String activityName) {
            this.activityName = activityName;
            return this;
        }

        public Builder setOnCreateTimeStamp(long onCreateTimeStamp) {
            this.onCreateTimeStamp = onCreateTimeStamp;
            return this;
        }

        public void setFragmentName(String fragmentName) {
            this.fragmentName = fragmentName;
        }

        public Builder setOnDestroyTimeStamp(long onDestroyTimeStamp) {
            this.onDestroyTimeStamp = onDestroyTimeStamp;
            return this;
        }

        public ViewExposureEvent build() {
            ViewExposureEvent event = new ViewExposureEvent(this.tag);
            event.activityName = this.activityName;
            event.viewPath = this.viewPath;
            event.onCreateTimeStamp = this.onCreateTimeStamp;
            event.onDestroyTimeStamp = this.onDestroyTimeStamp;
            event.fragmentName = this.fragmentName;
            event.tag="ViewExposureEvent";
            return event;
        }
    }
}
