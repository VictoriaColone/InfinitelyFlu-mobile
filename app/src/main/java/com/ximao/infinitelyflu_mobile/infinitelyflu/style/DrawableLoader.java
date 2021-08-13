package com.ximao.infinitelyflu_mobile.infinitelyflu.style;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ximao.infinitelyflu_mobile.utils.Util;
import com.ximao.infinitelyflu_mobile.utils.network.ApiResponse;
import com.ximao.infinitelyflu_mobile.utils.network.ImageDownload;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.nio.charset.StandardCharsets;



final class DrawableLoader {

    private static final String TAG = "DrawableLoader";

    private int requestCode;

    private boolean cache, used;
    private OnDrawableLoadedListener listener;
    private Context context;

    DrawableLoader(JSONObject attributes, OnDrawableLoadedListener listener, Context context) throws JSONException {
        this.listener = listener;
        this.context = context;

        if(attributes.has("cache")) {
            cache = attributes.getBoolean("cache");
        }
    }

    void load(String src, int requestCode) {
        if(used) {
            Util.log(TAG, "DrawableLoader cannot be reused. Please construct new one to load another drawable.");
            return;
        }

        this.used = true;
        this.requestCode = requestCode;

        if(cache) {
            loadImageFromCache(src);
        }else {
            loadImageFromServer(src);
        }
    }

    private void loadImageFromServer(final String src) {
        final ImageDownload request = new ImageDownload(src, context);
        request.addHandler(new ApiResponse() {
            @Override
            public void onSuccess(String response) {
                if(cache) {
                    try {
                        FileUtils.writeByteArrayToFile(createFile(src), request.getBytes());
                    }catch(Exception e) {
                        Util.log("Image error", "Loading image from server and caching it produced the following error: " + e.getMessage());
                    }
                }

                listener.onDrawableLoaded(request.getDrawable(), requestCode);
            }

            @Override
            public void onError(String message) {
                Util.log("Image error", "Loading image from server produced the following error: " + message);

                loadImageFromCache(src);
            }
        });
        request.start();
    }

    private void loadImageFromCache(String src) {
        byte[] bytes;

        try {
            bytes = FileUtils.readFileToByteArray(createFile(src));
        }catch(Exception e) {
            Util.log("Image error", "Loading image from cache produced the following error: " + e.getMessage());

            loadImageFromServer(src);

            return;
        }

        if(bytes != null && bytes.length > 0) {
            listener.onDrawableLoaded(new BitmapDrawable(context.getResources(),
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length)), requestCode);
        }else {
            loadImageFromServer(src);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private File createFile(String src) throws Exception {
        String path = context.getFilesDir() + File.separator + Util.hash(src.getBytes(StandardCharsets.UTF_8));

        Util.log(TAG, "Creating file at path: " + path);

        File file = new File(path);
        file.createNewFile();

        return file;
    }
}