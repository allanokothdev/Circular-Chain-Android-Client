package com.circularchained.android.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.circularchained.android.R;

public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

    private final Context mContext;
    private final int headerOffset;
    private final boolean sticky;
    private final SectionCallback sectionCallback;
    private View headerView;
    private TextView textView;

    public RecyclerItemDecoration(Context mContext, int headerHeight, boolean isSticky, SectionCallback sectionCallback) {
        this.mContext = mContext;
        headerOffset = headerHeight;
        sticky = isSticky;
        this.sectionCallback = sectionCallback;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if(sectionCallback.isSection(pos)) {
            outRect.top = headerOffset;
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if(headerView==null) {
            headerView = inflateHeader(parent);
            textView = headerView.findViewById(R.id.textView);
            fixLayoutSize(headerView,parent);
        }

        String prevTitle = "";
        for(int i=0;i<parent.getChildCount();i++) {
            View child = parent.getChildAt(i);
            int childPos = parent.getChildAdapterPosition(child);


            String title = sectionCallback.getSectionHeaderName(childPos);
            textView.setText(title);
            if(!prevTitle.equalsIgnoreCase(title) || sectionCallback.isSection(childPos)) {
                drawHeader(c,child,headerView);
                prevTitle = title;
            }
        }
    }

    private void drawHeader(Canvas c, View child, View headerView) {
        c.save();
        if(sticky) {
            c.translate(0,Math.max(0,child.getTop()-headerView.getHeight()));
        }else {
            c.translate(0,child.getTop()-headerView.getHeight());
        }
        headerView.draw(c);
        c.restore();
    }

    public void fixLayoutSize(View view, ViewGroup viewGroup) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.getWidth(),View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.getHeight(),View.MeasureSpec.UNSPECIFIED);
        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,viewGroup.getPaddingLeft()+viewGroup.getPaddingRight(),view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,viewGroup.getPaddingTop()+viewGroup.getPaddingBottom(),view.getLayoutParams().height);
        view.measure(childWidth,childHeight);
        view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
    }

    private View inflateHeader(RecyclerView recyclerView) {


        return LayoutInflater.from(mContext).inflate(R.layout.header_item,recyclerView,false);
    }

    public interface SectionCallback{
        boolean isSection(int pos);
        String getSectionHeaderName(int pos);
    }
}
