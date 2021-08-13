package com.ximao.infinitelyflu_mobile.infinitelyflu.style;

import android.content.Context;
import android.view.View;
import android.widget.ToggleButton;
import com.ximao.infinitelyflu_mobile.infinitelyflu.IFWidgetTreeFactory;
import org.json.JSONObject;



public final class ToggleButtonStyler extends CompoundButtonStyler {

    public ToggleButtonStyler(IFWidgetTreeFactory factory, Context context) {
        super(factory, context);
    }

    @Override
    public View style(View view, JSONObject attributes) throws Exception {
        super.style(view, attributes);

        ToggleButton toggleButton = (ToggleButton) view;

        if(attributes.has("textOn")) {
            toggleButton.setTextOn(attributes.getString("textOn"));
        }

        if(attributes.has("textOff")) {
            toggleButton.setTextOn(attributes.getString("textOff"));
        }

        return toggleButton;
    }
}