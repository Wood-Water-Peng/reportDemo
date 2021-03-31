package com.example.lib;

import android.text.TextUtils;

import com.example.lib.event.PageLifeCycleEvent;
import com.example.lib.node.ActivityNode;
import com.example.lib.node.FragmentNode;
import com.example.lib.node.PageNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/3/19 8:57 AM
 * @Version 1.0
 * 控制当前显示的所有页面
 * Activity<------------------Activity
 * |
 * |
 * FragA-FragB-FragC
 * |
 * |
 * FragC-FragD
 * <p>
 * <p>
 * 节点的操作情况
 * <p>
 * 入参为节点所属的直接Activity的名字--->找到这个Activity节点
 * <p>
 * 1.新增
 * 2.移除
 * 3.更新节点中的数据
 * <p>
 * 说明：
 * <p>
 * 同一层级下，至少有一个fragment节点可见，如果有多个，则认为最后一个可见的fragment为最终可见的fragment
 */
public class PageNodeManager {
    //当前最新的可见节点
    //当同一层级有多个fragment曝光时，取数组中最后一个
    PageNode head;

    //当前activity的头结点
    ActivityNode activityHead;
    private static PageNodeManager pageNodeManager = new PageNodeManager();


    private PageNodeManager() {
    }

    public static PageNodeManager getInstance() {
        return pageNodeManager;
    }

    public PageNode getActivityHeadNode() {
        return activityHead;
    }

    public PageNode getHeadNode() {
        return head;
    }


    public ActivityNode findActivityNode(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalStateException("name can not be null");
        }

