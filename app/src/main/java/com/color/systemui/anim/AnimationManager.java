package com.color.systemui.anim;

import android.content.Context;
import android.view.View;
import android.util.Log;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.color.systemui.utils.StaticInstanceUtils;
import com.color.systemui.utils.StaticVariableUtils;


public class AnimationManager {

    private Context mycontext;

    public AnimationManager() {

    }

    public void setContext(Context context) {
        mycontext = context;
    }


    //1、悬浮球
    public void lefthoverballShowAnimation() {

        StaticInstanceUtils.hoverball.leftlayout.setAlpha(0.0f);
        StaticInstanceUtils.hoverball.leftlayout.setTranslationX(-StaticInstanceUtils.hoverball.leftlayout.getWidth());
        StaticInstanceUtils.hoverball.leftlayout.setVisibility(View.VISIBLE);

        StaticInstanceUtils.hoverball.leftlayout.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(200)
                .start();
    }

    public void lefthoverballHideAnimation() {
        StaticInstanceUtils.hoverball.leftlayout.setAlpha(1.0f);
        StaticInstanceUtils.hoverball.leftlayout.setTranslationX(0);
        StaticInstanceUtils.hoverball.leftlayout.animate()
                .alpha(0.0f)
                .translationX(-StaticInstanceUtils.hoverball.leftlayout.getWidth())
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (StaticInstanceUtils.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_lefthoverball_hide == 0) {
                            //StaticInstanceUtils.windowManagerToOsdObserver.globalClick 是定时标志，HoverballView.vvGone是主动触发消失
                            StaticInstanceUtils.hoverball.leftlayout.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_lefthoverball_hide = 1;
                        }
                    }

                })
                .start();
    }

    public void righthoverballShowAnimation() {
        StaticInstanceUtils.hoverball.rightlayout.setAlpha(0.0f);
        StaticInstanceUtils.hoverball.rightlayout.setTranslationX(StaticInstanceUtils.hoverball.rightlayout.getWidth());
        StaticInstanceUtils.hoverball.rightlayout.setVisibility(View.VISIBLE);

        //HoverballView.vv.startAnimation(leftIn);

        StaticInstanceUtils.hoverball.rightlayout.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(200)
                .start();
    }

    public void righthoverballHideAnimation() {
        StaticInstanceUtils.hoverball.rightlayout.setAlpha(1.0f);
        StaticInstanceUtils.hoverball.rightlayout.setTranslationX(0);
        StaticInstanceUtils.hoverball.rightlayout.animate()
                .alpha(0.0f)
                .translationX(StaticInstanceUtils.hoverball.rightlayout.getWidth())
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (StaticInstanceUtils.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_righthoverball_hide == 0) {
                            //StaticInstanceUtils.windowManagerToOsdObserver.globalClick 是定时标志，HoverballView.vvGone是主动触发消失
                            StaticInstanceUtils.hoverball.rightlayout.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_righthoverball_hide = 1;
                        }
                    }

                })
                .start();
    }

    //2、导航栏
    public void leftNavibarShowAnimation() {

        StaticInstanceUtils.navigationBar.leftNavibar.setAlpha(0.0f);
        StaticInstanceUtils.navigationBar.leftNavibar.setTranslationX(-StaticInstanceUtils.navigationBar.leftNavibar.getWidth());
        StaticInstanceUtils.navigationBar.leftNavibar.setVisibility(View.VISIBLE);
        StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.navigationBar.leftNavibar, StaticInstanceUtils.navigationBar.lp);
        StaticInstanceUtils.navigationBar.leftNavibar.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(200)
                .start();

    }

    public void leftNavibarHideAnimation() {

        StaticInstanceUtils.navigationBar.leftNavibar.setAlpha(1.0f);
        StaticInstanceUtils.navigationBar.leftNavibar.setTranslationX(0);
        StaticInstanceUtils.navigationBar.leftNavibar.animate()
                .alpha(0.0f)
                .translationX(-StaticInstanceUtils.navigationBar.leftNavibar.getWidth())
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (StaticInstanceUtils.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_leftnavibar_hide ==0) {
                            StaticInstanceUtils.navigationBar.leftNavibar.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_leftnavibar_hide = 1;
                        }
                    }

                })
                .start();

    }

    public void rightNavibarShowAnimation() {

        StaticInstanceUtils.navigationBar.rightNavibar.setAlpha(0.0f);
        StaticInstanceUtils.navigationBar.rightNavibar.setTranslationX(StaticInstanceUtils.navigationBar.rightNavibar.getWidth());
        StaticInstanceUtils.navigationBar.rightNavibar.setVisibility(View.VISIBLE);
        StaticInstanceUtils.mavts.wm.updateViewLayout(StaticInstanceUtils.navigationBar.rightNavibar, StaticInstanceUtils.navigationBar.lp2);
        StaticInstanceUtils.navigationBar.rightNavibar.animate()
                .alpha(1.0f)
                .translationX(0)
                .setDuration(200)
                .start();

    }

    public void rightNavibarHideAnimation() {

        StaticInstanceUtils.navigationBar.rightNavibar.setAlpha(1.0f);
        StaticInstanceUtils.navigationBar.rightNavibar.setTranslationX(0);
        StaticInstanceUtils.navigationBar.rightNavibar.animate()
                .alpha(0.0f)
                .translationX(StaticInstanceUtils.navigationBar.rightNavibar.getWidth())
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // 动画结束时执行的操作
                        if (StaticInstanceUtils.windowManagerToOsdObserver.globalClick == 0
                                                || StaticVariableUtils.Proactive_triggering_rightnavibar_hide ==0) {
                            StaticInstanceUtils.navigationBar.rightNavibar.setVisibility(View.GONE);
                            StaticVariableUtils.Proactive_triggering_rightnavibar_hide = 1;
                        }
                    }

                })
                .start();

    }

}
