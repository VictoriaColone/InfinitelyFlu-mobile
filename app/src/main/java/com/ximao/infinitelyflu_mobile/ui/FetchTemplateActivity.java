package com.ximao.infinitelyflu_mobile.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import com.ximao.infinitelyflu_mobile.utils.Nav;


/**
 * @author ximao
 * @date 2021/7/21
 * 拉取Template Activity
 */
public class FetchTemplateActivity extends AppCompatActivity {

    private static final String TAG = "FetchTemplateActivity";

    private  ProgressBar mLoadingProgressBar;

    private  Button mFetchButton;

    private  Button mOpenPreviewButton;

    private  EditText mTemplateName;

    private  EditText mTemplateVersion;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_template);
        mTemplateName = findViewById(R.id.name);
        mTemplateVersion = findViewById(R.id.version);
        mFetchButton = findViewById(R.id.fetch);
        mOpenPreviewButton = findViewById(R.id.open_preview);
        mLoadingProgressBar = findViewById(R.id.loading);
        initClickListener();
        InfinitelyFluEngine.newInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // yutao todo 创建视图，后续放到文件下载回调中
        InfinitelyFluEngine.getInstance().creatView();
    }

    private void initClickListener() {
        // 拉取if文件
        mFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.from(FetchTemplateActivity.this).toUri("infinitelyflu://com.ximao.infinitelyflu_mobile/IFTemplatePreviewActivity");
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
        // 跳转预览页面
        mOpenPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FetchTemplateActivity.this, IFTemplatePreviewActivity.class));
            }
        });
    }

}