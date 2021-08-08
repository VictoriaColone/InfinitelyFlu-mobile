package com.ximao.infinitelyflu_mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.ximao.infinitelyflu_mobile.R;
import com.ximao.infinitelyflu_mobile.infinitelyflu.InfinitelyFluEngine;
import com.ximao.infinitelyflu_mobile.utils.DownloadListener;
import com.ximao.infinitelyflu_mobile.utils.FileUtils;

import org.json.JSONObject;

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

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            JSONObject jsonObject = null;
            String responseData = "";
            // 网络线程
            try {
                if (TextUtils.isEmpty(response.body().string())) {
                    return;
                }
                Log.d(TAG, "onResponse:" + response.body().string());
                responseData = response.body().string();
                jsonObject = new JSONObject(responseData);
                if (jsonObject == null) {
                    return;
                }
                // yutao todo 保存json到本地
                InfinitelyFluEngine.getInstance().cacheTemplateJson();
                // Json获取后，创建视图
                InfinitelyFluEngine.getInstance().creatView(jsonObject);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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

}