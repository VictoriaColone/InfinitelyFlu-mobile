package com.ximao.infinitelyflu_mobile.infinitelyflu.style;

import android.content.Context;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.ximao.infinitelyflu_mobile.infinitelyflu.IFWidgetTreeFactory;
import org.json.JSONArray;
import org.json.JSONObject;



public final class CustomViewStyler extends ViewStyler {

    public CustomViewStyler(IFWidgetTreeFactory factory, Context context) {
        super(factory, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View style(View view, JSONObject attributes) throws Exception {
        super.style(view, attributes);

        if(attributes.has("custom")) {
            JSONArray custom = attributes.getJSONArray("custom");

            for(int i = 0; i < custom.length(); i++) {
                JSONObject method = custom.getJSONObject(i);

                new MethodInvoker.Builder(view, method, context)
                        .build()
                        .invoke();
            }
        }

        return view;
    }
}