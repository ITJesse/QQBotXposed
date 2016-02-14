package com.itjesse.qqbot;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by Jesse on 16/2/14.
 */
public class QQBotHook {
    static String msgUid = "";
    static String frienduin = "";
    static String istroop = "";
    static String selfuin = "";
    static String isread = "";

    static Object BaseChatPie = null;

    public void log(String tag, Object log) {
        XposedBridge.log("[" + tag + "]  " + log.toString());
    }

    public void hook(final ClassLoader loader) {
        try {
            hookQQMessageFacade(loader);
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
        try {
            hookApplicationPackageManager(loader);
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }

    protected void hookQQMessageFacade(final ClassLoader loader) {
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.MessageHandlerUtils", loader, "a",
                "com.tencent.mobileqq.app.QQAppInterface",
                "com.tencent.mobileqq.data.MessageRecord", Boolean.TYPE, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("QQBot debug indentify", "2");
//                        log("QQBot debug param0", param.args[0]);
                        log("QQBot debug param1", param.args[1]);
//                        log("QQBot debug param2", param.args[2]);
                        String msgtype = XposedHelpers.getObjectField(param.args[1], "msgtype").toString();
                        String msg = XposedHelpers.getObjectField(param.args[1], "msg").toString();
                        log("QQBot debug messagerecord param z msgtype", msgtype);
                        log("QQBot debug meddagerecord param z msg", msg);
                        if (msgtype.equals("-1000")) {
                            String tmpmsgUid = XposedHelpers.getObjectField(param.args[1], "msgUid").toString();
                            String tmpfrienduin = XposedHelpers.getObjectField(param.args[1], "frienduin").toString();
                            istroop = XposedHelpers.getObjectField(param.args[1], "istroop").toString();
                            selfuin = XposedHelpers.getObjectField(param.args[1], "selfuin").toString();
                            isread = XposedHelpers.getObjectField(param.args[1], "isread").toString();
                            log("QQBot debug messagerecord param z msgUid", tmpmsgUid);
                            log("QQBot debug messagerecord param z frienduin", frienduin);
                            log("QQBot debug messagerecord param z istroop", istroop);
                            log("QQBot debug messagerecord param z selfuin", selfuin);
                            log("QQBot debug messagerecord param z isread", isread);
                            msgUid = tmpmsgUid;
                            frienduin = tmpfrienduin;
                            log("QQBot debug", "save message successful");
                            Thread.sleep(100);
                            if (msg.equals("test")) {
                                log("QQBot debug", "send test msg");
                                String title = "来自 Xposed QQBot 的测试消息";
                                Object QQAppInterface = XposedHelpers.findFirstFieldByExactType(XposedHelpers.findClass("com.tencent.mobileqq.activity.BaseChatPie", loader), XposedHelpers.findClass("com.tencent.mobileqq.app.QQAppInterface", loader)).get(BaseChatPie);
                                Object Context = XposedHelpers.findFirstFieldByExactType(XposedHelpers.findClass("com.tencent.mobileqq.activity.BaseChatPie", loader), XposedHelpers.findClass("android.content.Context", loader)).get(BaseChatPie);
                                Object SessionInfo = XposedHelpers.findFirstFieldByExactType(XposedHelpers.findClass("com.tencent.mobileqq.activity.BaseChatPie", loader), XposedHelpers.findClass("com.tencent.mobileqq.activity.aio.SessionInfo", loader)).get(BaseChatPie);
                                ArrayList arrayList = new ArrayList();
                                Object ChatActivityFacade$SendMsgParams = XposedHelpers.newInstance(XposedHelpers.findClass("com.tencent.mobileqq.activity.ChatActivityFacade$SendMsgParams", loader));
                                XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.tencent.mobileqq.activity.ChatActivityFacade", loader), "a", QQAppInterface, Context, SessionInfo, title, arrayList, ChatActivityFacade$SendMsgParams);
                            }
                        }
                    }

                }
        );

        XposedBridge.hookAllConstructors(XposedHelpers.findClass("com.tencent.mobileqq.activity.BaseChatPie", loader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                BaseChatPie = param.thisObject;
                log("QQBot debug BaseChatPie", "is being set");
                if (BaseChatPie == null) {
                    log("QQBot debug BaseChatPie", "is null !!!!!!");
                } else {
                    log("QQBot debug BaseChatPie", "is saved");
                }
            }
        });

    }

    protected void hookApplicationPackageManager(ClassLoader loader) {
        findAndHookMethod("android.app.ApplicationPackageManager", loader,
                "getInstalledApplications", int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        @SuppressWarnings("unchecked") List<ApplicationInfo> applicationInfoList
                                = (List<ApplicationInfo>) param.getResult();
                        ArrayList<ApplicationInfo> to_remove = new ArrayList<>();
                        for (ApplicationInfo info : applicationInfoList) {
                            if (info.packageName.contains("com.itjesse.qqbot") ||
                                    info.packageName.contains("de.robv.android.xposed.installer")) {
                                to_remove.add(info);
                            }
                        }
                        if (to_remove.isEmpty())
                            return;

                        applicationInfoList.removeAll(to_remove);
                    }
                });
        findAndHookMethod("android.app.ApplicationPackageManager", loader,
                "getInstalledPackages", int.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        @SuppressWarnings("unchecked") List<PackageInfo> packageInfoList
                                = (List<PackageInfo>) param.getResult();
                        ArrayList<PackageInfo> to_remove = new ArrayList<>();
                        for (PackageInfo info : packageInfoList) {
                            if (info.packageName.contains("com.itjesse") ||
                                    info.packageName.contains("de.robv.android.xposed.installer")) {
                                to_remove.add(info);
                            }
                        }
                        if (to_remove.isEmpty())
                            return;

                        packageInfoList.removeAll(to_remove);
                    }
                });
    }

}
