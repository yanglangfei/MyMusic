package com.yf.mymusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yf.mymusic.R;
import com.yf.mymusic.activity.MusicPlayActivity;
import com.yf.mymusic.adapter.MusicTypeList;
import com.yf.mymusic.model.Music;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public class MusicList extends Fragment {
    private List<Music> musics = new ArrayList<>();
    private MusicTypeList adapter;
    private ListView lv_hot_music;
    private String getHotList = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ui_hot_list, container, false);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int type=getArguments().getInt("type");
        initData(type);
    }

    private void initView() {
        lv_hot_music = (ListView) view.findViewById(R.id.lv_hot_music);
        adapter = new MusicTypeList(musics, getActivity());
        lv_hot_music.setAdapter(adapter);
        lv_hot_music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
                intent.putExtra("img", musics.get(i).getPic_small());
                intent.putExtra("songId", musics.get(i).getSong_id());
                getActivity().startActivity(intent);
            }
        });
    }



    private void initData(int type) {
        RequestParams param = new RequestParams(getHotList);
        param.addParameter("method", "baidu.ting.billboard.billList");
        param.addParameter("type", type);
        param.addParameter("size", 50);
        param.addParameter("offset", 0);
        x.http().get(param, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray song_list = object.getJSONArray("song_list");
                        for (int i = 0; i < song_list.length(); i++) {
                            JSONObject song = song_list.getJSONObject(i);
                            String title = song.getString("title");
                            String pic_small = song.getString("pic_small");
                            String author = song.getString("author");
                            String song_id = song.optString("song_id");
                            String publishtime = song.optString("publishtime");
                            Music music = new Music();
                            music.setTitle(title);
                            music.setAuthor(author);
                            music.setPic_small(pic_small);
                            music.setSong_id(song_id);
                            music.setPublishtime(publishtime);
                            musics.add(music);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
}
