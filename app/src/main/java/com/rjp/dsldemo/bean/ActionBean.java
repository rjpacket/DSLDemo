package com.rjp.dsldemo.bean;

/**
 * author: jinpeng.ren create at 2020/5/13 14:14
 */
public class ActionBean {

    /**
     * name : toast
     * msg : click test
     */

    private String name;
    private String msg;
    private String nextPage;
    private String response;
    private String url;
    private String params;
    private String action;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
