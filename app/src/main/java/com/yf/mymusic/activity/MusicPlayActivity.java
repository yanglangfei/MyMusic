package com.yf.mymusic.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yf.mymusic.R;
import com.yf.mymusic.utils.LrcHandle;
import com.yf.mymusic.view.WordView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/26.
 */

public class MusicPlayActivity extends Activity implements View.OnClickListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private ImageView songImg;
    private String img;
    private MediaPlayer player;
    private ImageView iv_state;
    private boolean isPlay;
    private String songId;
    private String getFile = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    private String file_link;
    private Animation anim;
    private WordView songTxt;
    private SeekBar sb;
    private  TextView tv_current_time;
    private  TextView end_time;
    private SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
    private List<Integer> mTimeList;
    private List<String> mWords = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_play);
        initView();
    }

    private void initView() {
        sb= (SeekBar) findViewById(R.id.sb);
        end_time= (TextView) findViewById(R.id.end_time);
        tv_current_time= (TextView) findViewById(R.id.tv_current_time);
        songTxt= (WordView) findViewById(R.id.songTxt);
        songImg = (ImageView) findViewById(R.id.songImg);
        img = getIntent().getStringExtra("img");
        songId = getIntent().getStringExtra("songId");
        Glide.with(this).load(img)
                .asBitmap()
                .into(new BitmapImageViewTarget(songImg) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(),resource);
                        roundedBitmapDrawable.setCircular(true);
                        songImg.setImageDrawable(roundedBitmapDrawable);
                    }
                });

        anim=AnimationUtils.loadAnimation(this,R.anim.play_anim);
        LinearInterpolator linter=new LinearInterpolator();
        anim.setInterpolator(linter);
        iv_state = (ImageView) findViewById(R.id.iv_state);
        iv_state.setOnClickListener(this);
        player = new MediaPlayer();
        player.setOnBufferingUpdateListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        sb.setOnSeekBarChangeListener(this);
        initData();
    }

    private void initData() {
        // method=baidu.ting.song.play&songid=877578
        RequestParams param = new RequestParams(getFile);
        param.addParameter("method", "baidu.ting.song.play");
        param.addParameter("songid", songId);
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
                        JSONObject songinfo = object.getJSONObject("songinfo");
                        JSONObject bitrate = object.getJSONObject("bitrate");
                        file_link = bitrate.getString("file_link");
                        String lrclink=songinfo.getString("lrclink");
                        initSongLrc(lrclink);
                        if (file_link != null) {
                            try {
                                player.setDataSource(file_link);
                                try {
                                    player.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
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

    private void initSongLrc(String lrclink) {
        RequestParams param=new RequestParams(lrclink);
        Log.i("111",lrclink);
        x.http().get(param, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                Toast.makeText(MusicPlayActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                handleLur(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MusicPlayActivity.this, "errot:"+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(MusicPlayActivity.this, "c", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                Toast.makeText(MusicPlayActivity.this, "fin", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void handleLur(String result) {
        // 处理歌词文件
            String[] line = result.split("\n");
            for (int i = 0; i < line.length; i++) {
                String lines = line[i];
                int beginIndex = lines.indexOf("[");
                int endIndex=lines.indexOf("]");
                String time=lines.substring(beginIndex+1,endIndex);
                Log.i("111","lie:"+lines);
                int current= timeHandler(time);
                mTimeList.add(current);
                mWords.add(lines);
            }
        songTxt.setmWordsList(mWords);
    }


    // 分离出时间
    private int timeHandler(String string) {
        String timeData[] = string.split(":");
        // 分离出分、秒并转换为整型
        int minute = Integer.parseInt(timeData[0]);
        int second = Integer.parseInt(timeData[1]);
        int millisecond = Integer.parseInt(timeData[2]);

        // 计算上一行与下一行的时间转换为毫秒数
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        return currentTime;
    }







    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_state:
                if (isPlay) {
                    isPlay = false;
                    iv_state.setImageResource(R.drawable.zt);
                    changePlayerState(isPlay);
                } else {
                    isPlay = true;
                    iv_state.setImageResource(R.drawable.bf);
                    changePlayerState(isPlay);
                }
                break;
        }
    }

    private void changePlayerState(boolean isPlay) {
        if (file_link != null) {
            if (player != null) {
                if (isPlay) {
                    player.start();
                    songImg.startAnimation(anim);
                    initLur();
                } else {
                    player.pause();
                    songImg.clearAnimation();
                }
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        int totle = mediaPlayer.getDuration();
        int current = mediaPlayer.getCurrentPosition();
        if(mediaPlayer.isPlaying()){
            sb.setMax(totle);
            sb.setProgress(current);
            Date totleDate = new Date(totle);
            Date currentDate = new Date(current);
            end_time.setText( sdf.format(totleDate));
            tv_current_time.setText(sdf.format(currentDate));
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        int totle = mediaPlayer.getDuration();
        int current = mediaPlayer.getCurrentPosition();
        sb.setMax(totle);
        sb.setProgress(current);
        Date totleDate = new Date(totle);
        Date currentDate = new Date(current);
        end_time.setText( sdf.format(totleDate));
        tv_current_time.setText(sdf.format(currentDate));

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        iv_state.setImageResource(R.drawable.tz);
        songImg.clearAnimation();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        player.seekTo(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

        public    void initLur(){
            final Handler handler = new Handler();
            new Thread(new Runnable() {
                int i = 0;
                @Override
                public void run() {
                    while (player.isPlaying()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                songTxt.invalidate();
                            }
                        });
                        try {
                            Thread.sleep(Integer.parseInt(mTimeList.get(i + 1)
                                    .toString())
                                    - Integer.parseInt(mTimeList.get(i).toString()));
                        } catch (InterruptedException e) {
                        }
                        i++;
                        if (i == mTimeList.size() - 1) {
                            player.stop();
                            break;
                        }
                    }
                }
            }).start();
        }
}

