package com.rjp.dsldemo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rjp.dsldemo.LayoutFactory;
import com.rjp.dsldemo.R;
import com.rjp.dsldemo.bean.DataBean;
import com.rjp.dsldemo.bean.ViewBean;
import com.rjp.dsldemo.page.IPage;

import java.util.ArrayList;
import java.util.List;

/**
 * author: jinpeng.ren create at 2020/5/14 21:27
 */
public class CommonAdapter extends BaseAdapter {

    private IPage page;
    private ViewBean viewBean;
    private List<DataBean> datas = new ArrayList<>();

    public CommonAdapter(IPage page, ViewBean viewBean) {
        this.page = page;
        this.viewBean = viewBean;
    }

    @Override
    public int getCount() {
        return getDatas().size();
    }

    @Override
    public int getViewTypeCount() {
        List<ViewBean> children = viewBean.getChildren();
        return children == null ? 0 : children.size();
    }

    @Override
    public int getItemViewType(int position) {
        DataBean dataBean = datas.get(position);
        return Integer.parseInt(dataBean.getItemType());
    }

    @Override
    public Object getItem(int position) {
        return getDatas().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataBean dataBean = getDatas().get(position);
        String itemType = dataBean.getItemType();
        ViewBean viewBean = getViewBeanByItemType(itemType);
        if (convertView == null) {
            convertView = LayoutFactory.createView(page, viewBean);
        }
        bindListViewData(convertView, dataBean);
        return convertView;
    }

    private void bindListViewData(View view, DataBean dataBean) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                bindListViewData(child, dataBean);
            }
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            ViewBean tagBean = (ViewBean) textView.getTag();
            String keyData = dataBean.getData(tagBean.getValue());
            textView.setText(keyData);
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            ViewBean tagBean = (ViewBean) imageView.getTag(R.id.image_tag_id);
            String keyData = dataBean.getData(tagBean.getValue());
            Glide.with(page.getContext()).load(keyData).into(imageView);
        }
    }

    /**
     * 获取一个类型的数据
     *
     * @param itemType
     * @return
     */
    private ViewBean getViewBeanByItemType(String itemType) {
        List<ViewBean> children = viewBean.getChildren();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            ViewBean viewBean = children.get(i);
            if (itemType.equals(viewBean.getItemType())) {
                return viewBean;
            }
        }
        return null;
    }

    public List<DataBean> getDatas() {
        return datas;
    }
}
