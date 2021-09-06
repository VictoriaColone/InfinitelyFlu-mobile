package com.ximao.infinitelyflu_mobile.infinitelyflu.style;

import android.content.Context;
import android.view.View;
import com.ximao.infinitelyflu_mobile.infinitelyflu.IFWidgetTreeFactory;
import com.ximao.infinitelyflu_mobile.ui.SensorLayout;
import org.json.JSONObject;


/**
 * 裸眼3D控件
 * @author ximao
 */
public class SensorLayoutStyler extends FrameLayoutStyler{

    public SensorLayoutStyler(IFWidgetTreeFactory factory, Context context) {
        super(factory, context);
    }

    @Override
    public View style(View view, JSONObject attributes) throws Exception {
        super.style(view, attributes);
        SensorLayout sensorLayout = (SensorLayout) view;
        if (attributes.has("isBackground")) {
            sensorLayout.setIsBackground(attributes.getBoolean("isBackground"));
        }
        return sensorLayout;
    }

}
