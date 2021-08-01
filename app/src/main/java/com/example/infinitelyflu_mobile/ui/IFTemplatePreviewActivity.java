package com.example.infinitelyflu_mobile.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    public static final String TAG = "IFTemplatePreviewActivity";
    /**
     * 视图容器
     */
    private ViewGroup mContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mContainer = findViewById(R.id.rv_main_container);
        insertView(mContainer);
        InfinitelyFluEngine infinitelyFluEngine = new InfinitelyFluEngine(this);
        infinitelyFluEngine.creatView();
        infinitelyFluEngine.insertView(mContainer);
    }

    /**
     * 插入视图
     */
    private void insertView(ViewGroup rootView) {
    }

}