package com.frame.client;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.frame.client.popup.AbsPopupMenuView;
import com.frame.client.popup.MenuTypeItem;
import com.frame.client.popup.PopupMenuMintuesView;
import com.frame.client.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * Created by Hongmingwei on 2017/8/30.
 * Email: 648600445@qq.com
 */

public class HomePagerActivity extends BaseActivity{


    private AbsPopupMenuView mPopupMenuView;
    private ArrayList<MenuTypeItem> mMenuList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }


    private void initPopup(){
        MenuTypeItem menu = new MenuTypeItem("123");

        int size = mMenuList.size();
        mPopupMenuView = new PopupMenuMintuesView(this, size);
        mPopupMenuView.addTypeList(mMenuList);
        mPopupMenuView.setOnPopupMenuClickListener(new AbsPopupMenuView.ISpinnerWindowClickListener() {
            @Override
            public void onSpinnerItemChicked(View view, long position, int typeId) {
                mPopupMenuView.setLastSelected(typeId);

                switch (typeId){
                    case 0:

                        break;
                }
            }

            @Override
            public void onSpinnerOutSideClicked() {

            }
        });
        int id;
//        mPopupMenuView.showPopupWindow("位置", DisplayUtils.dipToPx(this, -5), DisplayUtils.dipToPx(this, -2));
    }
}
