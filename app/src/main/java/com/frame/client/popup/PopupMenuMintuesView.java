package com.frame.client.popup;

import android.content.Context;
import android.widget.BaseAdapter;

import com.frame.client.R;

/**
 * Created by Hongmingwei on 2017/9/13.
 * Email: 648600445@qq.com
 */

public class PopupMenuMintuesView extends AbsPopupMenuView {

    /**
     * params
     */
    private PopMenuTypeAdapter adapter;

    public PopupMenuMintuesView(Context context, int itemNums) {
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
    protected BaseAdapter getAdapter() {
        if (adapter == null){
            adapter = new PopMenuTypeAdapter(mContext, mItems, lastSelectedId);
            this.typeListView.setAdapter(adapter);
        } else {
            adapter.updateItems(mItems);
            adapter.setLastSelectedId(lastSelectedId);
        }
        return adapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.vw_pop_minutes;
    }
}
