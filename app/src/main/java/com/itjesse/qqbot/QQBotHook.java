package com.itjesse.qqbot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
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

    static Context globalcontext = null;
    static Object FriendsManager = null;
    static Object HotChatManager = null;
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
                        log("QQBot debug indentify", "3");
//                        log("QQBot debug param0", param.args[0]);
                        log("QQBot debug param1", param.args[1]);
//                        log("QQBot debug param2", param.args[2]);
                        String msgtype = XposedHelpers.getObjectField(param.args[1], "msgtype").toString();
                        String msg = XposedHelpers.getObjectField(param.args[1], "msg").toString();
                        log("QQBot debug messagerecord param z msgtype", msgtype);
                        if (msgtype.equals("-1000")) {
                            String tmpmsgUid = XposedHelpers.getObjectField(param.args[1], "msgUid").toString();
                            String tmpfrienduin = XposedHelpers.getObjectField(param.args[1], "frienduin").toString();
                            istroop = XposedHelpers.getObjectField(param.args[1], "istroop").toString();
                            selfuin = XposedHelpers.getObjectField(param.args[1], "selfuin").toString();
                            isread = XposedHelpers.getObjectField(param.args[1], "isread").toString();
                            log("QQBot debug meddagerecord param z msg", msg);
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
                                Object SessionInfo = XposedHelpers.newInstance(XposedHelpers.findClass("com.tencent.mobileqq.activity.aio.SessionInfo", loader), new Object[0]);
                                utils.changAccessory(XposedHelpers.findClass("com.tencent.mobileqq.activity.aio.SessionInfo", loader), String.class, "a").set(SessionInfo, frienduin);
                                utils.changAccessory(XposedHelpers.findClass("com.tencent.mobileqq.activity.aio.SessionInfo", loader), Integer.TYPE, "a").setInt(SessionInfo, Integer.parseInt(istroop));
                                ArrayList arrayList = new ArrayList();
                                Object ChatActivityFacade$SendMsgParams = XposedHelpers.newInstance(XposedHelpers.findClass("com.tencent.mobileqq.activity.ChatActivityFacade$SendMsgParams", loader), new Object[0]);
                                XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.tencent.mobileqq.activity.ChatActivityFacade", loader), "a", new Object[] { QQAppInterface, Context, SessionInfo, title, arrayList, ChatActivityFacade$SendMsgParams });
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

        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.activity.SplashActivity", loader, "doOnCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                globalcontext = (Context) param.thisObject;
                if (globalcontext == null) {
                    log("hongbao debug", "save the globalconext is null");
                }
            }
        });


        XposedHelpers.findAndHookConstructor("com.tencent.mobileqq.app.FriendsManager", loader, "com.tencent.mobileqq.app.QQAppInterface", new

                XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        FriendsManager = param.thisObject;
                        if (FriendsManager == null) {
                            log("QQBot debug FriendsManager", "is null****");
                        } else {
                            log("QQBot debug", "save the FriendsManager");
                        }
                    }
                });
        XposedHelpers.findAndHookConstructor("com.tencent.mobileqq.app.HotChatManager", loader, "com.tencent.mobileqq.app.QQAppInterface", new

                        XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                HotChatManager = param.thisObject;
                                if (HotChatManager == null) {
                                    log("QQBot debug FriendsManager", "is null****");
                                } else {
                                    log("QQBot debug", "save the HotChatManager");
                                }
                            }
                        }
        );


        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.pluginsdk.PluginProxyActivity", loader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Intent intent = (Intent) XposedHelpers.callMethod(param.thisObject, "getIntent");
                log("hongbao plugin", intent.getStringExtra("pluginsdk_launchActivity"));
                ClassLoader classLoader = (ClassLoader) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.tencent.mobileqq.pluginsdk.PluginStatic", loader), "a", param.thisObject, XposedHelpers.getObjectField(param.thisObject, "k").toString(), XposedHelpers.getObjectField(param.thisObject, "i").toString());
                if (intent.getStringExtra("pluginsdk_launchActivity").equals("com.tenpay.android.qqplugin.activity.GrapHbActivity")) {
                    XposedHelpers.findAndHookMethod("com.tenpay.android.qqplugin.activity.GrapHbActivity", classLoader, "a", JSONObject.class,
                            new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    JSONObject jsonObject = (JSONObject) param.args[0];
                                    log("hongbao debug", jsonObject.toString());
//                                    Object obj = XposedHelpers.getObjectField(param.thisObject, "mCloseBtn");
                                    XposedHelpers.callMethod(param.thisObject, "finish");
//
//                                    XposedHelpers.callMethod(obj, "performClick");
//                                    try {
//                                        int amount = jsonObject.getJSONObject("recv_object").getInt("amount");
//                                        log("hongbao debug get money", amount);
//                                        double a = (double) amount / 100;
//                                        toshow(loadPackageParam, true,a + " 元");
//                                    }catch (Exception e){
//                                        toshow(loadPackageParam, true,"没抢到");
//                                    }
                                }
                            });
                }
            }
        });

        XposedBridge.hookAllConstructors(XposedHelpers.findClass("com.tencent.mobileqq.activity.BaseChatPie", loader), new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                BaseChatPie = param.thisObject;
                if (BaseChatPie == null) {
                    log("QQBot debug BaseChatPie", "is null *****");
                } else {
                    log("QQBot debug", "save the BaseChatPie");
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
                            if (info.packageName.contains("com.itjesse.qqbot") ||
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
