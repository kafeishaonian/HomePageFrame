package com.frame.client;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends ActivityGroup {

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * param
     */
    public static final int TAB_INDEX_HOMEPAGE_ACTIVITY = 0x00;//首页
    public static final int TAB_INDEX_TRADE_ACTIVITY = 0x01;//交易
    public static final int TAB_INDEX_WATCH_ACTIVITY = 0x02;//投资圈
    public static final int TAB_INDEX_MINE_ACTIVITY = 0x03;//我的

    public static final String EXTRA_TAB_INDEX_KEY = "TAB_INDEX_KEY";// 用来传递从通知栏进入时，需要切换的tab

    private MainActivityPresenter mPresenter;
    private static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public static MainActivity getInstance(){
        return instance;
    }

    private void initView(){
        mPresenter = new MainActivityPresenter(this);
        instance = this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * 通过传递的Intent初始化相应的Ui
     * @param intent
     */
    private void handleIntent(Intent intent){
        if (intent == null){
            Log.e(TAG, "handleIntent intent is null");
            return;
        }
        if (intent.hasExtra(EXTRA_TAB_INDEX_KEY)){// 如果是从通知栏或wap页进入时，切换page
            setIntent(intent);
            if (mPresenter != null){
                mPresenter.initTabs(intent);
            }
        }
    }

    /**
     * 更新小红点
     */
    public void notifiyHotUpadate(int tag){
        mPresenter.notifyHotUpdate(tag);
    }

    /**
     * 更新小红点
     */
    public void clearHotUpadate(int tag){
        mPresenter.clearHotUpdate(tag);
    }

    /**
     * 跳转到交易页
     */
    public void switchToTradeTab() {
        if (mPresenter == null || mPresenter.getTab() == null) {
            return;
        }
        mPresenter.getTab().check(R.id.tab_trade);
    }

    /**
     * 跳转到投资圈
     */
    public void switchToLiveTab() {
        if (mPresenter == null || mPresenter.getTab() == null) {
            return;
        }
        mPresenter.getTab().check(R.id.tab_watch);
    }

    /**
     * 跳转到首页
     */
    public void switchToHomePageTab() {
        if (mPresenter == null || mPresenter.getTab() == null) {
            return;
        }
        mPresenter.getTab().check(R.id.tab_homepage);
    }

    /**
     * 跳转到我的页面
     */
    public void switchToMineTab(){
        if (mPresenter == null || mPresenter.getTab() == null){
            return;
        }
        mPresenter.getTab().check(R.id.tab_mine);
    }
}
