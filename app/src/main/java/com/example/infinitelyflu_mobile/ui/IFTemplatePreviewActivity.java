package com.example.infinitelyflu_mobile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.infinitelyflu_mobile.R;
import com.example.infinitelyflu_mobile.ui.adapter.IFTemplatePreviewAdapter;


/**
 * @author ximao
 * @date 2021/7/28
 * 预览TemplatePreviewActivity
 */
public class IFTemplatePreviewActivity extends AppCompatActivity {

    private RecyclerView rvMainContainer;

    private IFTemplatePreviewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        rvMainContainer = findViewById(R.id.rv_main_container);
        adapter = new IFTemplatePreviewAdapter(this, rvMainContainer);
        rvMainContainer.setAdapter(adapter);
    }
}