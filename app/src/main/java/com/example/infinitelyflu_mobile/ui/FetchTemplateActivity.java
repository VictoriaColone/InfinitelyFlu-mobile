package com.example.infinitelyflu_mobile.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.infinitelyflu_mobile.R;
import com.example.infinitelyflu_mobile.utils.FileUtils;


/**
 * @author ximao
 * @date 2021/7/21
 * 拉取Template Activity
 */
public class FetchTemplateActivity extends AppCompatActivity {

    private static final String TAG = "FetchTemplateActivity";

    private  ProgressBar mLoadingProgressBar;

    private  Button mLoginButton;

    private  EditText mTemplateName;

    private  EditText mTemplateVersion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_template);

        mTemplateName = findViewById(R.id.name);
        mTemplateVersion = findViewById(R.id.version);
        mLoginButton = findViewById(R.id.fetch);
        mLoadingProgressBar = findViewById(R.id.loading);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                String templateName = mTemplateName.getText().toString();
                String templateVersion = mTemplateVersion.getText().toString();
                if (!TextUtils.isEmpty(templateName) && !TextUtils.isEmpty(templateVersion)) {
                    Log.d(TAG, templateName + "   " + templateVersion);
                    // yutao todo 调用后端下载接口
                    FileUtils.downloadFileAsync(templateName, templateVersion, getApplicationContext(), new DownloadListener() {
                        @Override
                        public void callback() {
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

    }

    public interface DownloadListener {
        void callback();
    }

}