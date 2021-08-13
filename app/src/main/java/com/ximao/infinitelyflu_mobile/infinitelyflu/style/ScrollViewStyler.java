package com.ximao.infinitelyflu_mobile.infinitelyflu.style;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;
import com.ximao.infinitelyflu_mobile.infinitelyflu.IFWidgetTreeFactory;
import org.json.JSONObject;



public final class ScrollViewStyler extends FrameLayoutStyler {

    public ScrollViewStyler(IFWidgetTreeFactory factory, Context context) {
        super(factory, context);
    }

    @Override
    public View style(View view, JSONObject attributes) throws Exception {
        super.style(view, attributes);

        ScrollView scrollView = (ScrollView) view;

        if(attributes.has("fillViewport")) {
            scrollView.setFillViewport(attributes.getBoolean("fillViewport"));
        }

        if(attributes.has("smoothScrollingEnabled")) {
            scrollView.setSmoothScrollingEnabled(attributes.getBoolean("smoothScrollingEnabled"));
        }

        if(attributes.has("overScrollMode")) {
            String mode = attributes.getString("overScrollMode");

            if(mode.equalsIgnoreCase("always")) {
                scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_ALWAYS);
            }else if(mode.equalsIgnoreCase("never")) {
                scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
            }else if(mode.equalsIgnoreCase("if_content_scrolls")) {
                scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_IF_CONTENT_SCROLLS);
            }
        }

        return scrollView;
    }
}