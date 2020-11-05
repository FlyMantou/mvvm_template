package com.app.hubert.guide.core;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.app.hubert.guide.listener.AnimationListenerAdapter;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;

import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class GuideLayout extends RelativeLayout {

    public static final int DEFAULT_BACKGROUND_COLOR = 0xb2000000;

    private Paint mPaint;
    public GuidePage guidePage;
    private OnGuideLayoutDismissListener listener;

    public GuideLayout(Context context) {
        this(context, null);
    }

    public GuideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(xfermode);

        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        setClickable(true);

        //ViewGroup默认设定为true，会使onDraw方法不执行，如果复写了onDraw(Canvas)方法，需要清除此标记
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int backgroundColor = guidePage.getBackgroundColor();
        canvas.drawColor(backgroundColor == 0 ? DEFAULT_BACKGROUND_COLOR : backgroundColor);
        drawHightLights(canvas);
    }

    private void drawHightLights(Canvas canvas) {
        List<HighLight> highLights = guidePage.getHighLights();
        if (highLights != null) {
            for (HighLight highLight : highLights) {
                RectF rectF = highLight.getRectF((View) getParent());
                switch (highLight.getShape()) {
                    case CIRCLE:
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), highLight.getRadius(), mPaint);
                        break;
                    case OVAL:
                        canvas.drawOval(rectF, mPaint);
                        break;
                    case ROUND_RECTANGLE:
                        canvas.drawRoundRect(rectF, highLight.getRound(), highLight.getRound(), mPaint);
                        break;
                    case NONE:
                        break;
                    case RECTANGLE:
                    default:
                        canvas.drawRect(rectF, mPaint);
                        break;
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Animation enterAnimation = guidePage.getEnterAnimation();
        if (enterAnimation != null) {
            startAnimation(enterAnimation);
        }
    }

    public void setGuidePage(GuidePage page) {
        this.guidePage = page;
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guidePage.isEverywhereCancelable()) {
                    remove();
                }
            }
        });
    }

    public void setOnGuideLayoutDismissListener(OnGuideLayoutDismissListener listener) {
        this.listener = listener;
    }

    public void remove() {
        Animation exitAnimation = guidePage.getExitAnimation();
        if (exitAnimation != null) {
            exitAnimation.setAnimationListener(new AnimationListenerAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    dismiss();
                }
            });
            startAnimation(exitAnimation);
        } else {
            dismiss();
        }
    }

    private void dismiss() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
            if (listener != null) {
                listener.onGuideLayoutDismiss(this);
            }
        }
    }

    public interface OnGuideLayoutDismissListener {
        void onGuideLayoutDismiss(GuideLayout guideLayout);
    }

}
