package com.color.systemui.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;

import com.color.systemui.interfaces.Instance;
import com.color.systemui.utils.InstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;


public class AnimationManager implements Instance {

    private Context mycontext;

    public AnimationManager() {

    }

    public void setContext(Context context) {
        mycontext = context;
    }


    //1、悬浮球
    public void lefthoverballShowAnimation() {

        STATIC_INSTANCE_UTILS.hoverball.leftlayout.setAlpha(0.0f);
        STATIC_INSTANCE_UTILS.hoverball.leftlayout.setTranslationX(-STATIC_INSTANCE_UTILS.hoverball.leftlayout.getWidth());
        STATIC_INSTANCE_UTILS.hoverball.leftlayout.setVisibility(View.VISIBLE);

        STATIC_INSTANCE_UTILS.hoverball.leftlayout.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(300)
                .start();
    }

    public void lefthoverballHideAnimation() {
        STATIC_INSTANCE_UTILS.hoverball.leftlayout.setAlpha(1.0f);
        STATIC_INSTANCE_UTILS.hoverball.leftlayout.setTranslationX(0);
        STATIC_INSTANCE_UTILS.hoverball.leftlayout.animate()
                .alpha(0.0f)
                .translationX(-STATIC_INSTANCE_UTILS.hoverball.leftlayout.getWidth())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (STATIC_INSTANCE_UTILS.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_lefthoverball_hide == 0) {
                            //InstanceUtils.windowManagerToOsdObserver.globalClick 是定时标志，HoverballView.vvGone是主动触发消失
                            Log.d("SettingsControlHoverballObserver"," 动画执行完毕");
                            STATIC_INSTANCE_UTILS.hoverball.leftlayout.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_lefthoverball_hide = 1;
                        }
                    }

                })
                .start();
    }

    public void righthoverballShowAnimation() {
        STATIC_INSTANCE_UTILS.hoverball.rightlayout.setAlpha(0.0f);
        STATIC_INSTANCE_UTILS.hoverball.rightlayout.setTranslationX(STATIC_INSTANCE_UTILS.hoverball.rightlayout.getWidth());
        STATIC_INSTANCE_UTILS.hoverball.rightlayout.setVisibility(View.VISIBLE);

        //HoverballView.vv.startAnimation(leftIn);

        STATIC_INSTANCE_UTILS.hoverball.rightlayout.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(300)
                .start();
    }

    public void righthoverballHideAnimation() {
        STATIC_INSTANCE_UTILS.hoverball.rightlayout.setAlpha(1.0f);
        STATIC_INSTANCE_UTILS.hoverball.rightlayout.setTranslationX(0);
        STATIC_INSTANCE_UTILS.hoverball.rightlayout.animate()
                .alpha(0.0f)
                .translationX(STATIC_INSTANCE_UTILS.hoverball.rightlayout.getWidth())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (STATIC_INSTANCE_UTILS.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_righthoverball_hide == 0) {
                            //InstanceUtils.windowManagerToOsdObserver.globalClick 是定时标志，HoverballView.vvGone是主动触发消失
                            STATIC_INSTANCE_UTILS.hoverball.rightlayout.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_righthoverball_hide = 1;
                        }
                    }

                })
                .start();
    }

    //2、导航栏
    public void leftNavibarShowAnimation() {

        STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setAlpha(0.0f);
        STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setTranslationX(-STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.getWidth());
        STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setVisibility(View.VISIBLE);
        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.navigationBar.leftNavibar, STATIC_INSTANCE_UTILS.navigationBar.lp);
        STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(300)
                .start();

    }

    public void leftNavibarHideAnimation() {

        STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setAlpha(1.0f);
        STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setTranslationX(0);
        STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.animate()
                .alpha(0.0f)
                .translationX(-STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.getWidth())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (STATIC_INSTANCE_UTILS.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_leftnavibar_hide ==0) {
                            STATIC_INSTANCE_UTILS.navigationBar.leftNavibar.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_leftnavibar_hide = 1;
                        }
                    }

                })
                .start();

    }

    public void rightNavibarShowAnimation() {

        STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setAlpha(0.0f);
        STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setTranslationX(STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.getWidth());
        STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setVisibility(View.VISIBLE);
        STATIC_INSTANCE_UTILS.mavts.wm.updateViewLayout(STATIC_INSTANCE_UTILS.navigationBar.rightNavibar, STATIC_INSTANCE_UTILS.navigationBar.lp2);
        STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(300)
                .start();

    }

    public void rightNavibarHideAnimation() {

        STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setAlpha(1.0f);
        STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setTranslationX(0);
        STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.animate()
                .alpha(0.0f)
                .translationX(STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.getWidth())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (STATIC_INSTANCE_UTILS.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_rightnavibar_hide ==0) {
                            STATIC_INSTANCE_UTILS.navigationBar.rightNavibar.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_rightnavibar_hide = 1;
                        }
                    }

                })
                .start();

    }

    public void statusbarShowAnimation() {
        synchronized (mycontext) {
            STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.statusBar.statusbar, "scaleX", 0.5f, 1.0f);
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.statusBar.statusbar, "scaleY", 0.5f, 1.0f);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.statusBar.statusbar, "alpha", 0.0f, 1.0f);

            AnimatorSet entranceAnimatorSet = new AnimatorSet();
            entranceAnimatorSet.setDuration(500); // 设置动画持续时间为1秒
            entranceAnimatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);

            entranceAnimatorSet.start();
