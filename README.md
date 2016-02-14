# QQBotXposed
一个基于 Xposed 的 QQ 聊天机器人。（还不能用）

# 用法

1. 安装 Xposed 和 QQ for Android （测试版本为 V 6.2.1.2690）
2. 编译插件并安装，在 Xposed 中启用
3. 重启手机
4. 打开 QQ 选择你想要自动回复的人或群，进去聊天界面，之后可 Home 最小化或关闭屏幕
5. 向 Bot 登陆的 QQ 号发送 “test” ，Bot 会向之前选定的聊天对象回复 “来自 Xposed QQBot 的测试消息”

# 问题

1. 不管是谁向 Bot 发送消息，Bot 都会向聊天对象回复消息。
2. 不能实现多目标。
3. 不能接收图片。
