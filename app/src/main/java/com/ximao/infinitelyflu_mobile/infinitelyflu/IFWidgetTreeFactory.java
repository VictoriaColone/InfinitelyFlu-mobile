package com.ximao.infinitelyflu_mobile.infinitelyflu;


import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONObject;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.CompoundButtonStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.CustomViewStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.FrameLayoutStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.GridLayoutStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.GridViewStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.ImageViewStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.LinearLayoutStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.RelativeLayoutStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.ScrollViewStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.SensorLayoutStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.SwitchStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.TextViewStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.ToggleButtonStyler;
import com.ximao.infinitelyflu_mobile.infinitelyflu.style.ViewConstant;
import com.ximao.infinitelyflu_mobile.ui.SensorLayout;
import com.ximao.infinitelyflu_mobile.utils.Util;

import java.lang.reflect.Constructor;
import java.util.Set;


/**
 * @author ximao
 * @date 2021/8/8
 * IF视图树工厂
 */
public class IFWidgetTreeFactory {

    private static final String TAG = "IFWidgetTreeFactory";

    private Context mContext;

    private ViewGroup mContainer;

    public IFWidgetTreeFactory(Context context) {
        mContext = context;
        // 创建容器布局
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mContainer = new FrameLayout(mContext);
        mContainer.setLayoutParams(lp);
    }

    /**
     * 创建View虚拟树
     * @param jsonObject json数据
     * @return 视图虚拟树
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View createViewTree(JSONObject jsonObject) {
        return addViews(mContainer, jsonObject);
    }

    /**
     * 添加视图
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private View addViews(ViewGroup container, JSONObject jsonObject) {

        Util.log(TAG, "Adding children for view " + container.getClass().getSimpleName());
        Set<String> keys = jsonObject.keySet();
        // 用以存放属性
        org.json.JSONObject attributes = new org.json.JSONObject();
        View view = null;
        try {
            for (String key : keys) {
                if (key.contains("-")) {
                    // 控件类型，先创建控件
                    view = createView(ViewConstant.VIEW_REFLECT_MAP.get(key.substring(0, key.indexOf("-"))));
                    if (view instanceof ViewGroup) {
                        container.addView(addViews((ViewGroup)view, jsonObject.getJSONObject(key)));
                    } else {
                        org.json.JSONObject childAttributes =
                                new org.json.JSONObject(jsonObject.getJSONObject(key).toJSONString());
                        childAttributes.put("parent_class", container.getClass());
                        styleView(view, childAttributes);
                        container.addView(view);
                    }
                } else {
                    // 当前控件的属性
                    attributes.put(key, jsonObject.get(key));
                }
            }
            if (attributes.length() != 0) {
                attributes.put("parent_class", container.getClass());
                styleView(container, attributes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return container;
    }

    /**
     * 创建View
     * @param className
     * @return
     * @throws Exception
     */
    private View createView(String className) throws Exception {
        Util.log(TAG, "Creating view " + className);
        Class elementClass = Class.forName(className);
        Constructor constructor = elementClass.getConstructor(Context.class);
        return (View) constructor.newInstance(mContext);
    }

    /**
     * 设置视图属性
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View styleView(View view, org.json.JSONObject attributes) throws Exception {
        Util.log(TAG, "Styling view " + view.getClass().getSimpleName());

        /**
         * 控件属性调用此处要注意：
         * 子类控件初始化应写在父类控件初始化之前，否则无法为特有属性赋值
         */
        if (view instanceof Switch) {
            view = new SwitchStyler(this, mContext).style(view, attributes);
        } else if (view instanceof ToggleButton) {
            view = new ToggleButtonStyler(this, mContext).style(view, attributes);
        } else if (view instanceof CompoundButton) {
            view = new CompoundButtonStyler(this, mContext).style(view, attributes);
        } else if (view instanceof TextView) {
            view = new TextViewStyler(this, mContext).style(view, attributes);
        } else if (view instanceof ImageView) {
            view = new ImageViewStyler(this, mContext).style(view, attributes);
        } else if (view instanceof LinearLayout) {
            view = new LinearLayoutStyler(this, mContext).style(view, attributes);
        } else if (view instanceof RelativeLayout) {
            view = new RelativeLayoutStyler(this, mContext).style(view, attributes);
        } else if (view instanceof GridLayout) {
            view = new GridLayoutStyler(this, mContext).style(view, attributes);
        } else if (view instanceof GridView) {
            view = new GridViewStyler(this, mContext).style(view, attributes);
        } else if (view instanceof ScrollView) {
            view = new ScrollViewStyler(this, mContext).style(view, attributes);
        } else if (view instanceof SensorLayout) {
            view = new SensorLayoutStyler(this, mContext).style(view, attributes);
        } else if (view instanceof FrameLayout) {
            view = new FrameLayoutStyler(this, mContext).style(view, attributes);
        }
        view = new CustomViewStyler(this, mContext).style(view, attributes);
        return view;
    }


}
