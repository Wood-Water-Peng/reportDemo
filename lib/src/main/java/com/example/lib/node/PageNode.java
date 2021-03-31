package com.example.lib.node;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 11:00 AM
 * @Version 1.0
 */
public class PageNode {
    //节点的标识,用于查找维护的节点
    private String mIdentifier;
    public boolean isVisible = false;
    //节点创建时间
    public long createTime;
    //节点被销毁时间
    public long destroyTime;

    public PageNode(String name) {
        this.name = name;
        this.mIdentifier = name;
    }

    String name;
    public PageNode pre;

    //获取节点存活的总时间
    public long getSurviveTime() {
        return destroyTime - createTime;
    }


    public String getIdentifier() {
        return mIdentifier;
    }

    public void setIdentifier(String mIdentifier) {
        this.mIdentifier = mIdentifier;
    }
}
