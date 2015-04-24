package com.piaojin.tools;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import oa.piaojin.com.androidoa.R;

/**
 * Created by piaojin on 2015/3/30.
 */
public class MyAnimationUtils {
    //动画
    public static void ScaleIn(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.scale_in));
    }

    public static void ScaleOut(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.scale_out));
    }

    public static void DownIn(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.down_in));
    }

    public static void DownOut(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.down_out));
    }

    public static void RotateIn(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.rotate_in));
    }

    public static void AlphaIn(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.alpha_in));
    }
    public static void AlphaOut(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.alpha_out));
    }
    public static void LeftOut(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.left_out));
    }
    public static void RightIn(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.right_in));
    }
    public static void TopOut(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.top_out));
    }
    public static void RotateOut(View view, Context context) {
        view.setAnimation(AnimationUtils.loadAnimation(context,
                R.anim.rotate_out));
    }
}
