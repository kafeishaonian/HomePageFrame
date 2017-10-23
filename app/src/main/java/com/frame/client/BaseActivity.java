package com.frame.client;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.Stack;

/**
 * Created by Hongmingwei on 2017/8/31.
 * Email: 648600445@qq.com
 */

public class BaseActivity extends FragmentActivity {
    /**
     * TAG
     */
    private static final String TAG = BaseActivity.class.getSimpleName();

    private static Stack<BaseActivity> sActivities = new Stack<BaseActivity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        removeActivity(this);
        super.onDestroy();
    }

    /**
     * 界面Activity入栈
     *
     * @param activity
     */
    private static void addActivity(BaseActivity activity) {
        if (activity == null) {
            return;
        }
        sActivities.push(activity);
    }

    /**
     * 获取栈顶界面Activity
     */
    public static BaseActivity getTopActivity() {
        if (sActivities.empty()) {
            return null;
        } else {
            return sActivities.peek();
        }
    }

    /**
     * 界面Activity出栈
     * @param activity
     */
    private static void removeActivity(BaseActivity activity) {
        if (activity == null) {
            return;
        }

        if (sActivities.contains(activity)) {
            sActivities.remove(activity);
        }
    }

    /**
     * 退出app
     */
    public static void closeApplication() {
        if (!sActivities.empty()) {
            for (int i = 0; i < sActivities.size(); i++) {
                BaseActivity activity=sActivities.get(i);
                if (activity != null && !activity.isFinishing())
                    activity.finish();
            }
            sActivities.clear();
        }
    }


    public static void refreshTopActivity(BaseActivity activity) {
        removeActivity(activity);
        addActivity(activity);
    }
}
