package com.yf.mymusic.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.yf.mymusic.R;
import com.yf.mymusic.adapter.HotAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */

public class MusicListFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private View view;
    private ViewPager vp_hot;
    private RadioGroup rg_music_list;
    private HorizontalScrollView sv_lay;
    private List<Fragment> fragments=new ArrayList<>();
  //  type = 1-新歌榜,2-热歌榜,11-摇滚榜,12-爵士,16-流行,21-欧美金曲榜,22-经典老歌榜,23-情歌对唱榜,24-影视金曲榜,25-网络歌曲榜
    private  int type[]={1,2,11,12,16,21,22,23,24,25};
    private HotAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.ui_music_list,container,false);
        initView();
        initFragmant();
        return view;
    }

    private void initFragmant() {
        fragments.clear();
        for (int i = 0; i < type.length; i++) {
            MusicList musicList=new MusicList();
            Bundle build=new Bundle();
            build.putInt("type",type[i]);
            musicList.setArguments(build);
            fragments.add(musicList);
        }
        adapter.notifyDataSetChanged();
    }


    private void initView() {
        sv_lay= (HorizontalScrollView) view.findViewById(R.id.sv_lay);
        rg_music_list= (RadioGroup) view.findViewById(R.id.rg_music_list);
        vp_hot= (ViewPager) view.findViewById(R.id.vp_hot);
        adapter=new HotAdapter(getChildFragmentManager(),fragments);
        vp_hot.setAdapter(adapter);
        rg_music_list.setOnCheckedChangeListener(this);
        vp_hot.setOnPageChangeListener(this);
        if(rg_music_list.getChildCount()>0){
            RadioButton rb= (RadioButton) rg_music_list.getChildAt(0);
            rb.setChecked(true);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton radioButton= (RadioButton) view.findViewById(i);
        if(radioButton.isChecked()){
            sv_lay.scrollTo(radioButton.getLeft(),0);
            int index=rg_music_list.indexOfChild(radioButton);
            vp_hot.setCurrentItem(index);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton rb= (RadioButton) rg_music_list.getChildAt(position);
        rb.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
