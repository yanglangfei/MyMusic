package com.yf.mymusic.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yf.mymusic.R;
import com.yf.mymusic.model.SearchMusic;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class SearchAdapter extends BaseAdapter {
    private final List<SearchMusic> searchMusics;
    private final Context context;

    public SearchAdapter(List<SearchMusic> searchMusics, Context context) {
        this.searchMusics=searchMusics;
        this.context=context;
    }

    @Override
    public int getCount() {
        return searchMusics.size();
    }

    @Override
    public Object getItem(int i) {
        return searchMusics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.ui_sear_music,null);
        }
        TextView search_Title= (TextView) view.findViewById(R.id.search_Title);
        TextView autor_tv= (TextView) view.findViewById(R.id.autor_tv);
        search_Title.setText(searchMusics.get(i).getSongname());
        autor_tv.setText(searchMusics.get(i).getArtistname());
        return view;
    }
}
