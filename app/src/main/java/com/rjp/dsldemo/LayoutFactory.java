package com.rjp.dsldemo;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.rjp.dsldemo.action.IAction;
import com.rjp.dsldemo.adapter.CommonAdapter;
import com.rjp.dsldemo.bean.ActionBean;
import com.rjp.dsldemo.bean.ViewBean;
import com.rjp.dsldemo.constant.ViewType;
import com.rjp.dsldemo.page.IPage;

import java.util.List;

/**
 * author: jinpeng.ren create at 2020/5/13 13:59
 */
public class LayoutFactory {

    public static View createView(IPage page, ViewBean viewBean) {
        String name = viewBean.getName();
        switch (name) {
            case ViewType.VLL:
                return createLinearLayout(page, viewBean, true);
            case ViewType.HLL:
                return createLinearLayout(page, viewBean, false);
            case ViewType.TV:
                return createTextView(page, viewBean);
            case ViewType.IV:
                return createImageView(page, viewBean);
            case ViewType.LV:
                return createListView(page, viewBean);
            default:
                return new View(page.getContext());
        }
    }

    private static View createListView(IPage page, ViewBean viewBean) {
        ListView listView = new ListView(page.getContext());
        listView.setAdapter(new CommonAdapter(page, viewBean));
        List<View> viewCache = page.getViewCache();
        if (viewCache != null) {
            listView.setTag(viewBean.getId());
            viewCache.add(listView);
        }
        return listView;
    }

    private static View createLinearLayout(IPage page, ViewBean viewBean, boolean isVertical) {
        LinearLayout vll = new LinearLayout(page.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        vll.setLayoutParams(layoutParams);
        vll.setOrientation(isVertical ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
        List<ViewBean> children = viewBean.getChildren();
        if (children != null && children.size() > 0) {
            int size = children.size();
            for (int i = 0; i < size; i++) {
                vll.addView(createView(page, children.get(i)));
            }
        }
        return vll;
    }

    private static View createImageView(IPage page, ViewBean viewBean) {
        ImageView imageView = new ImageView(page.getContext());
        imageView.setTag(R.id.image_tag_id, viewBean);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(viewBean.getWidth(), viewBean.getHeight());
        imageView.setLayoutParams(layoutParams);
        String url = viewBean.getUrl();
        if (!TextUtils.isEmpty(url)) {
            Glide.with(page.getContext()).load(url).into(imageView);
        }
        if (!TextUtils.isEmpty(viewBean.getValue())) {
            List<View> viewCache = page.getViewCache();
            if (viewCache != null) {
                viewCache.add(imageView);
            }
        }
        return imageView;
    }

    /**
     * 创建一个TextView
     *
     * @param page
     * @param viewBean
     * @return
     */
    private static View createTextView(IPage page, ViewBean viewBean) {
        TextView tv = new TextView(page.getContext());
        try {
            tv.setTextColor(Color.parseColor(viewBean.getColor()));
        } catch (Exception e) {
            e.printStackTrace();
            tv.setTextColor(Color.parseColor("#333333"));
        }
        try {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, viewBean.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.0f);
        }
        if (!TextUtils.isEmpty(viewBean.getValue())) {
            List<View> viewCache = page.getViewCache();
            if (viewCache != null) {
                viewCache.add(tv);
            }
        }
        tv.setText(viewBean.getContent());
        tv.setTag(viewBean);
        tv.setOnClickListener(v -> {
            ViewBean bean = (ViewBean) v.getTag();
            IAction action = ActionFactory.createAction(page, bean.getAction());
            action.action(JSONObject.parseObject(bean.getAction(), ActionBean.class));
        });
        return tv;
    }

}
