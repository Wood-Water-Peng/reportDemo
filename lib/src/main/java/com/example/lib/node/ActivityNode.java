package com.example.lib.node;

import com.example.lib.node.FragmentNode;
import com.example.lib.node.PageNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 11:29 AM
 * @Version 1.0
 */
public class ActivityNode extends PageNode {
    public ActivityNode(String name) {
        super(name);
    }

    public List<FragmentNode> childFragments=new ArrayList<>();
}
