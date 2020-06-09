package com.rjp.dsldemo;

import com.alibaba.fastjson.JSONObject;
import com.rjp.dsldemo.action.IAction;
import com.rjp.dsldemo.action.NoneAction;
import com.rjp.dsldemo.action.OpenPageAction;
import com.rjp.dsldemo.action.RequestAction;
import com.rjp.dsldemo.action.SetDataAction;
import com.rjp.dsldemo.action.SetListDataAction;
import com.rjp.dsldemo.action.ToastAction;
import com.rjp.dsldemo.bean.ActionBean;
import com.rjp.dsldemo.constant.ActionType;
import com.rjp.dsldemo.page.IPage;

/**
 * author: jinpeng.ren create at 2020/5/13 15:33
 */
public class ActionFactory {

    public static IAction createAction(IPage page, String action) {
        ActionBean actionBean = JSONObject.parseObject(action, ActionBean.class);
        String name = actionBean.getName();
        switch (name) {
            case ActionType.TOAST:
                return new ToastAction(page);
            case ActionType.OPEN:
                return new OpenPageAction(page);
            case ActionType.SET_DATA:
                return new SetDataAction(page);
            case ActionType.SET_LIST_DATA:
                return new SetListDataAction(page);
            case ActionType.REQUEST:
                return new RequestAction(page);
            default:
                return new NoneAction();
        }
    }
}
