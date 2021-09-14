package com.ximao.infinitelyflu_mobile.utils.file;

import static android.content.Context.MODE_APPEND;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.ximao.infinitelyflu_mobile.IFApplication;
import com.ximao.infinitelyflu_mobile.activity.ifInterface.GetJsonFromCacheCallback;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private static final String URL = "http://30.7.243.4:8080/InfinitelyFlu_server_war/template/";

    /**
     * 下载文件
     * @param templateName
     * @param templateVersion
     * @param callback
     */
    public static void downloadFileAsync(String templateName, String templateVersion,
                                         DownloadListener callback) {
        Log.d(TAG, "开始下载");
        /**
         * Android移动端与Tomcat建立连接：
         * 1. 在同一无墙网络环境下
         * 2. 设置为http请求，但要在AndroidManifest里Application标签下注册android:usesCleartextTraffic="true"
         *    越过网络安全检查
         * 3. ip地址不能用localhost，Mac系统通过终端命令 ifconfig en0 获取ip地址
         */
        Context context = IFApplication.getInstance();
        if (context == null) {
            return;
        }
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getTemplateJsonAsync(String templateName, String templateVersion, Callback callback,
                                            GetJsonFromCacheCallback cacheCallback) {
        Log.d(TAG, "开始获取Json");
        // 比对模板名&&版本号，如果有缓存，从缓存中获取，若无，后端拉取
        if (TextUtils.isEmpty(templateName) || TextUtils.isEmpty(templateVersion)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = FileUtils.read(templateName + templateVersion);
                if (!TextUtils.isEmpty(response)) {
                    cacheCallback.onResponse(response);
                } else {
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
            }
        }).start();
    }

    /**
     * 读取Json文件
     * @param fileName 文件名 格式：模板名+版本号
     */
    public static String read(String fileName) {
        Context context = IFApplication.getInstance();
        if (context == null) {
            return null;
        }
        try {
            FileInputStream inStream = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder();
            while ((hasRead = inStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, hasRead));
            }
            inStream.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缓存Json文件
     * @param templateName
     * @param templateVersion
     * @param responseData
     */
    public static void cacheTemplateJson(String templateName, String templateVersion,
                                         String responseData) {
        Context context = IFApplication.getInstance();
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(templateName) || TextUtils.isEmpty(templateVersion)
                || TextUtils.isEmpty(responseData)) {
            return;
        }
        String fileName = templateName + templateVersion;
        try {
            FileOutputStream fos = context.openFileOutput(fileName, MODE_APPEND);
            fos.write(responseData.getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "cacheTemplateJson: Failed");
            e.printStackTrace();
        }
    }

}
