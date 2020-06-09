package com.rjp.dsldemo.page;

import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.List;

/**
 * author: jinpeng.ren create at 2020/5/13 19:37
 */
public interface IPage {

    Context getContext();

    List<View> getViewCache();

    HashMap<String, Object> getVariableMap();
}
