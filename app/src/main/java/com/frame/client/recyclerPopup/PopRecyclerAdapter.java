package com.frame.client.recyclerPopup;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Hongmingwei on 2017/9/15.
 * Email: 648600445@qq.com
 */

public abstract class PopRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    public PopRecyclerTypeAdapter.OnItemClickListener onItemClickListener = null;

    public PopRecyclerAdapter(){

    }

    public static interface OnItemClickListener{
        void onItemClick(View view, int position, int id);
    }

    public void setOnItemClickListener(PopRecyclerTypeAdapter.OnItemClickListener listener){
        onItemClickListener = listener;
    }
}
