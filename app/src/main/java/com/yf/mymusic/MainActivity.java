package com.yf.mymusic;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.yf.mymusic.adapter.MusicTypeList;
import com.yf.mymusic.fragment.DiscoverFragment;
import com.yf.mymusic.fragment.MusicListFragment;
import com.yf.mymusic.fragment.MyFragment;
import com.yf.mymusic.model.Music;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost tabHost;
    private  String tabStr[]={"音乐榜","发现","我的"};
    private  int tabDraw[]={R.drawable.music_tab_select,R.drawable.discover_tab,R.drawable.my_tab};
    private  Class tabClass[]={MusicListFragment.class, DiscoverFragment.class, MyFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this,getSupportFragmentManager(),R.id.lay_cont);
        for (int i = 0; i < tabStr.length; i++) {
            TabHost.TabSpec tab = tabHost.newTabSpec(tabStr[i]);
            View view= LayoutInflater.from(this).inflate(R.layout.ui_tab,null);
            TextView tv_tab_str= (TextView) view.findViewById(R.id.tv_tab_str);
            ImageView iv_tab= (ImageView) view.findViewById(R.id.iv_tab);
            iv_tab.setBackgroundDrawable(ContextCompat.getDrawable(this,tabDraw[i]));
            tv_tab_str.setText(tabStr[i]);
            tab.setIndicator(view);
            tabHost.addTab(tab,tabClass[i],null);
        }

    }


}
