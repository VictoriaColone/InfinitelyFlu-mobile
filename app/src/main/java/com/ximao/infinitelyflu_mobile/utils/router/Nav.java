package com.ximao.infinitelyflu_mobile.utils.router;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


/**
 * @author ximao
 * @date 2021/08/06
 * 唤端工具
 * 使用demo
 * Nav.from(FetchTemplateActivity.this).toUri("infinitelyflu://com.ximao.infinitelyflu_mobile/IFTemplatePreviewActivity");
 */
public class Nav {

    /**
     * 上下文
     */
    private final Context mContext;
    /**
     * 唤起参数
     */
    private Bundle mOptions;

    private Nav(Context context) {
        this.mContext = context;
        this.mOptions = null;
    }

    /**
     * 配置参数
     */
    public Nav withOptions(Bundle options) {
        if (this.mOptions != null) {
            this.mOptions.putAll(options);
        } else {
            this.mOptions = options;
        }

        return this;
    }

    /**
     * 设置context
     */
    public static Nav from(Activity activity) {
        return new Nav(activity);
    }

    /**
     * 无回调跳转uri
     */
    public boolean toUri(String uri) {
        return this.toUri(uri, (Nav.CallBack)null);
    }

    /**
     * 带回调跳转uri
     */
    public boolean toUri(String uri, Nav.CallBack callBack) {
        if (null == uri) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri));
        this.mContext.startActivity(intent);
        return true;
    }

    /**
     * 回调接口
     */
    public interface CallBack {
        void onResult(boolean var1, Intent var2);
    }

}
