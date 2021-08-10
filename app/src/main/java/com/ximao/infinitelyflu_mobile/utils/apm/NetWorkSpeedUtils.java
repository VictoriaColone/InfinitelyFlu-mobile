package com.ximao.infinitelyflu_mobile.utils.apm;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author ximao
 * @date 2021/8/9
 * 网速监控
 */
public class NetWorkSpeedUtils {

    private Context context;

    private Handler mHandler;

    private long lastTotalRxBytes = 0;

    private long lastTimeStamp = 0;

    public NetWorkSpeedUtils(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            showNetSpeed();
        }
    };

    public void startShowNetSpeed() {
        lastTotalRxBytes = getTotalRxBytes();
        lastTimeStamp = System.currentTimeMillis();
        // 每60ms执行一次
        new Timer().schedule(task, 0, 60);
    }

    private long getTotalRxBytes() {
        // 转为KB
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) ==
                TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
    }

    private void showNetSpeed() {
        long nowTotalRxBytes = getTotalRxBytes();
        long nowTimeStamp = System.currentTimeMillis();
        // 毫秒转换
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));
        // 毫秒转换
        long speed2 = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 % (nowTimeStamp - lastTimeStamp));
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        Message msg = mHandler.obtainMessage();
        msg.what = 100;
        msg.obj = getTotalSpeed(speed, speed2);
        // 更新界面
        mHandler.sendMessage(msg);
    }

    private String getTotalSpeed(long speed, long speed2) {
        DecimalFormat showFloatFormat =new DecimalFormat("0.00");
        String totalSpeedStr = "";
        if (speed >= 1024) {
            totalSpeedStr = showFloatFormat.format(speed/1024)+" M/s";
        } else {
            totalSpeedStr =showFloatFormat.format(speed+speed2/1024)+" K/s";
        }
        return totalSpeedStr;
    }

}


