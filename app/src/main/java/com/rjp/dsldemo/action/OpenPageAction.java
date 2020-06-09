package com.rjp.dsldemo.action;

import android.content.Context;
import android.content.Intent;

import com.rjp.dsldemo.bean.ActionBean;
import com.rjp.dsldemo.page.IPage;
import com.rjp.dsldemo.page.PageActivity;

/**
 * author: jinpeng.ren create at 2020/5/13 17:37
 */
public class OpenPageAction implements IAction {

    private IPage page;

    public OpenPageAction(IPage page) {
        this.page = page;
    }

    @Override
    public void action(ActionBean action) {
        Context context = page.getContext();
        Intent intent = new Intent(context, PageActivity.class);
        intent.putExtra("nextPage", action.getNextPage());
        context.startActivity(intent);
    }
}
