package com.example.lib.event;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:08 PM
 * @Version 1.0
 */
public class PageOnDestroyEvent extends PageEvent {

    public PageOnDestroyEvent(String name, String id) {
        super(name, id);
        tag = "PageOnDestroyEvent";
    }


}
