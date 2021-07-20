package com.example.infinitelyflu_mobile.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void downloadFileAsync(Callback callback) {
        Log.d(TAG, "开始下载");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/InfinitelyFlu_server_war/template/downloadTemplate?name=test&&version=0.1")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "下载失败" + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    //回调的方法执行在子线程。
                    Log.d(TAG, "onResponse: 获取数据成功了");
                    try{
                        //从服务器得到输入流对象
                        InputStream is = response.body().byteStream();
                        File dir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
                        if (!dir.exists()){
                            dir.mkdirs();
                        }
                        //根据目录和文件名得到file对象
                        File file = new File(dir, response.header("Disposition", "main_android.xml"));
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buf = new byte[1024*8];
                        int len = 0;
                        while ((len = is.read(buf)) != -1){
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d(TAG, "run: mainthread");
                                    callback.onResponse(call, response);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
