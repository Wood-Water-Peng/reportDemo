package com.example.lib.bean;

import com.example.lib.event.PageEvent;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 7:53 PM
 * @Version 1.0
 * <p>
 * 事件的封装类，用于上传到服务器
 */
public class PageEventWrapper {
    PageEvent pageEvent;
    int num;
    String content;

    public int getNum() {
        return num;
    }

    public String getContent() {
        return content;
    }

    public PageEventWrapper(PageEvent pageEvent) {
        this.pageEvent = pageEvent;
    }

    public PageEventWrapper(int num, String content) {
        this.num = num;
        this.content = content;
    }
}
