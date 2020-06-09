package com.rjp.dsldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.rjp.dsldemo.page.PageActivity;
import com.rjp.dsldemo.util.FileUtil;

public class MainActivity extends AppCompatActivity {

    private String pageJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageJson = FileUtil.readAssets2String(this, "three_button.json");
    }

    public void startDSL(View view) {
        PageActivity.start(this, pageJson);
    }
}
