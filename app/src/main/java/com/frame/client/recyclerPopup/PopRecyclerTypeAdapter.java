package com.frame.client.recyclerPopup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.client.R;
import com.frame.client.popup.MenuTypeItem;

import java.util.ArrayList;

/**
 * Created by Hongmingwei on 2017/9/13.
 * Email: 648600445@qq.com
 */

public class PopRecyclerTypeAdapter extends PopRecyclerAdapter implements View.OnClickListener{

    private ArrayList<MenuTypeItem> mItems;
    private LayoutInflater mInflater;
    private int mLastSelectedId;
    private Context mContext;

    public PopRecyclerTypeAdapter(Context context, ArrayList<MenuTypeItem> items, int lastSelectedId){
        mContext = context;
        mItems = items;
        mLastSelectedId = lastSelectedId;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateItems(ArrayList<MenuTypeItem> items){
        if (items != null && items.size() > 0){
            mItems = items;
        }
    }

    public void setLastSelectedId(int lastSelectedId){
        mLastSelectedId = lastSelectedId;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.listitem_menu_type, parent, false);
        ItemCache viewCache = new ItemCache(view);
        view.setOnClickListener(this);
        return viewCache;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemCache viewCache = (ItemCache) holder;
        viewCache.textView.setText(mItems.get(position).getText());
        if (position == mLastSelectedId){
            viewCache.textView.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            viewCache.textView.setTextColor(mContext.getResources().getColor(R.color.dark3));
        }

        if (mItems.get(position).getResourceId() == 0){
            viewCache.imageView.setVisibility(View.GONE);
        } else {
            viewCache.imageView.setVisibility(View.VISIBLE);
            viewCache.imageView.setImageResource(mItems.get(position).getResourceId());
        }
        viewCache.itemView.setTag(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null){
            onItemClickListener.onItemClick(v, (int)v.getTag(), (int) v.getTag());
        }
    }


    public static class ItemCache extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ItemCache(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.type_name);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
