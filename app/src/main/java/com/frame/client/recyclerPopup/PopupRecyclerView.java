package com.frame.client.recyclerPopup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

/**
 * Created by Hongmingwei on 2017/9/13.
 * Email: 648600445@qq.com
 */

public class PopupRecyclerView extends AbsPopupRecyclerView {

    private PopRecyclerTypeAdapter adapter;


    public PopupRecyclerView(Context context, int itemNums) {
        super(context, itemNums);
    }

    @Override
    protected int getItemWidth() {
        return 144;
    }

    @Override
    protected int getItemHeight() {
        return 40;
    }

    @Override
    protected PopRecyclerTypeAdapter getAdapter() {
        if (adapter == null){
            adapter = new PopRecyclerTypeAdapter(mContext, mItems, lastSelectedId);
            this.typeRecyclerView.setAdapter(adapter);
        } else {
            adapter.updateItems(mItems);
            adapter.setLastSelectedId(lastSelectedId);
        }
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected int getOrientation() {
        return LinearLayoutManager.HORIZONTAL;
    }
}
