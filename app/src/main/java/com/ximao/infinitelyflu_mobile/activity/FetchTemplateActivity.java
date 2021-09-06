package com.ximao.infinitelyflu_mobile.activity;


import static com.alibaba.fastjson.JSON.parseObject;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.ximao.infinitelyflu_mobile.R;
import com.ximao.infinitelyflu_mobile.infinitelyflu.InfinitelyFluEngine;
import com.ximao.infinitelyflu_mobile.utils.apm.FloatViewService;
import com.ximao.infinitelyflu_mobile.utils.file.DownloadListener;
import com.ximao.infinitelyflu_mobile.utils.file.FileUtils;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * @author ximao
 * @date 2021/7/21
 * 拉取Template Activity
 */
public class FetchTemplateActivity extends AppCompatActivity {

    private static final String TAG = "FetchTemplateActivity";

    private  ProgressBar mLoadingProgressBar;

    private  Button mFetchXmlButton;

    private  Button mFetchJsonButton;

    private  Button mOpenPreviewButton;

    private  EditText mTemplateName;

    private  EditText mTemplateVersion;

    private Intent serviceIntent;

    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onFailure: Get Json Failed");
                    mLoadingProgressBar.setVisibility(View.GONE);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = "";
            // 网络线程
            try {
                responseData = response.body().string();
                if (TextUtils.isEmpty(responseData)) {
                    return;
                }
                Log.d(TAG, "onResponse:" + responseData);
                // OrderedField保证有序性
                JSONObject jsonObject = parseObject(responseData, Feature.OrderedField);
                if (jsonObject == null) {
                    return;
                }
                // Json获取后，创建视图
                InfinitelyFluEngine.getInstance().creatView(jsonObject);
                // yutao todo json缓存策略
                InfinitelyFluEngine.getInstance().cacheTemplateJson();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 主线程
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mLoadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "Get Json Success", Toast.LENGTH_LONG).show();
                }
            });
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_template);
        mTemplateName = findViewById(R.id.name);
        mTemplateVersion = findViewById(R.id.version);
        mFetchXmlButton = findViewById(R.id.fetch_xml);
        mFetchJsonButton = findViewById(R.id.fetch_json);
        mOpenPreviewButton = findViewById(R.id.open_preview);
        mLoadingProgressBar = findViewById(R.id.loading);
        initClickListener();
        InfinitelyFluEngine.newInstance(this);
        showFloatView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭悬浮控件
        stopService(serviceIntent);
    }

    private void initClickListener() {
        // 拉取xml文件
        mFetchXmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                String templateName = mTemplateName.getText().toString();
                String templateVersion = mTemplateVersion.getText().toString();
                if (!TextUtils.isEmpty(templateName) && !TextUtils.isEmpty(templateVersion)) {
                    Log.d(TAG, templateName + "   " + templateVersion);
                    // 文件下载
                    FileUtils.downloadFileAsync(templateName, templateVersion, getApplicationContext(),
                            new DownloadListener() {
                        @Override
                        public void downloadCallback() {
                            mLoadingProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(), "Download Success", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    mLoadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "Input Expection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 获取Json
        mFetchJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                String templateName = mTemplateName.getText().toString();
                String templateVersion = mTemplateVersion.getText().toString();
                if (!TextUtils.isEmpty(templateName) && !TextUtils.isEmpty(templateVersion)) {
                    Log.d(TAG, templateName + "   " + templateVersion);
                    // 获取json
                    FileUtils.getTemplateJsonAsync(templateName, templateVersion, mCallback);
                } else {
                    Log.d(TAG, templateName + "   " + templateVersion);
                    mLoadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), "Input Expection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 跳转预览页面
        mOpenPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FetchTemplateActivity.this, IFTemplatePreviewActivity.class));
            }
        });
    }

    /**
     * 开启悬浮控件
     */
    private void showFloatView() {
        //Android 6.0之后的悬浮窗动态申请,覆盖显示权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !Settings.canDrawOverlays(getApplicationContext())) {
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +
                    FetchTemplateActivity.this.getPackageName())));
        }
        serviceIntent= new Intent(FetchTemplateActivity.this, FloatViewService.class);
        startService(serviceIntent);
    }

}