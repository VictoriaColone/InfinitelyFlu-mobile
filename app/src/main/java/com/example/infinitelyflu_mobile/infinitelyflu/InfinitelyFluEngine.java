package com.example.infinitelyflu_mobile.infinitelyflu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String TAG = "InfinitelyFluEngine";
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
    private JSONObject mJsonData = new JSONObject();

    /**
     * 构造函数
     * @param context
     */
    private InfinitelyFluEngine(Context context) {
        this.mContext = context;
    }

    /**
     * 单例构造
     * @param context  上下文
     */
    public static void newInstance(Context context) {
        if (mInfinitelyFluEngine == null) {
            synchronized (InfinitelyFluEngine.class) {
                if (mInfinitelyFluEngine == null) {
                    mInfinitelyFluEngine = new InfinitelyFluEngine(context);
                }
            }
        }
    }

    /**
     * 获取单例
     * @return mInfinitelyFluEngine
     */
    public static InfinitelyFluEngine getInstance() {
        return mInfinitelyFluEngine;
    }

    /**
     * 设置根视图
     * @param rootView
     */
    public void setRootView(ViewGroup rootView) {
        rootView.removeAllViews();
        this.mRootView = rootView;
    }

    /**
     * 获取根视图
     * @return rootView
     */
    public ViewGroup getRootView() {
        return mRootView;
    }

    /**
     * 设置数据
     * @param data
     */
    public void setJsonData(JSONObject data) {
        this.mJsonData = mJsonData;
    }


    /**
     * 获取数据
     * @return json
     */
    public JSONObject getJsonData() {
        return mJsonData;
    }

    /**
     * 创建视图
     */
    @SuppressLint("ResourceAsColor")
    public void creatView() {
        TextView parentView = new TextView(mContext);
        parentView.setText("yutao=======test");
        mViewSet.add(parentView);
//        TextView childView = new TextView(mContext);
//        childView.setText("yutao===test");
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout view = new LinearLayout(mContext);
//
//
//        view.setBackgroundColor(R.color.design_dark_default_color_background);
//        view.setLayoutParams(lp);//设置布局参数
//        view.setOrientation(LinearLayout.VERTICAL);// 设置子View的Linearlayout// 为垂直方向布局
//        view.addView(childView);
//        mViewSet.add(view);
    }

    /**
     * 使用前调用View.getchildAt()定位父布局
     */
    public void insertView() {
        // yutao todo 添加view前必须进行一次remove操作
        for (View view : mViewSet) {
            mRootView.addView(view);
        }
    }

    /**
     * 清理引擎数据
     */
    public void clearIFData() {
        if (InfinitelyFluEngine.getInstance() != null) {
            mViewSet = new ArrayList<>();
            mJsonData = new JSONObject();
            mRootView = null;
        }
    }

}
