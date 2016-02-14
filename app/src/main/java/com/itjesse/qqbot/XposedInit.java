package com.itjesse.qqbot;

import android.util.SparseArray;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Jesse on 16/2/14.
 */
public class XposedInit implements IXposedHookLoadPackage {
    private SparseArray<QQBotHook> mQQHooks;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.contains("com.tencent.mobileqq"))
            return;

        XposedBridge.log("QQBot loaded app: " + loadPackageParam.packageName);

        QQBotHook hooks = getHooks(loadPackageParam.appInfo.uid);
        if (hooks != null)
            hooks.hook(loadPackageParam.classLoader);
    }

    private QQBotHook getHooks(int uid) {
        if (mQQHooks == null) {
            mQQHooks = new SparseArray<>();
        }
        if (mQQHooks.indexOfKey(uid) != -1) {
            return mQQHooks.get(uid);
        }

        mQQHooks.put(uid, new QQBotHook());

        return mQQHooks.get(uid);
    }

}
