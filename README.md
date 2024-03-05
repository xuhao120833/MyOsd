## 一、文件目录  
&nbsp;&nbsp;&nbsp;&nbsp;该项目共有三个目录：    
&nbsp;&nbsp;&nbsp;&nbsp;com.color  
&nbsp;&nbsp;&nbsp;&nbsp;---notification 对应快捷中心  
&nbsp;&nbsp;&nbsp;&nbsp;---osd  对应遥控器菜单  
&nbsp;&nbsp;&nbsp;&nbsp;---systemui  对应悬浮球、导航栏、状态栏  


### notification  
&nbsp;&nbsp;&nbsp;&nbsp;代码从MyNotificationService.java开始看，该服务开机自启动。快捷中心分为两个部分：快捷设置和通知中心。快捷设置不难，就是RecyclerView填充了固定的内容；通知中心由MyNotificationService循环监听系统通知，有通知就过滤显示出来。通知中心这块代码比较复杂，过滤掉了不需要显示的通知、内容为null的通知，蓝牙通知单独处理，一般通知添加了左滑显示菜单、右滑移除通知功能。通知中心用的变量比较多，看不懂不必纠结，重点看MyNotificationService 和 Notification_Center_Adapter之间的主要代码逻辑即可。  
   


### osd  
&nbsp;&nbsp;&nbsp;&nbsp;代码从BootCompleteReceiver.java开始看，刷机后首次开机会先监听开机完成广播启动无障碍服务MenuService.java


### systemui  
&nbsp;&nbsp;&nbsp;&nbsp;systemui在MenuService.java的onCreate方法中启动


## 二、任意分辨率适配
&nbsp;&nbsp;&nbsp;&nbsp;notification、osd、systemui三个部分都采取了类似的适配方案，但是notification的最为成熟，看的时候直接看com.color.notification.view下面的自定义View文件，对应notification.xml看理解得更快点。


## 三、项目编译  
&nbsp;&nbsp;&nbsp;&nbsp;1、需要修改gradle.properties 中系统签名的目录，换成自己电脑的目录  
&nbsp;&nbsp;&nbsp;&nbsp;RELEASE_STORE_FILE=F\:\\cm4ks_key\\platform.jks，没有系统签名可询问组长张华开。  
&nbsp;&nbsp;&nbsp;&nbsp;2、替换sdk中的android.jar。[android.jar链接](https://drive.weixin.qq.com/s?k=ABoA9QdVAGECrT6LEt)


## 四、后续有待开发
&nbsp;&nbsp;&nbsp;&nbsp;通知中心的左滑菜单三个按键：置顶、删除、设置，其中置顶和删除功能已经开发完毕，设置键功能需要ColorSettings开发完应用通知权限功能之后，再进行按键功能绑定，实现点击设置键跳转到通知权限界面，用户可自行打开或者屏蔽掉某个应用的通知，类似手机。


#### 注：遥控器唤起的音量条、亮度条，相关代码由罗飞、王涵开发，不理解可以询问他们。


