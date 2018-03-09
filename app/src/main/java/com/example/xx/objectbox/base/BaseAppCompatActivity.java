package com.example.xx.objectbox.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 日期：2018/3/8
 * 描述：
 *
 * @author XX
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setLayout());
        unbinder = ButterKnife.bind(this);
        getIntentData();
        initData();
        bindEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
        unbinder.unbind();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            View v = this.getCurrentFocus();
            if (this.isShouldHideInput(v, ev) && v != null) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && v instanceof EditText) {
            int[] leftTop = new int[]{0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return event.getX() <= (float) left || event.getX() >= (float) right || event.getY() <= (float) top || event.getY() >= (float) bottom;
        } else {
            return false;
        }
    }

    protected abstract int setLayout();

    protected abstract void destroy();

    protected abstract void getIntentData();

    protected abstract void initData();

    protected abstract void bindEvent();
}
