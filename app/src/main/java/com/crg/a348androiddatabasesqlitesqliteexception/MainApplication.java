package com.crg.a348androiddatabasesqlitesqliteexception;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by crg on 16-8-9.
 */
public class MainApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900045883", true);
    }
}