//        entranceAnimatorSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                // 动画结束时执行的操作
//                STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.VISIBLE);
//            }
//        });
        }

    }

    public void statusBarHideAnimation() {
        synchronized (mycontext) {
            // 退场动画
            ObjectAnimator scaleXAnimatorExit = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.statusBar.statusbar, "scaleX", 1.0f, 0.5f);
            ObjectAnimator scaleYAnimatorExit = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.statusBar.statusbar, "scaleY", 1.0f, 0.5f);
            ObjectAnimator alphaAnimatorExit = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.statusBar.statusbar, "alpha", 1.0f, 0.0f);

            AnimatorSet exitAnimatorSet = new AnimatorSet();
            exitAnimatorSet.setDuration(500); // 设置动画持续时间为1秒
            exitAnimatorSet.playTogether(scaleXAnimatorExit, scaleYAnimatorExit, alphaAnimatorExit);

            exitAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束时执行的操作
                    STATIC_INSTANCE_UTILS.statusBar.statusbar.setVisibility(View.GONE);
                }
            });
            // 应用动画到视图上
            exitAnimatorSet.start(); //退场动画

        }
    }

    public void NotificationShowAnimation() {
        synchronized (mycontext) {
            STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.VISIBLE);
            ObjectAnimator alphaAnimatorExit = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.myNotification.notification, "alpha", 0.0f, 1.0f);

            AnimatorSet exitAnimatorSet = new AnimatorSet();
            exitAnimatorSet.setDuration(300);
            exitAnimatorSet.play(alphaAnimatorExit);

            exitAnimatorSet.start();
        }
    }

    public void NotificationHideAnimation() {
        synchronized (mycontext) {
            ObjectAnimator alphaAnimatorExit = ObjectAnimator.ofFloat(STATIC_INSTANCE_UTILS.myNotification.notification, "alpha", 1.0f, 0.0f);

            AnimatorSet exitAnimatorSet = new AnimatorSet();
            exitAnimatorSet.setDuration(300); // 设置动画持续时间为1秒
            exitAnimatorSet.play(alphaAnimatorExit);

            exitAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // 动画结束时执行的操作
                    STATIC_INSTANCE_UTILS.myNotification.notification.setVisibility(View.GONE);
                }
            });

            exitAnimatorSet.start();
        }
    }

    public void Notification_Center_Settings_HideAnimation(@NonNull View view) {
        // 创建一个渐变动画
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(200); // 设置动画持续时间
        fadeOut.setFillAfter(true); // 设置动画结束后保持最终状态

       // 监听动画结束事件
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // 动画开始时的操作
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束时的操作
                view.setVisibility(View.INVISIBLE);

                // 创建一个透明度变化动画，从0到1
                view.setAlpha(1f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // 动画重复时的操作
            }
        });
        view.startAnimation(fadeOut);

    }

    public void Notification_center_item_slideright(@NonNull View view) {
        // 获取视图当前的水平偏移量
        float currentTranslationX = view.getTranslationX();

        // 创建一个从当前位置平移到0的平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(currentTranslationX, 0, 0, 0);
        translateAnimation.setDuration(200); // 设置动画持续时间
        translateAnimation.setFillAfter(true); // 设置动画结束后保持最终状态

        // 设置动画监听器
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // 动画开始时的操作
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束时的操作
//                view.setTranslationX(0); // 重置水平偏移量为0
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // 动画重复时的操作
            }
        });

        view.setTranslationX(0); // 重置水平偏移量为0
        // 开始动画
        view.startAnimation(translateAnimation);
    }

    public void Notification_center_item_slideleft(@NonNull View view , @NonNull View settings , int distance) {
        // 获取视图当前的水平偏移量
        float currentTranslationX = view.getTranslationX();

        // 计算目标位置
        float targetTranslationX = distance;

        // 创建属性动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", currentTranslationX, targetTranslationX);
        animator.setDuration(200); // 设置动画持续时间

        // 设置动画结束监听器
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 动画结束时让settings可见
                settings.setVisibility(View.VISIBLE);
            }
        });

        view.setTranslationX(distance); // 重置水平偏移量为0
        // 开始动画
        animator.start();
    }

}
