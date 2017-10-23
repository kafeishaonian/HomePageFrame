package com.frame.client.recyclerPopup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;

import com.frame.client.R;
import com.frame.client.popup.MenuTypeItem;
import com.frame.client.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * Created by Hongmingwei on 2017/9/13.
 * Email: 648600445@qq.com
 */

public abstract class AbsPopupRecyclerView {

    /**
     * TAG
     */
    private static final String TAG = AbsPopupRecyclerView.class.getSimpleName();
    /**
     * View
     */
    protected PopupWindow mPopupWindow = null;
    protected ViewGroup mMenul;
//    protected ListView typeListView;
    protected RecyclerView typeRecyclerView;
    /**
     * Params
     */
    protected PopRecyclerAdapter adapter;
    protected ArrayList<MenuTypeItem> mItems;
    protected Context mContext;
    protected int lastSelectedId = -1;
    private ISpinnerWindowClickListener spinnerWindowClickListener;

    public final long getLastSelected() {
        return lastSelectedId;
    }

    public void setLastSelected(int index) {
        lastSelectedId = index;
    }

    public AbsPopupRecyclerView(Context context, int itemNums){
        this.mContext = context;
        initialControl(itemNums);
    }

    public final void setOnPopupMenuClickListener(ISpinnerWindowClickListener mOnPopupMenuClickListener){
        this.spinnerWindowClickListener = mOnPopupMenuClickListener;
    }

    public static interface ISpinnerWindowClickListener{
        void onSpinnerItemChicked(View view, long position, int typeId);

        void onSpinnerOutSideClicked();
    }

    private void initialControl(int itemNums){
        if (mPopupWindow != null){
            return;
        }
        mItems = new ArrayList<MenuTypeItem>();
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenul = (ViewGroup) mLayoutInflater.inflate(getLayoutId(), null);
        if (itemNums > 0){
            mMenul.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, DisplayUtils.dipToPx(mContext,
                    getItemHeight()) * itemNums));
        }
        mPopupWindow = new PopupWindow(mMenul, DisplayUtils.dipToPx(mContext, getItemWidth()), LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mMenul.setFocusableInTouchMode(true);
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.setClippingEnabled(false);
        mMenul.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closePopupWindow();//这里写明模拟menu的popupWindow退出
                spinnerWindowClickListener.onSpinnerOutSideClicked();
                return true;
            }
        });
        mMenul.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && isShowing()){
                    closePopupWindow();
                    spinnerWindowClickListener.onSpinnerOutSideClicked();
                    return true;
                }
                return false;
            }
        });
        View view = mMenul.findViewById(R.id.type_listview);
        if (view != null){
            typeRecyclerView = (RecyclerView) view;

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(getOrientation());
            typeRecyclerView.setLayoutManager(layoutManager);
            typeRecyclerView.setHasFixedSize(true);
            adapter.setOnItemClickListener(new PopRecyclerTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, int id) {
                    lastSelectedId = position;
                    closePopupWindow();
                    spinnerWindowClickListener.onSpinnerItemChicked(view, id, position);
                }
            });
        }
        typeRecyclerView.setVisibility(View.VISIBLE);

    }


    public void addTypeList(ArrayList<MenuTypeItem> list) {
        mItems.clear();
        mItems.addAll(list);
    }

    public void showPopupWindow(View mAnchor, int xoff, int yoff) {
        if (mAnchor == null) {
            return;
        }
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            adapter = getAdapter();
            mPopupWindow.showAsDropDown(mAnchor,xoff, yoff);

        } else if (mPopupWindow.isShowing()) {
            closePopupWindow();
        }
    }



    public boolean isShowing(){
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    /**
     * close popupwindow
     */
    public void closePopupWindow(){
        if (mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }


    protected abstract int getItemWidth();

    protected abstract int getItemHeight();

    protected abstract PopRecyclerAdapter getAdapter();

    protected abstract int getLayoutId();

    protected abstract int getOrientation();


}
