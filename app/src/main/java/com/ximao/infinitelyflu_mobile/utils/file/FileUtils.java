package com.ximao.infinitelyflu_mobile.utils.file;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * @author ximao
 * @date 2021/7/21
 * 文件工具类
 */
public class FileUtils {

    private static final String TAG = "FileUtils";

    public static final String METHOD_DOWNLOAD_TEMPLATE = "downloadTemplate";

    public static final String METHOD_GET_TEMPLATE_JSON = "getTemplateJson";

    public static final String NAME = "name";

    public static final String VERSION = "version";

    public static final String CONNECT_SIGN = "&&";

    public static final String QUESTION_SIGN = "?";

    public static final String EQUALS_SIGN = "=";

    // 这个地址会IP需要实时变更，可以用ifconfig en0 查询，也可以用charles查询
    private static final String URL = "http://30.25.158.55:8080/InfinitelyFlu_server_war/template/";

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
                    URL url = new URL(URL + METHOD_DOWNLOAD_TEMPLATE + QUESTION_SIGN +
                            NAME + EQUALS_SIGN + templateName + CONNECT_SIGN + VERSION + EQUALS_SIGN +
                            templateVersion);
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

    /**
     * 获取XML转成的Json
     * @param templateName
     * @param templateVersion
     * @param callback
     */
    public static void getTemplateJsonAsync(String templateName, String templateVersion, Callback callback) {
        Log.d(TAG, "开始获取Json");
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(URL + METHOD_GET_TEMPLATE_JSON + QUESTION_SIGN +
                            NAME + EQUALS_SIGN + templateName + CONNECT_SIGN + VERSION + EQUALS_SIGN +
                            templateVersion);
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder().url(url).build();
                    client.newCall(request).enqueue(callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
