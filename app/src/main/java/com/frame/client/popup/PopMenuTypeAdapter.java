package com.frame.client.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.client.R;

import java.util.ArrayList;

/**
 * Created by Hongmingwei on 2017/9/13.
 * Email: 648600445@qq.com
 */

public class PopMenuTypeAdapter extends BaseAdapter{

    private ArrayList<MenuTypeItem> mItems;
    private LayoutInflater mInflater;
    private int mLastSelectedId;
    private Context mContext;

    public PopMenuTypeAdapter(Context context, ArrayList<MenuTypeItem> items, int lastSelectedId){
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
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemCache viewCache = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.listitem_menu_type, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.type_name);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            viewCache  =new ItemCache();
            viewCache.textView = textView;
            viewCache.imageView = imageView;

            convertView.setTag(viewCache);
            convertView.setId(mItems.get(position).getTypeId());
        } else {
            viewCache = (ItemCache) convertView.getTag();
        }

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
        return convertView;
    }


    class ItemCache {
        TextView textView;
        ImageView imageView;
    }

}
