package com.yf.mymusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yf.mymusic.R;

/**
 * Created by Administrator on 2016/9/26.
 */

public class MyFragment extends Fragment {
    private View view;
    private ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.ui_my_tab,container,false);
        initView();
        return view;
    }

    private void initView() {

    }
}
