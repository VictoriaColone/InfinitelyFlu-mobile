package com.ximao.infinitelyflu_mobile.infinitelyflu.style;


import java.util.HashMap;
import java.util.Map;

/**
 * @author ximao
 * 控件对应表
 */
public class ViewConstant {

    public static final String TEXT_VIEW = "TextView";

    public static final String COMPOUND_BUTTON = "CompoundButton";

    public static final String FRAME_LAYOUT = "FrameLayout";

    public static final String LINEAR_LAYOUT = "LinearLayout";

    public static final String RELATIVE_LAYOUT = "RelativeLayout";

    public static final String GRID_LAYOUT = "GridLayout";

    public static final String GRID_VIEW = "GridView";

    public static final String IMAGE_VIEW = "ImageView";

    public static final String SCROLL_VIEW = "ScrollView";

    public static final String SWITCH = "Switch";

    public static final String TOGGLE_BUTTON = "ToggleButton";

    public static final Map<String, String> VIEW_REFLECT_MAP = new HashMap<String, String>(){{
        put(TEXT_VIEW, "android.widget.TextView");
        put(COMPOUND_BUTTON, "android.widget.CompoundButton");
        put(FRAME_LAYOUT, "android.widget.FrameLayout");
        put(LINEAR_LAYOUT, "android.widget.LinearLayout");
        put(RELATIVE_LAYOUT, "android.widget.RelativeLayout");
        put(GRID_LAYOUT, "android.widget.GridLayout");
        put(GRID_VIEW, "android.widget.GridView");
        put(IMAGE_VIEW, "android.widget.ImageView");
        put(SCROLL_VIEW, "android.widget.ScrollView");
        put(SWITCH, "android.widget.Switch");
        put(TOGGLE_BUTTON, "android.widget.ToggleButton");
    }};
}
