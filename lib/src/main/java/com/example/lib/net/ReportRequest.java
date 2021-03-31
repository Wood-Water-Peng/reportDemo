package com.example.lib.net;

import com.example.lib.bean.PageEventWrapper;
import com.example.lib.event.PageEvent;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:46 PM
 * @Version 1.0
 */
public class ReportRequest {
    PageEventWrapper event;

    public ReportRequest(PageEventWrapper event) {
        this.event = event;
    }

    public boolean performRequest() {
        //运行在子线程，将数据上报到服务端
        try {
            Thread.sleep(1000);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
