package com.ximao.infinitelyflu_mobile;


import androidx.multidex.MultiDexApplication;
import com.didichuxing.doraemonkit.DoKit;
import com.facebook.drawee.backends.pipeline.Fresco;


/**
 * @author ximao
 * @date 2021/8/11
 * Application类
 */
public class IFApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Fresco
        Fresco.initialize(this);
        // 初始化DoKit
        new DoKit.Builder(this).disableUpload().productId("").build();
//        DoKit.hide();
    }

}
