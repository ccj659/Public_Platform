package com.efly.platform.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.efly.platform.R;


/**
 * Created by Administrator on 2016/3/14.
 */
public class PopWinShare extends PopupWindow {

    private View mainView;
    private LinearLayout ll_new_meeting, ll_new_normal;

    public PopWinShare(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.item_popshare_window, null);
        //分享布局
        ll_new_meeting = ((LinearLayout)mainView.findViewById(R.id.ll_new_meeting));
        //复制布局
        ll_new_normal = (LinearLayout)mainView.findViewById(R.id.ll_new_normal);
        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            ll_new_meeting.setOnClickListener(paramOnClickListener);
            ll_new_normal.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
        //设置layout在PopupWindow中显示的位置
        //menuWindow.showAtLocation(this.findViewById(R.id.schoolmain), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 50);
    }


}
