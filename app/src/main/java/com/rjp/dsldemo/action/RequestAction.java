package com.rjp.dsldemo.action;

import com.alibaba.fastjson.JSONObject;
import com.rjp.dsldemo.ActionFactory;
import com.rjp.dsldemo.bean.ActionBean;
import com.rjp.dsldemo.page.IPage;

/**
 * author: jinpeng.ren create at 2020/5/14 17:09
 */
public class RequestAction implements IAction {

    private IPage page;

    public RequestAction(IPage page) {
        this.page = page;
    }

    @Override
    public void action(ActionBean action) {
        if (action != null) {
            try {
                Thread.sleep(2_000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String response = "{\n" +
                    "      \"a\": \"这是通过请求获取的数据，真的！\"\n" +
                    "    }";
            String lastAction = action.getAction();
            ActionBean lastActionBean = JSONObject.parseObject(lastAction, ActionBean.class);
            lastActionBean.setResponse(response);
            ActionFactory.createAction(page, lastAction).action(lastActionBean);
        }
    }
}