        if (activityHead == null) return null;
        PageNode cur = activityHead;
        if (cur instanceof FragmentNode) {
            cur = ((FragmentNode) cur).hostActivity;
        }
        while (cur != null) {
            if (cur.name.equals(name)) {
                return (ActivityNode) cur;
            }
            cur = cur.pre;
        }
        return null;
    }

    /**
     * 更新fragment节点信息
     */
    public void updateFragmentNodeInfo(String name, String activityName, boolean isVisible) {
        //1.在链表中找到activityName对应的节点
        //2.在activity的节点树中，找到name对应的节点


        //记录操作起始时间
        ActivityNode activityNode = findActivityNode(activityName);
        if (activityNode == null) {
            throw new IllegalStateException("can not find node->" + activityName);
        }

        PageNode fragmentNode = findFragmentNode(name, activityNode.name);

        if (fragmentNode == null) {
            throw new IllegalStateException("can not find node->" + name);
        }
        fragmentNode.isVisible = isVisible;
        //记录操作结束时间
    }

    public FragmentNode findFragmentNode(String fragmentIdentifier, String activityName) {
        if (TextUtils.isEmpty(fragmentIdentifier) || TextUtils.isEmpty(activityName)) {
            return null;
        }

        ActivityNode activityNode = findActivityNode(activityName);
        if (activityNode == null) {
            throw new IllegalStateException("can not find activity:" + activityName);
        }

        return _traversePageNode(activityNode, fragmentIdentifier);
    }

    public void removeActivityNode(String nodeName) {
        ActivityNode activityNode = findActivityNode(nodeName);
        if (activityNode == null) {
            throw new IllegalArgumentException("can not find node->" + nodeName);
        }
        removeActivityNode(activityNode);
    }

    public void removeActivityNode(ActivityNode node) {
        head = node.pre;
        //退出最后一个Activity
        if (head == null) return;
        if (head instanceof ActivityNode) {
            activityHead = (ActivityNode) head;
        } else {
            activityHead = (((FragmentNode) head).hostActivity);
        }
    }

    public void addActivityNode(ActivityNode node) {
        if (node == null) throw new IllegalArgumentException("can not add a null node!!!");
        if (activityHead == null) {
            head = node;
            activityHead = node;
        } else {
            activityHead = node;
            node.pre = head;
            head = node;
        }
        ReportLog.logD("addActivityNode->" + node.name);
    }


    public void removeFragmentNode(String identifier, String activityName) {
        FragmentNode node = findFragmentNode(identifier, activityName);
        if (node == null) {
            throw new IllegalStateException("can not find node->" + identifier);
        }

        if (node.hostFragment == null) {
            node.hostActivity.childFragments.remove(node);
        } else {
            node.hostFragment.children.remove(node);
        }
        if (node == head) {
            head = node.pre;
        }
        ReportLog.logD("removeFragmentNode->" + identifier + "---activity:" + activityName);
    }

    public void addFragmentNode(FragmentNode node, String parent, String activityName) {
        if (node == null) {
            throw new IllegalStateException("node can not be null");
        }
        ActivityNode activityNode = findActivityNode(activityName);
        if (activityNode == null) {
            throw new IllegalStateException("can not find node->" + activityName);
        }


        FragmentNode parentNode = findFragmentNode(parent, activityName);

        if (parentNode == null) {
            activityNode.childFragments.add(node);
            node.pre = activityNode;
            node.hostFragment = parentNode;
        } else {
            parentNode.children.add(node);
            node.pre = parentNode;
        }
        node.hostActivity = activityNode;

        ReportLog.logD("addFragmentNode->" + node.name + "---parent:" + parent + "---activity:" + activityName + "---pre:" + node.pre.name);
    }


    FragmentNode targetNode = null;

    private FragmentNode _traversePageNode(ActivityNode activityNode, String fragmentIdentifier) {
        if (activityNode == null) {
            throw new IllegalStateException("can not find node->" + activityNode.name);
        }
        for (int i = 0; i < activityNode.childFragments.size(); i++) {
            _traverseChildren(activityNode.childFragments.get(i), fragmentIdentifier);
        }
        if (targetNode == null) {
            throw new IllegalStateException("can not find node->" + fragmentIdentifier);
        }
        return targetNode;
    }

    private void _traverseChildren(FragmentNode fragmentNode, String fragmentIdentifier) {
        if (fragmentNode == null) return;
        if (fragmentNode.getIdentifier().equals(fragmentIdentifier)) {
            targetNode = fragmentNode;
            return;
        } else {
            if (fragmentNode.children == null || fragmentNode.children.isEmpty()) return;
            for (int i = 0; i < fragmentNode.children.size(); i++) {
                FragmentNode fragmentNode1 = fragmentNode.children.get(i);
                _traverseChildren(fragmentNode1, fragmentIdentifier);
            }
        }
    }


    public void onFragmentPaused(String identifier, String activityName) {
//        PageLifeCycleEvent pageLifeCycleEvent = new PageLifeCycleEvent(fragmentNode, fragmentNode);
//        pageLifeCycleEvent.name = fragmentNode;
//        pageLifeCycleEvent.report();
//        ReportLog.logD("onFragmentPaused->" + fragmentNode + "---activity:" + activityName);
    }

    public void onActivityPaused(String activityName) {
        PageLifeCycleEvent pageLifeCycleEvent = new PageLifeCycleEvent(activityName, activityName);
        pageLifeCycleEvent.name = activityName;
        pageLifeCycleEvent.report();
        ReportLog.logD("onActivityPaused->" + activityName);
    }

    /**
     * 获取当前显示页面的页面路径
     *
     * @return
     */
    public List<PageNode> getPageNodePath() {
        //找到当前显示的节点
        List<PageNode> list = new ArrayList<>();
        _traversePath(head, list);
        return list;
    }

    private void _traversePath(PageNode node, List<PageNode> list) {
        if (node == null) return;
        _traversePath(node.pre, list);
        list.add(new PageNode(node.name));
    }

    /**
     * 更新head指针位置
     *
     * @param fragmentNode
     */
    public void updateHeadPoint(FragmentNode fragmentNode) {
        head = fragmentNode;
        ReportLog.logD("updateHeadPoint->" + fragmentNode.name);
    }
}
