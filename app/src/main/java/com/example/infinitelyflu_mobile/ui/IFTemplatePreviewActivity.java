package com.example.infinitelyflu_mobile.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import com.example.infinitelyflu_mobile.R;
import com.example.infinitelyflu_mobile.infinitelyflu.InfinitelyFluEngine;


/**
 * @author ximao
 * @date 2021/7/28
 * 预览TemplatePreviewActivity
 */
public class IFTemplatePreviewActivity extends AppCompatActivity {


    /**
     * TAG
     */
    private static final String TAG = "IFTemplatePreviewActivity";
    /**
     * 视图容器
     */
    private ViewGroup mContainer;
    /**
     * 持有引擎对象
     * @param
     */
    private InfinitelyFluEngine mIFEngine = InfinitelyFluEngine.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mContainer = findViewById(R.id.rv_main_container);
        mContainer.removeAllViews();
        // 将RootView传入到IF引擎中
        mIFEngine.setRootView(mContainer);
        // 插入视图
        mIFEngine.insertView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InfinitelyFluEngine.getInstance().clearIFData();
    }
}