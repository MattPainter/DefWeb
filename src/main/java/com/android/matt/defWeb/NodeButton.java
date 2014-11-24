package com.android.matt.defWeb;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Matt on 08/09/2014. Node button is button with extra data storage associated
 */
public class NodeButton extends Button {
    private String[] nodeParents;
    private String[] nodeChildren;
    private String nodeData;

    public NodeButton(Context context) {
        super(context);
    }

    public NodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NodeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setNodeParents(String[] parents) {
        nodeParents = parents;
    }

    public void setNodeChildren(String[] children) {
        nodeChildren = children;
    }

    public void setNodeData(String data) {
        nodeData = data;
    }

    public String getNodeData() {
        return nodeData;
    }

    public String[] getNodeParents() {
        return nodeParents;
    }

    public String[] getNodeChildren() {
        return nodeChildren;
    }

}
