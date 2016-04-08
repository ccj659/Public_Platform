package com.efly.platform.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.efly.platform.R;
import com.efly.platform.view.TitleBar;

/**
 * Created by Administrator on 2016/4/8.
 */
public class BaseActivity extends FragmentActivity{
    private TitleBar titleBar;



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setUpHeader();//标题
        getScreenMetric();//屏幕大小
    }

    private void getScreenMetric() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

    }

    private void setUpHeader() {
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setImmersive(true);
        titleBar.setBackgroundColor(getResources().getColor(R.color.blue));

        titleBar.setLeftImageResource(R.mipmap.back);
//        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        titleBar.setTitle(currentTab);
        titleBar.setTitleColor(Color.WHITE);
        titleBar.setSubTitleColor(Color.WHITE);
        titleBar.setDividerColor(Color.GRAY);
    }

    public TitleBar getTitleBar(){
        setUpHeader();
        return  titleBar;
    }

    public TitleBar getTitleBarInstance(){
        if (titleBar==null){
            setUpHeader();
        }
        return titleBar;
    }



}
