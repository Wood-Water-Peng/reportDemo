package com.example.lib;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 11:08 AM
 * @Version 1.0
 */
public interface EventConstants {
    //应用进程被创建
    String APPLICATION_EVENT_ON_CREATED = "application_event_on_created";
    //应用进程被带到前台
    String APPLICATION_EVENT_ON_RESUMED = "application_event_on_resumed";
    ////应用中所有的activity已经出栈，但是进程可能存在
    String APPLICATION_EVENT_ON_DESTROY = "application_event_on_destroy";


    //activity可见
    String ACTIVITY_EVENT_ON_RESUME = "activity_event_on_resume";

}
