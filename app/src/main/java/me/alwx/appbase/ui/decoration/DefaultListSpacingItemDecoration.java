package me.alwx.appbase.ui.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * sets spacing for grid RecyclerView
 *
 * @author alwx (http://alwx.me)
 * @version 1.0
 */
public class DefaultListSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpacing;

    public DefaultListSpacingItemDecoration(int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        int childIndex = parent.getChildLayoutPosition(view);
        if (childIndex == 0) {
            outRect.top = mSpacing;
        }
    }
}