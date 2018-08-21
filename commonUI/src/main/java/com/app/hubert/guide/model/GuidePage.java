package com.app.hubert.guide.model;

import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.animation.Animation;

import com.app.hubert.guide.listener.OnLayoutInflatedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/11/16.
 */

public class GuidePage {

    private List<HighLight> highLights = new ArrayList<>();
    private boolean everywhereCancelable = true;
    private int backgroundColor;
    private int layoutResId;
    private int[] clickToDismissIds;
    private OnLayoutInflatedListener onLayoutInflatedListener;
    private Animation enterAnimation, exitAnimation;

    public static GuidePage newInstance() {
        return new GuidePage();
    }

    public GuidePage addHighLight(View view) {
        return addHighLight(view, HighLight.Shape.RECTANGLE, 0, 0);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape) {
        return addHighLight(view, shape, 0, 0);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape, int padding) {
        return addHighLight(view, shape, 0, padding);
    }

    /**
     * 添加需要高亮的view
     *
     * @param view    需要高亮的view
     * @param shape   高亮类型：圆形，椭圆，矩形，圆角矩形
     * @param round   圆角尺寸，单位dp
     * @param padding 高亮相对view的padding,单位px
     */
    public GuidePage addHighLight(View view, HighLight.Shape shape, int round, int padding) {
        highLights.add(
                HighLight.newInstance(view)
                        .setShape(shape)
                        .setRound(round)
                        .setPadding(padding));
        return this;
    }

    public GuidePage addHighLight(HighLight... highLights) {
        this.highLights.addAll(Arrays.asList(highLights));
        return this;
    }

    public GuidePage addHighLight(List<HighLight> list) {
        highLights.addAll(list);
        return this;
    }

    /**
     * 添加引导层布局
     *
     * @param resId 布局id
     * @param id    布局中点击消失引导页的控件id
     */
    public GuidePage setLayoutRes(@LayoutRes int resId, int... id) {
        this.layoutResId = resId;
        clickToDismissIds = id;
        return this;
    }

    public GuidePage setEverywhereCancelable(boolean everywhereCancelable) {
        this.everywhereCancelable = everywhereCancelable;
        return this;
    }

    /**
     * 设置背景色
     */
    public GuidePage setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * 设置自定义layout填充监听，用于自定义layout初始化
     *
     * @param onLayoutInflatedListener listener
     */
    public GuidePage setOnLayoutInflatedListener(OnLayoutInflatedListener onLayoutInflatedListener) {
        this.onLayoutInflatedListener = onLayoutInflatedListener;
        return this;
    }

    /**
     * 设置进入动画
     */
    public GuidePage setEnterAnimation(Animation enterAnimation) {
        this.enterAnimation = enterAnimation;
        return this;
    }

    /**
     * 设置退出动画
     */
    public GuidePage setExitAnimation(Animation exitAnimation) {
        this.exitAnimation = exitAnimation;
        return this;
    }

    public boolean isEverywhereCancelable() {
        return everywhereCancelable;
    }

    public boolean isEmpty() {
        return layoutResId == 0 && highLights.size() == 0;
    }

    public List<HighLight> getHighLights() {
        return highLights;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public int[] getClickToDismissIds() {
        return clickToDismissIds;
    }

    public OnLayoutInflatedListener getOnLayoutInflatedListener() {
        return onLayoutInflatedListener;
    }

    public Animation getEnterAnimation() {
        return enterAnimation;
    }

    public Animation getExitAnimation() {
        return exitAnimation;
    }
}
