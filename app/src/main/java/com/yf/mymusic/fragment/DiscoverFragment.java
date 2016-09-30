package com.yf.mymusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.yf.mymusic.R;
import com.yf.mymusic.activity.MusicPlayActivity;
import com.yf.mymusic.adapter.SearchAdapter;
import com.yf.mymusic.model.SearchMusic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */

public class DiscoverFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView searchList;
    private Button btn_search;
    private Spinner sp_type;
    private EditText searchInfo;
    private View view;
    private int type;
    private SearchAdapter adapter;
    private  String baseUrl="http://tingapi.ting.baidu.com/v1/restserver/ting";
    private List<SearchMusic> searchMusics=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.ui_sear_list,container,false);
        initView();
        return view;
    }

    private void initData() {
        String into=searchInfo.getText().toString();
        if(into.length()<=0){
            Toast.makeText(getActivity(), "请输入要搜索的内容", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams param=new RequestParams(baseUrl);
        param.addParameter("method","baidu.ting.search.catalogSug");
        param.addParameter("query",into);
        x.http().get(param, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                if(result!=null){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONArray array=object.getJSONArray("song");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject song=array.getJSONObject(i);
                            String songname=song.getString("songname");
                            String artistname=song.getString("artistname");
                            String songid=song.getString("songid");
                            SearchMusic searchMusic=new SearchMusic();
                            searchMusic.setSongname(songname);
                            searchMusic.setArtistname(artistname);
                            searchMusic.setSongid(songid);
                            searchMusics.add(searchMusic);
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

    private void initView() {
        searchList= (ListView) view.findViewById(R.id.searchList);
        btn_search= (Button) view.findViewById(R.id.btn_search);
        sp_type= (Spinner) view.findViewById(R.id.sp_type);
        searchInfo= (EditText) view.findViewById(R.id.searchInfo);
        sp_type.setOnItemSelectedListener(this);
        btn_search.setOnClickListener(this);
        adapter=new SearchAdapter(searchMusics,getActivity());
        searchList.setAdapter(adapter);
        searchList.setOnItemClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type=i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        searchMusics.clear();
        adapter.notifyDataSetChanged();
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(getActivity(), MusicPlayActivity.class);
        intent.putExtra("songId",searchMusics.get(i).getSongid());
        getActivity().startActivity(intent);

    }
}
