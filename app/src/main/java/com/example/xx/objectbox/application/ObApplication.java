package com.example.xx.objectbox.application;

import android.app.Application;

import com.example.xx.objectbox.bean.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * 日期：2018/3/7
 * 描述：Application
 *
 * @author XX
 */

public class ObApplication extends Application {

    private static BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        initObjectBox();
    }

    private void initObjectBox() {
        boxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}
