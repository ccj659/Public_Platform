package com.efly.platform.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efly.platform.R;
import com.efly.platform.bean.User;
import com.efly.platform.utils.SharedPreferenceUtil;

/**
 * Created by Administrator on 2016/4/8.
 */
public class HomeFragment extends Fragment {
    private View view;
    private User user;
    private FragmentActivity mainActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Boolean isLogin = SharedPreferenceUtil.getInstance().getIsLogin();
        user = SharedPreferenceUtil.getInstance().getUser();
        if (user == null || isLogin == false) {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }

        mainActivity =  getActivity();
        initView(view);
        initData();
    }

    private void initView(View view) {
    }
    private void initData() {
    }
}
