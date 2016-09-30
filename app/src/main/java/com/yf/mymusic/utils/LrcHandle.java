package com.yf.mymusic.utils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/***
 * 获取时间以及对应的歌词
 *
 * @author chao
 *
 */
public class LrcHandle {
    private  String lurTxt;
    private List<String> mWords = new ArrayList<String>();
    private List<Integer> mTimeList = new ArrayList<Integer>();

    public void setLurTxt(String lurTxt) {
        this.lurTxt = lurTxt;
        readLRC(lurTxt);
    }

    // 处理歌词文件
    public void readLRC(String path) {
        String[] line = path.split("\n");
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
    }

    public List<String> getWords() {
        return mWords;
    }

    public List<Integer> getTime() {
        return mTimeList;
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
}