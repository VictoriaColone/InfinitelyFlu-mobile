package com.ximao.infinitelyflu_mobile.utils.router;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;


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
    private Context mContext;
    /**
     * 唤起参数
     */
    private Bundle mOptions;

    /**
     * 空参构造，提供给点击事件调用
     */
    public Nav() {}

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

    /*************************************  IF模板静态调用点击事件  *************************************/
    /**
     * <onClick
     *      class="com.ximao.infinitelyflu_mobile.utils.router.Nav"
     *      method="longToast">
     *      <parameters
     *          type="android.content.Context"/>
     *      <parameters
     *          type="java.lang.String"
     *          value="hello, InfinitelyFlu~~~"/>
     * </onClick>
     */

    /**
     * 长吐司
     */
    public static void longToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    /**
     * 短吐司
     */
    public static void shortToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

}
