package com.example.infinitelyflu_mobile.ui.adapter;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.infinitelyflu_mobile.ui.viewholder.PreviewViewHolder;

/**
 * @author ximao
 * @date 2021/7/28
 * 模板预览页面配套Adapter
 */
public class IFTemplatePreviewAdapter extends RecyclerView.Adapter<PreviewViewHolder> {


    private RecyclerView recyclerView;

//    DinamicXEngineRouter engineRouter;

    Context context;

    public IFTemplatePreviewAdapter(Context context, RecyclerView rv) {
        recyclerView = rv;
        this.context = context;
    }

    @NonNull
    @Override
    public PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView view = new TextView(context);
        PreviewViewHolder holder = new PreviewViewHolder(view);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null) {
            lp = recyclerView.getLayoutManager().generateLayoutParams(lp);
        } else {
            lp = recyclerView.getLayoutManager().generateDefaultLayoutParams();
        }
        holder.itemView.setLayoutParams(lp);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
