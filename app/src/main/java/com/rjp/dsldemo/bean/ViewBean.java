package com.rjp.dsldemo.bean;

import java.util.List;

/**
 * author: jinpeng.ren create at 2020/5/13 14:10
 */
public class ViewBean {

    private String id;
    private String name;
    private String content;
    private String color;
    private String value;
    private float size = 14.0f;
    private int width;
    private int height;
    private List<ViewBean> children;
    private String action;
    private String url;
    private String itemType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<ViewBean> getChildren() {
        return children;
    }

    public void setChildren(List<ViewBean> children) {
        this.children = children;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * name : TV
     * content : TEST
     * color : #333333
     * size : 16
     * action : {"name":"toast","msg":"click test"}
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
