package com.example.lib;

import com.example.lib.node.PageNode;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 11:08 AM
 * @Version 1.0
 */
public class NodePath {
    private static NodePath nodePath = new NodePath();

    private NodePath() {
    }

    public static NodePath getInstance() {
        return nodePath;
    }

    Queue<PageNode> nodeQueue = new ArrayDeque<>();


    public void addNode(PageNode node) {
        nodeQueue.add(node);
    }

    public boolean removeNode(PageNode node) {
        return nodeQueue.remove(node);
    }

    public PageNode getHead() {
        if (nodeQueue.isEmpty()) {
            return null;
        }
        return nodeQueue.element();
    }
}
