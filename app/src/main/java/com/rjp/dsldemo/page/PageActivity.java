package com.rjp.dsldemo.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.rjp.dsldemo.LayoutFactory;
import com.rjp.dsldemo.R;
import com.rjp.dsldemo.bean.PageBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageActivity extends AppCompatActivity implements IPage {
    /** view缓存，方便刷新 */
    private List<View> viewCache = new ArrayList<>();
    /** 变量表 */
    private HashMap<String, Object> variableMap = new HashMap<>();

    public static void start(Context context, String pageJson) {
        Intent starter = new Intent(context, PageActivity.class);
        starter.putExtra("nextPage", pageJson);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        FrameLayout pageContainer = findViewById(R.id.page_container);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nextPage")) {
            String nextPage = intent.getStringExtra("nextPage");
            PageBean pageBean = JSONObject.parseObject(nextPage, PageBean.class);
            if (pageBean != null) {
                pageContainer.addView(LayoutFactory.createView(this, pageBean.getLayout()));
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public List<View> getViewCache() {
        return viewCache;
    }

    @Override
    public HashMap<String, Object> getVariableMap() {
        return variableMap;
    }
}
