package com.yf.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yf.mymusic.R;
import com.yf.mymusic.model.Music;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */

public class MusicTypeList extends BaseAdapter {
    private final List<Music> musics;
    private final Context context;

    public MusicTypeList(List<Music> musics, Context context) {
        this.musics=musics;
        this.context=context;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int i) {
        return musics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.ui_hot_item,null);
        }
        ImageView iv_image= (ImageView) view.findViewById(R.id.iv_image);
        TextView tv_title= (TextView) view.findViewById(R.id.tv_title);
        TextView tv_author= (TextView) view.findViewById(R.id.tv_author);
        TextView tv_publish_time= (TextView) view.findViewById(R.id.tv_publish_time);
        tv_publish_time.setText(musics.get(i).getPublishtime());
        tv_author.setText(musics.get(i).getAuthor());
        Glide.with(context).load(musics.get(i).getPic_small()).into(iv_image);
        tv_title.setText(musics.get(i).getTitle());
        return view;
    }
}
