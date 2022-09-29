package com.ximao.infinitelyflu_mobile;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.multidex.MultiDexApplication;
import com.didichuxing.doraemonkit.DoKit;
import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * @author ximao
 * @date 2021/8/11
 * Application类
 */
public class IFApplication extends MultiDexApplication {

    /**
     * Application对象
     */
    private static IFApplication mInstance;

    /**
     * 栈顶Activity对象
     */
    private Activity mTopActivity;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initGlobelActivity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 初始化Fresco
                Fresco.initialize(IFApplication.this);
                // 初始化DoKit
                new DoKit.Builder(IFApplication.this).disableUpload().productId("").build();
                DoKit.hide();
            }
        }).start();

    }

    /**
     * 监听Activity生命周期，onResume后绑定TopActivity
     */
    private void initGlobelActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("onActivityCreated===", mTopActivity + "");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("onActivityDestroyed===", mTopActivity + "");
            }

            /** Unused implementation **/
            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("onActivityStarted===", mTopActivity + "");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mTopActivity = activity;
                Log.e("onActivityResumed===", mTopActivity + "");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e("onActivityPaused===", mTopActivity + "");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e("onActivityStopped===", mTopActivity + "");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    /**
     * 获取实例
     * @return
     */
    public static IFApplication getInstance() {
        return mInstance;
    }

    /**
     * 公开方法，外部可通过 MyApplication.getInstance().getCurrentActivity() 获取到当前最上层的activity
     */
    public Activity getCurrentActivity() {
        return mTopActivity;
    }

}
