package com.rjp.dsldemo.action;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.rjp.dsldemo.R;
import com.rjp.dsldemo.bean.ActionBean;
import com.rjp.dsldemo.bean.DataBean;
import com.rjp.dsldemo.bean.ViewBean;
import com.rjp.dsldemo.page.IPage;

import java.util.List;

/**
 * author: jinpeng.ren create at 2020/5/13 21:15
 */
public class SetDataAction implements IAction {

    private IPage page;
    private Handler handler;

    public SetDataAction(IPage page) {
        this.page = page;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void action(ActionBean action) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainThreadSetData(action);
            }
        });
    }

    private void mainThreadSetData(ActionBean action) {
        if (action != null) {
            String response = action.getResponse();
            if (!TextUtils.isEmpty(response)) {
                DataBean dataBean = JSONObject.parseObject(response, DataBean.class);
                List<View> viewCache = page.getViewCache();
                if (viewCache != null && viewCache.size() > 0) {
                    int size = viewCache.size();
                    for (int i = 0; i < size; i++) {
                        View view = viewCache.get(i);
                        bindViewData(view, dataBean);
                    }
                }
            }
        }
    }

    /**
     * 绑定页面数据
     *
     * @param view
     * @param dataBean
     */
    private void bindViewData(View view, DataBean dataBean) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            ViewBean viewBean = (ViewBean) textView.getTag();
            String keyData = dataBean.getData(viewBean.getValue());
            textView.setText(keyData);
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            ViewBean viewBean = (ViewBean) imageView.getTag(R.id.image_tag_id);
            String keyData = dataBean.getData(viewBean.getValue());
            Glide.with(page.getContext()).load(keyData).into(imageView);
        }
    }
}
