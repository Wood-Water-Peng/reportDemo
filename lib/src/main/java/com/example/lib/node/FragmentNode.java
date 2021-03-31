package com.example.lib.node;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 11:29 AM
 * @Version 1.0
 */
public class FragmentNode extends PageNode {
    public FragmentNode(String name) {
        super(name);
    }

    public ActivityNode hostActivity;
    public FragmentNode hostFragment;
    public List<FragmentNode> children = new ArrayList<>();

}
