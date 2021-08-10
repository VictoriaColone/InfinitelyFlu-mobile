package com.ximao.infinitelyflu_mobile.utils.apm;


import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author ximao
 * @date 2021/8/9
 * Cpu检测工具
 */
public class CpuMonitorUtils {

    private Handler mHandler;

    private RandomAccessFile mProcStatFile;

    private RandomAccessFile mAppStatFile;

    private Long mLastCpuTime;

    private Long mLastAppCpuTime;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            showCupRate();
        }
    };

    public CpuMonitorUtils(Handler handler) {
        this.mHandler = handler;
    }

    public void startShowCpuRate() {
        // 每60ms执行一次
        new Timer().schedule(task, 0, 60);
    }

    /**
     * 展示Cpu使用率
     */
    private void showCupRate() {
        Message msg = mHandler.obtainMessage();
        msg.what = 200;
        float result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = getCpuRateForO();
        } else {
            result = getCpuRate();
        }

        msg.obj = result + " %";
        mHandler.sendMessage(msg);
    }

    /**
     * 获取CPU占用率 Android 8.0以下
     */
    private float getCpuRate() {
        long cpuTime;
        long appTime;
        float value = 0.0f;
        try {
            if (mProcStatFile == null || mAppStatFile == null) {
                mProcStatFile = new RandomAccessFile("/proc/stat", "r");
                mAppStatFile = new RandomAccessFile("/proc/" + android.os.Process.myPid() + "/stat", "r");
            } else {
                mProcStatFile.seek(0L);
                mAppStatFile.seek(0L);
            }
            String procStatString = mProcStatFile.readLine();
            String appStatString = mAppStatFile.readLine();
            String procStats[] = procStatString.split(" ");
            String appStats[] = appStatString.split(" ");
            cpuTime = Long.parseLong(procStats[2]) + Long.parseLong(procStats[3])
                    + Long.parseLong(procStats[4]) + Long.parseLong(procStats[5])
                    + Long.parseLong(procStats[6]) + Long.parseLong(procStats[7])
                    + Long.parseLong(procStats[8]);
            appTime = Long.parseLong(appStats[13]) + Long.parseLong(appStats[14]);
            if (mLastCpuTime == null && mLastAppCpuTime == null) {
                mLastCpuTime = cpuTime;
                mLastAppCpuTime = appTime;
                return value;
            }
            value = ((float) (appTime - mLastAppCpuTime) / (float) (cpuTime - mLastCpuTime)) * 100f;
            mLastCpuTime = cpuTime;
            mLastAppCpuTime = appTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取CPU占用率 Android 8.0以上
     */
    private float getCpuRateForO() {
        java.lang.Process process = null;
        try {
            process = Runtime.getRuntime().exec("top -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int cpuIndex = -1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (TextUtils.isEmpty(line)) {
                    continue;
                }
                int tempIndex = getCPUIndex(line);
                if (tempIndex != -1) {
                    cpuIndex = tempIndex;
                    continue;
                }
                if (line.startsWith(String.valueOf(Process.myPid()))) {
                    if (cpuIndex == -1) {
                        continue;
                    }
                    String[] param = line.split("\\s+");
                    if (param.length <= cpuIndex) {
                        continue;
                    }
                    String cpu = param[cpuIndex];
                    if (cpu.endsWith("%")) {
                        cpu = cpu.substring(0, cpu.lastIndexOf("%"));
                    }
                    float rate = Float.parseFloat(cpu) / Runtime.getRuntime().availableProcessors();
                    return rate;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return 0;
    }

    /**
     * 获取CPU 指示数
     * @param line
     * @return
     */
    private int getCPUIndex(String line) {
        if (line.contains("CPU")) {
            String[] titles = line.split("\\s+");
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].contains("CPU")) {
                    return i;
                }
            }
        }
        return -1;
    }

}
