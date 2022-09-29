package com.ximao.infinitelyflu_mobile.utils.apm;


import android.os.Handler;
import android.os.Message;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author ximao
 * @date 2021/8/10
 * 内存检测工具
 */
public class MemoryUtils {

    private Handler mHandler;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            showMemoryUsed();
        }
    };

    public MemoryUtils(Handler handler) {
        this.mHandler = handler;
    }

    public void startShowMemoryUsage() {
        // 每60ms执行一次
        new Timer().schedule(task, 0, 60);
    }

    /**
     * 展示内存使用情况
     */
    private void showMemoryUsed() {
        Message msg = mHandler.obtainMessage();
        msg.what = 300;
        MemoryInfo memoryInfo = getMemoryInfo();
        msg.obj = memoryInfo.toString();
        mHandler.sendMessage(msg);
    }

    private MemoryInfo getMemoryInfo() {
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setMaxMemory((float) (Runtime.getRuntime().maxMemory() * 1.0/ (1024 * 1024)));
        memoryInfo.setTotalMemory((float) (Runtime.getRuntime().totalMemory() * 1.0/ (1024 * 1024)));
        memoryInfo.setFreeMemory((float) (Runtime.getRuntime().freeMemory() * 1.0/ (1024 * 1024)));
        return  memoryInfo;
    }

}
