/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tws.trevon.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.tws.trevon.constants.ISysConfig;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chandra.kalwar
 *  This class will not have DB interactions... so no instance variables as well
 */

/**
 *
 * @author chandra.kalwar
  This class will not have DB interactions... so no instance variables as well
 */
public final class AnimUtilities
{
    private static final String TAG = AnimUtilities.class.getSimpleName();

    public static void blinkWithColor(final View view, final float color) {
        ObjectAnimator anim
                = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), color, view.getAlpha(), color, view.getAlpha(), color, view.getAlpha(), color, view.getAlpha());
        anim.setDuration(3000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

   public static void moveX(final View view, final float finalX) {
        ObjectAnimator anim
                = ObjectAnimator.ofFloat(view, "x", 0.0f, finalX);
        anim.setDuration(3000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public static void moveYFromStartToEnd(final View view) {
        ObjectAnimator anim
                = ObjectAnimator.ofFloat(view, "y", 0.0f, view.getY());
        anim.setDuration(3000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    public static void flipOnVertical(final View view) {
        ObjectAnimator anim
                = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 360.0f);
        anim.setDuration(1000);
        anim.start();
    }

    public static void flipOnHorizontal(final View view) {
        ObjectAnimator anim
                = ObjectAnimator.ofFloat(view, "rotationX", 0.0f, 360.0f);
        anim.setDuration(1000);
        anim.start();
    }

    public void disappearBox(final View view){
        ObjectAnimator anim
                = ObjectAnimator.ofFloat(view, "alpha",
                1.0f, 0.25f, 0.75f, 0.15f, 0.5f, 0.0f);
        anim.setDuration(5000);
        anim.start();
    }

    //AppUtilities.animateView(recyclerView, R.anim.sequence_animation);
    public static void animateView(final View view, final int resId)
    {
        Animation animation = AnimationUtils.loadAnimation(AppController.getInstance(), resId);
        view.startAnimation(animation);
    }

    public static void moveViewsFromStartToEnd(final View... viewArray)
    {
        List<Animator> animList = new ArrayList<Animator>();
        for (int i = 0; i < viewArray.length; i++)
        {
            View view = viewArray[i];
            ObjectAnimator anim
                    = ObjectAnimator.ofFloat(view, "y", 0.0f, view.getY());
            animList.add(anim);
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.playTogether(animList);
        animSet.setDuration(5000);
        animSet.start();
    }

    public static void rotateViewsHorizontalHalf(Animator.AnimatorListener animatorListener, final View... viewArray)
    {
        List<Animator> animList = new ArrayList<Animator>();
        for (int i = 0; i < viewArray.length; i++)
        {
            View view = viewArray[i];
            ObjectAnimator anim
                    = ObjectAnimator.ofFloat(view, "rotationX", 0.0f, -120, 0.0f);
            animList.add(anim);
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.playTogether(animList);
        animSet.setDuration(ISysConfig.RESET_ANIMATION_TIME);
        animSet.start();

        animSet.addListener(animatorListener);
    }

/*    public static void blinkAllViews(final List viewList)
    {
        blinkAllViews(viewList.toArray());
    }*/

    public static void blinkAllViews(final List<View> viewList)
    {
        blinkAllViews(viewList, null);
    }

    public static void blinkAllViews(final List<View> viewList, Animator.AnimatorListener animatorListener)
    {
        List<Animator> animList = new ArrayList<Animator>();
        final float color = ISysConfig.COLOR_TO_BLINK;
        for (View view : viewList)
        {
            ObjectAnimator anim
                    = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), color, view.getAlpha(), color, view.getAlpha(), color, view.getAlpha(), color, view.getAlpha());
            animList.add(anim);
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.playTogether(animList);
        animSet.setDuration(ISysConfig.VALIDATION_ANIMATION_TIME);
        animSet.start();

        animSet.addListener(animatorListener);
    }

    public static Animator revealView(final View view)
    {
        if(view != null && view.getVisibility() != View.VISIBLE)
        {
            if(android.os.Build.VERSION.SDK_INT > 20)
            {
                try
                {
                    // get the center for the clipping circle
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.VISIBLE);
                        }
                    });

                    // make the view visible and start the animation
                    anim.start();
                    return anim;
                }
                catch(Exception ex)
                {
                    view.setVisibility(View.VISIBLE);
                }
            }
        else
            {
                view.setVisibility(View.VISIBLE);
            }
        }

        return null;
    }

    public static Animator hideView(final View view)
    {
        if(view != null && view.getVisibility() != View.INVISIBLE)
        {
            if(android.os.Build.VERSION.SDK_INT > 20)
            {
                try
                {
                    // get the center for the clipping circle
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;

                    // get the initial radius for the clipping circle
                    float initialRadius = (float) Math.hypot(cx, cy);

                    // create the animation (the final radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

                    // make the view invisible when the animation is done
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.INVISIBLE);
                        }
                    });

                    // start the animation
                    anim.start();
                    return anim;
                }
                catch(Exception ex)
                {
                    view.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                view.setVisibility(View.INVISIBLE);
            }
        }
        return null;
    }

    public static Animator removeView(final View view)
    {
        if(view != null && view.getVisibility() != View.GONE)
        {
            if(android.os.Build.VERSION.SDK_INT > 20)
            {
                try
                {
                    // get the center for the clipping circle
                    int cx = view.getWidth() / 2;
                    int cy = view.getHeight() / 2;

                    // get the initial radius for the clipping circle
                    float initialRadius = (float) Math.hypot(cx, cy);

                    // create the animation (the final radius is zero)
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

                    // make the view invisible when the animation is done
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.GONE);
                        }
                    });

                    // start the animation
                    anim.start();
                    return anim;
                }
                catch(Exception ex)
                {
                    view.setVisibility(View.GONE);
                }
            }
            else
            {
                view.setVisibility(View.GONE);
            }
        }
        return null;
    }

    public static void scaleViewOnOff(final View view, final WorkBetweenTwoActions inBetweenWorkListener) {
        Animation anim = new ScaleAnimation(
                1f, 0, // Start and end values for the X axis scaling
                1, 0, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(2000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(inBetweenWorkListener != null)
                {
                    inBetweenWorkListener.beforeStartingNext();
                }

                Animation anim = new ScaleAnimation(
                        0f, 1, // Start and end values for the X axis scaling
                        0, 1, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(2000);
                view.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    public static void fadeOutInView(final View view, final WorkBetweenTwoActions inBetweenWorkListener)
    {
        ObjectAnimator anim
                = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        anim.setDuration(1500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                if(inBetweenWorkListener != null)
                {
                    inBetweenWorkListener.beforeStartingNext();
                }

                ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                anim.setDuration(1500);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.start();
    }

    public interface WorkBetweenTwoActions
    {
        void beforeStartingNext();
    }
}