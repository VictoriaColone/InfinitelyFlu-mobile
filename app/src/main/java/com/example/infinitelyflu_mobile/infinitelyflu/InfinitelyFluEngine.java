package com.example.infinitelyflu_mobile.infinitelyflu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author ximao
 * @date 2021/7/31
 * 动态化引擎
 */
public class InfinitelyFluEngine {

    /**
     * TAG
     */
    public static final String TAG = "InfinitelyFluEngine";
    /**
     * 单例
     */
    private volatile static InfinitelyFluEngine mInfinitelyFluEngine;
    /**
     * 根视图View
     */
    public ViewGroup mRootView;
    /**
     * 上下文信息
     */
    public Context mContext;
    /**
     * 视图集
     */
    public ArrayList<View> mViewSet = new ArrayList<>();
    /**
     * 模板字符串
     */
    private JSONObject mJsonData;

    public InfinitelyFluEngine(Context context) {
        this.mContext = context;
    }

    /**
     * 获取根布局
     */
    public View getRootView () {
        return mRootView;
    }

    /**
     * 创建视图
     */
    @SuppressLint("ResourceAsColor")
    public void creatView() {
        TextView parentView = new TextView(mContext);
        parentView.setText("yutao=======test");
        mViewSet.add(parentView);
        TextView childView = new TextView(mContext);
        childView.setText("yutao===test");
//        mRootView.addView(childView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout view = new LinearLayout(mContext);
        view.setLayoutParams(lp);//设置布局参数
        view.setBackgroundColor(android.R.color.black);
        view.setOrientation(LinearLayout.VERTICAL);// 设置子View的Linearlayout// 为垂直方向布局
        view.addView(childView);
        mViewSet.add(view);
    }

    /**
     * 使用前调用View.getchildAt()定位父布局
     */
    @SuppressLint("ResourceAsColor")
    public void insertView(ViewGroup rootView) {
        for (View view : mViewSet) {
            rootView.addView(view);
        }
    }

}
