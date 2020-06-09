package com.rjp.dsldemo.bean;

/**
 * author: jinpeng.ren create at 2020/5/13 18:18
 */
public class PageBean {
    private String contextName;
    private ViewBean layout;

    public ViewBean getLayout() {
        return layout;
    }

    public void setLayout(ViewBean layout) {
        this.layout = layout;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }
}
