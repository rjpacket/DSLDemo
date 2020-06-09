package com.rjp.dsldemo.action;

import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.rjp.dsldemo.adapter.CommonAdapter;
import com.rjp.dsldemo.bean.ActionBean;
import com.rjp.dsldemo.bean.DataBean;
import com.rjp.dsldemo.page.IPage;

import java.util.List;

/**
 * author: jinpeng.ren create at 2020/5/21 17:25
 */
public class SetListDataAction implements IAction {

    private IPage page;

    public SetListDataAction(IPage page) {
        this.page = page;
    }

    @Override
    public void action(ActionBean action) {
        if (action != null) {
            String response = action.getResponse();
            JSONObject responseJson = JSONObject.parseObject(response);
            String listViewId = responseJson.getString("id");
            List<DataBean> dataBeans = JSONObject.parseArray(responseJson.getString("list"), DataBean.class);
            List<View> viewCache = page.getViewCache();
            int size = viewCache.size();
            for (int i = 0; i < size; i++) {
                View view = viewCache.get(i);
                if (view instanceof ListView) {
                    ListView listView = (ListView) view;
                    String tagId = (String) listView.getTag();
                    if (listViewId.equals(tagId)) {
                        CommonAdapter adapter = (CommonAdapter) listView.getAdapter();
                        List<DataBean> datas = adapter.getDatas();
                        datas.addAll(dataBeans);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
