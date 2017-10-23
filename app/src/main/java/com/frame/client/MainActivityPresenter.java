package com.frame.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

/**
 * Created by Hongmingwei on 2017/8/30.
 * Email: 648600445@qq.com
 */

public class MainActivityPresenter implements OnCheckedChangeListener, View.OnClickListener {
    /**
     * TAG
     */
    private static final String TAG = MainActivityPresenter.class.getSimpleName();

    private final MainActivity mActivity;
    private SwitchTabHandler mSwitchTabHandler;
    private static final int SWITCH_TAB_MSG = 0x102;
    /**
     * View
     */
    private ViewGroup mContentView;

    private final RadioButton mHomePageTab;
    private final RadioButton mTradeTab;
    private final RadioButton mWatchTab;
    private final RadioButton mMineTab;
    private final RadioGroup mTab;

    public MainActivityPresenter(MainActivity activity){
        mActivity = activity;
        mSwitchTabHandler = new SwitchTabHandler(activity);

        mContentView = (ViewGroup) findViewById(R.id.content_view);
        mHomePageTab = (RadioButton) findViewById(R.id.tab_homepage);
        mTradeTab = (RadioButton) findViewById(R.id.tab_trade);
        mWatchTab = (RadioButton) findViewById(R.id.tab_watch);
        mMineTab = ((RadioButton) findViewById(R.id.tab_mine));

        mHomePageTab.setOnCheckedChangeListener(this);
        mTradeTab.setOnCheckedChangeListener(this);
        mWatchTab.setOnCheckedChangeListener(this);
        mMineTab.setOnCheckedChangeListener(this);

        mTab = (RadioGroup) findViewById(R.id.rg_tabs);

        initTabs(mActivity.getIntent());
    }


    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked){
            return;
        }
        final int id = buttonView.getId();
        switch (id){
            case R.id.tab_homepage:
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                switchTab(HomePagerActivity.class, mContentView);
                break;
            case R.id.tab_trade:
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                switchTab(TwoActivity.class, mContentView);
                break;
            case R.id.tab_watch:
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                switchTab(ThreeActivity.class, mContentView);
                break;
            case R.id.tab_mine:
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                switchTab(MineActivity.class, mContentView);
                //隐藏下面的小红点
//                clearHotUpdate(MainActivity.TAB_INDEX_MINE_ACTIVITY);
                break;
        }
    }

    /**
     * 切换tab页
     * @param clazz
     * @param contentView
     */
    private void switchTab(Class<?> clazz, ViewGroup contentView){
        final Message msg = Message.obtain();
        msg.obj = new Pair<Class<?>, ViewGroup>(clazz, contentView);
        msg.what = SWITCH_TAB_MSG;
        mSwitchTabHandler.sendMessageDelayed(msg, 0);
    }

    @Override
    public void onClick(View v) {

    }

    @SuppressLint("HandlerLeak")
    private class SwitchTabHandler extends WeakReferenceHandler<MainActivity>{

        public SwitchTabHandler(MainActivity reference) {
            super(reference);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void handleMessage(MainActivity reference, Message msg) {
            if ((reference != null && reference.isFinishing())){
                return;
            }
            switch (msg.what){
                case SWITCH_TAB_MSG:
                    @SuppressWarnings("unchecked")
                    Pair<Class<?>, ViewGroup> data = (Pair<Class<?>, ViewGroup>) msg.obj;
                    if (data == null){
                        return;
                    }
                    if (!(data.second.getChildAt(0) instanceof RadioGroup)){
                        data.second.removeViewAt(0);
                    }
                    final Intent intent = new Intent(mActivity.getApplication(), data.first)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    final Intent paramsIntent = mActivity.getIntent();

                    final boolean hasExtra = paramsIntent != null && paramsIntent.hasExtra(MainActivity.EXTRA_TAB_INDEX_KEY)
                            && (data.first == convertIndexToClass(paramsIntent.getIntExtra(MainActivity.EXTRA_TAB_INDEX_KEY,
                            MainActivity.TAB_INDEX_HOMEPAGE_ACTIVITY)));
                    //包含外部push等入口跳转
                    if (hasExtra){
                        intent.putExtras(paramsIntent);
                    }
                    data.second.addView(
                            mActivity.getLocalActivityManager().startActivity(data.first.getSimpleName(), intent)
                                .getDecorView(), 0, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.MATCH_PARENT));
                    if (hasExtra){
                        mActivity.setIntent(null);
                    }

                    //维持4个tab对应的activity在BaseActivity的栈内位置
                    final Activity tabActivity = getTabActivity();
                    if (tabActivity == null){
                        return;
                    }
                    BaseActivity.refreshTopActivity((BaseActivity) tabActivity);
                    break;
            }
        }

        private Class<?> convertIndexToClass(int tabIndex){
            switch (tabIndex){
                case MainActivity.TAB_INDEX_HOMEPAGE_ACTIVITY:
                    return HomePagerActivity.class;
                case MainActivity.TAB_INDEX_TRADE_ACTIVITY:
                    return TwoActivity.class;
                case MainActivity.TAB_INDEX_WATCH_ACTIVITY:
                    return ThreeActivity.class;
                case MainActivity.TAB_INDEX_MINE_ACTIVITY:
                    return MineActivity.class;
                default:
                    break;
            }
            return null;
        }
    }

    /**
     * @return  tab选中的activity
     */
    @SuppressWarnings("deprecation")
    public Activity getTabActivity(){
        final int checkedId = mTab.getCheckedRadioButtonId();
        Class<?> clazz = null;

        switch (checkedId){
            case R.id.tab_homepage:
                clazz = HomePagerActivity.class;
                break;
            case R.id.tab_trade:
                clazz = TwoActivity.class;
                break;
            case R.id.tab_watch:
                clazz = ThreeActivity.class;
                break;
            case R.id.tab_mine:
                clazz = MineActivity.class;
                break;
            default:
                clazz = HomePagerActivity.class;
                break;
        }

        return mActivity.getLocalActivityManager().getActivity(clazz.getSimpleName());
    }

    /**
     * 为首页提供当前tab区域，用于控制隐藏和显示
     *
     * @return
     */
    public RadioGroup getTab() {
        return mTab;
    }

    /**
     * 处理选中的Tab
     * @param intent
     */
    public void initTabs(Intent intent){
        final RadioGroup tab = mTab;

        //是否需要重新设定选中tab,比如从通知栏进入
        final boolean isNeedRecheck = (intent != null) && intent.hasExtra(MainActivity.EXTRA_TAB_INDEX_KEY);
        //是否已经有选中的tab
        final boolean isDefaultChecked = tab.getCheckedRadioButtonId() != -1;

        int index;
        if (isNeedRecheck){
            // 需要重新设置选中tab，比如从通知栏调起时
            index = intent.getIntExtra(MainActivity.EXTRA_TAB_INDEX_KEY, MainActivity.TAB_INDEX_HOMEPAGE_ACTIVITY);
        } else if (isDefaultChecked){
            // 不需要重新设置tab，并且曾经已经有默认选中tab。无需处理
            return;
        } else {
            //初始化
            index = MainActivity.TAB_INDEX_HOMEPAGE_ACTIVITY;
        }
        Log.e(TAG, "indexKey = " + index);
        switch (index){
            case MainActivity.TAB_INDEX_HOMEPAGE_ACTIVITY:
                tab.check(R.id.tab_homepage);
                break;
            case MainActivity.TAB_INDEX_TRADE_ACTIVITY:
                tab.check(R.id.tab_trade);
                break;
            case MainActivity.TAB_INDEX_WATCH_ACTIVITY:
                tab.check(R.id.tab_watch);
                break;
            case MainActivity.TAB_INDEX_MINE_ACTIVITY:
                tab.check(R.id.tab_mine);
                break;
        }
    }


    /**
     * 清除更新小圆点
     */
    public void clearHotUpdate(int tag){
        if (tag == MainActivity.TAB_INDEX_MINE_ACTIVITY){
            transMineTab(false);
        }
    }

    /**
     * 通知tab上显示新数据提示
     */
    public void notifyHotUpdate(int tag){
        if (tag == MainActivity.TAB_INDEX_MINE_ACTIVITY){
            //绘制红点提示
            transMineTab(true);
        }
    }
    /**
     * 我的Tab小圆点设置
     */
    private void transMineTab(boolean isShowPoint){
        drawUpdateIndicator(mMineTab, isShowPoint, R.mipmap.newcfd_mynor2x,
                R.mipmap.newcfd_mysel2x);
    }

    /**
     * 设置热点和离线的tab上的小红点，显示有新数据
     * @param view  需要显示小红点的tab
     * @param need2Show  是否有数据更新，有显示小红点，没有还原
     * @param normalRes
     * @param checkedRes
     */
    private void drawUpdateIndicator(RadioButton view, boolean need2Show, int normalRes, int checkedRes){
        final DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        final Resources res = mActivity.getResources();

        final Bitmap indicatorRes = BitmapFactory.decodeResource(res, R.mipmap.icon_red_dot);
        final Bitmap appBitmapNormal = BitmapFactory.decodeResource(res, normalRes);
        final Bitmap appBitmapPressed = BitmapFactory.decodeResource(res, checkedRes);

        final StateListDrawable stateDrawable = new StateListDrawable();
        final int checked = android.R.attr.state_checked;
        final int pressed = android.R.attr.state_pressed;
        final int focused = android.R.attr.state_focused;
        final int selected = android.R.attr.state_selected;

        final Bitmap newBitmapNormal = drawBitmap(dm, appBitmapNormal, indicatorRes, need2Show);
        final Bitmap newBitmapPressed = drawBitmap(dm, appBitmapPressed, indicatorRes, need2Show);

        final BitmapDrawable normalDrawable = new BitmapDrawable(res, newBitmapNormal);
        final BitmapDrawable pressedDrawable = new BitmapDrawable(res, newBitmapPressed);
        //View添加Selector
        stateDrawable.addState(new int[] { focused }, pressedDrawable);
        stateDrawable.addState(new int[] { pressed }, pressedDrawable);
        stateDrawable.addState(new int[] { selected }, pressedDrawable);
        stateDrawable.addState(new int[] { checked }, pressedDrawable);
        stateDrawable.addState(new int[] { -checked }, normalDrawable);
        stateDrawable.addState(new int[] {}, normalDrawable);
        view.setCompoundDrawablesWithIntrinsicBounds(null, stateDrawable, null, null);
        view.setPadding(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, dm), 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
    }

    /**
     * 画出小红点与前景混合好的图片数据
     * @param dm 显示尺寸
     * @param background   按钮图片
     * @param indicator 小圆圈
     * @param isNeedShow 是否有数据更新
     * @return  混合好的图片
     */
    private Bitmap drawBitmap(DisplayMetrics dm, Bitmap background, Bitmap indicator, boolean isNeedShow){
        final Canvas canvas = new Canvas();
        final int yOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm);
        final int yRedOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm);
        final int width = background.getScaledWidth(dm) + indicator.getScaledWidth(dm);
        final int height = background.getScaledHeight(dm) + yRedOffset;
        final Bitmap smallBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(smallBitmap);
        final Paint textPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(background, indicator.getScaledWidth(dm) / 2, yOffset, textPainter);
        textPainter.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        if (isNeedShow){
            canvas.drawBitmap(indicator, width - indicator.getScaledWidth(dm), yRedOffset, textPainter);
        }
        canvas.save();
        return smallBitmap;
    }

}
