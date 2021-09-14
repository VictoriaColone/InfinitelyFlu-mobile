package com.ximao.infinitelyflu_mobile.infinitelyflu;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.alibaba.fastjson.JSONObject;
import com.ximao.infinitelyflu_mobile.utils.file.FileUtils;
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
     * 主线程Handler
     */
    private final Handler mHandler = new Handler(Looper.getMainLooper());
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
     * 创建视图
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void creatView(JSONObject jsonObject) {
        IFWidgetTreeFactory ifWidgetTreeFactory = new IFWidgetTreeFactory(mContext);
        View templateView = ifWidgetTreeFactory.createViewTree(jsonObject);
        if (templateView == null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "JsonParse Failed", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        mViewSet.add(templateView);
    }

    /**
     * 使用前调用View.getchildAt()定位父布局
     */
    public void insertView() {
        for (View view : mViewSet) {
            if (view.getParent() != null) {
                ((ViewGroup)view.getParent()).removeView(view);
            }
            mRootView.addView(view);
        }
    }

    /**
     * 清理引擎数据
     */
    public void clearIFData() {
        if (InfinitelyFluEngine.getInstance() != null) {
            mViewSet = new ArrayList<>();
            mRootView = null;
        }
    }

    /**
     * 缓存TemplateJson
     * @param templateName
     * @param templateVersion
     * @param responseData
     */
    public void cacheTemplateJson(String templateName, String templateVersion, String responseData) {
        FileUtils.cacheTemplateJson(templateName, templateVersion, responseData);
    }

}
