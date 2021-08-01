package com.example.infinitelyflu_mobile.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.example.infinitelyflu_mobile.ui.FetchTemplateActivity;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author ximao
 * @date 2021/7/21
 * 文件工具类
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    /**
     * 下载文件
     * @param templateName
     * @param templateVersion
     * @param callback
     */
    public static void downloadFileAsync(String templateName, String templateVersion, Context context,
                                         DownloadListener callback) {
        Log.d(TAG, "开始下载");

        /**
         * Android移动端与Tomcat建立连接：
         * 1. 在同一无墙网络环境下
         * 2. 设置为http请求，但要在AndroidManifest里Application标签下注册android:usesCleartextTraffic="true"
         *    越过网络安全检查
         * 3. ip地址不能用localhost，Mac系统通过终端命令 ifconfig en0 获取ip地址
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://172.20.10.9:8080/InfinitelyFlu_server_war/template/downloadTemplate?name="
                            + templateName +"&&version=" + templateVersion);
                    InputStream is = url.openStream();
                    //打开手机对应的输出流,输出到文件中
                    OutputStream os = context.openFileOutput(templateName + "_" + templateVersion
                            + ".xml", Context.MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    //从输入六中读取数据,读到缓冲区中
                    while((len = is.read(buffer)) > 0)
                    {
                        os.write(buffer,0,len);
                    }
                    //关闭输入输出流
                    is.close();
                    os.close();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d(TAG, "run on mainthread");
                                callback.downloadCallback();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "downloadFileAsync:" + e);
                }
            }
        }).start();
    }

}
