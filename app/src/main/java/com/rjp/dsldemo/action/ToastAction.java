package com.rjp.dsldemo.action;

import android.widget.Toast;

import com.rjp.dsldemo.bean.ActionBean;
import com.rjp.dsldemo.page.IPage;

/**
 * author: jinpeng.ren create at 2020/5/13 15:27
 */
public class ToastAction implements IAction {

    private IPage page;

    public ToastAction(IPage page) {
        this.page = page;
    }

    @Override
    public void action(ActionBean action) {
        Toast.makeText(page.getContext(), action.getMsg(), Toast.LENGTH_SHORT).show();
    }
}
