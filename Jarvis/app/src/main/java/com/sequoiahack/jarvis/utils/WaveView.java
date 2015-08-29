package com.sequoiahack.jarvis.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.sequoiahack.jarvis.R;

public class WaveView extends ViewGroup {
    private VolumeCallBack volumeCallback = null;

    private int colorVoiceWave = 0x70B1F3FF;
    private int colorVoiceWaveStroke = 0x2B1F3FF;
    private int colorVoiceWaveSurround = 0xfCDB3FA;

    private boolean isRun = false;
    private int width = 0;
    private int height = 0;

    private Bitmap iconBitmap = null;
    private Paint voicePaint = null;
    private int voiceRaduis = 0;

    private Circle firstCircle = null;
    private float firstFrom = 1.0f, firstTo = 1.0f;

    private Circle secondCircle = null;
    private float secondFrom = 1.0f, secondTo = 1.0f;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void startAnimation() {
        invalidate();

        firstFrom = firstTo = 1.0f;
        secondFrom = secondTo = 1.0f;

        isRun = true;
        post(timer);
    }

    public void stopAnimation() {
        isRun = false;
        firstCircle.clearAnimation();
        secondCircle.clearAnimation();
    }

    public void setVolumeCallback(VolumeCallBack callback) {
        volumeCallback = callback;
    }

    public void setVoiceCircleColor(int colorVoiceWave, int colorVoiceWaveStroke) {
        this.colorVoiceWave = colorVoiceWave;
        //	this.colorVoiceWaveStroke = colorVoiceWaveStroke;
        if (firstCircle != null) {
            //	firstCircle.setColorFill(colorVoiceWave);
            firstCircle.setColorStroke(colorVoiceWaveStroke);
        }
    }

    public static interface VolumeCallBack {
        public int getAmplitude();
    }

    private void init() {
        setWillNotDraw(false);
        float density = getContext().getResources().getDisplayMetrics().density;
        firstCircle = new Circle(getContext(), 50 * density + 0.5f,
                colorVoiceWave);
        firstCircle.setColorStroke(colorVoiceWaveStroke);
        addView(firstCircle);
        secondCircle = new Circle(getContext(), 50 * density + 0.5f,
                colorVoiceWaveSurround);
        secondCircle.setColorStroke(colorVoiceWaveSurround);
        // addView(secondCircle);
        initParticles();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0, j = getChildCount(); i < j; i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            child.measure(
                    MeasureSpec.makeMeasureSpec(r - l, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(b - t, MeasureSpec.AT_MOST));
            child.layout(0, 0, child.getMeasuredWidth(),
                    child.getMeasuredHeight());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        width = getWidth();
        height = getHeight();

        // particle
        for (int i = 0; i < particlesNum; i++) {
            Dot dot = dots[i];
            Paint paint = paints[i];
            dot.positionX = width / 2;
            dot.positionY = height / 2;
            // 变化
            dot.color = colorVoiceWaveSurround;
            paint.setColor(dot.color);
            dot.alpha *= dot.alphaDecreaseRate;
            dot.radius += dot.velocityRadius;
            paint.setAlpha(dot.alpha > 5 ? dot.alpha : 0);
            if (dot.alpha == 0) {
                dot.radius = 0;
                dot.alpha = (int) (255 * 0.6f);
            }
            canvas.drawCircle(dot.positionX, dot.positionY, dot.radius, paint);
        }

        //
        if (null == voicePaint) {
            voicePaint = new Paint();
            //	int colorCenter = R.color.voilet;
            //	voicePaint.setColor(getResources().getColor(colorCenter));
            voicePaint.setAntiAlias(true);
            voiceRaduis = (int) (35 * getContext().getResources()
                    .getDisplayMetrics().density + 0.5f);
        }
        canvas.drawCircle(width / 2, height / 2, voiceRaduis, voicePaint);

        //
        if (null == iconBitmap) {
            iconBitmap = BitmapFactory.decodeResource(getContext()
                    .getResources(), R.drawable.eye);
        }
        Rect destRect = new Rect();
        float cx = width / 2;
        float cy = height / 2;
        destRect.left = (int) (cx - iconBitmap.getWidth() * 0.5f);
        destRect.top = (int) (cy - iconBitmap.getHeight() * 0.5f);
        destRect.right = (int) (cx + iconBitmap.getWidth() * 0.5f);
        destRect.bottom = (int) (cy + iconBitmap.getHeight() * 0.5f);
        canvas.drawBitmap(iconBitmap, null, destRect, null);

    }

    private class Dot {
        public float radius;
        public float velocityRadius;
        public float positionX;
        public float positionY;
        public int color;
        public int strokeWidth;
        public int alpha; // [0..255]
        public float alphaDecreaseRate;
    }

    private int particlesNum = 1;
    private Dot[] dots = new Dot[particlesNum];
    private Paint[] paints = new Paint[particlesNum];

    private void initParticles() {
        for (int i = 0; i < particlesNum; i++) {
            Dot dot = new Dot();
            dot.radius = 0;
            dot.velocityRadius = 4f;
            dot.alphaDecreaseRate = 0.97f;
            dot.color = colorVoiceWaveSurround;
            dot.alpha = (int) (255 * 0.6f);
            dot.strokeWidth = 7;
            dots[i] = dot;

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(dot.color);
            paint.setAlpha(dot.alpha);
            //	paint.setStrokeWidth(dot.strokeWidth);
            paint.setStrokeWidth(7);
            paint.setAntiAlias(true);
            paints[i] = paint;
        }
    }

    private int animateTime = 50;
    private int countDownStep = 0;
    private int countDownStepMax = 500 / animateTime;

    private Runnable timer = new Runnable() {
        public void run() {
            if (!isRun) {
                return;
            }

            int ampl = 20;
            int amp = volumeCallback.getAmplitude();
            if (amp > 0) {
                float volume = (float) (2.0f * Math.log10(amp / ampl) / 10.0f);
                firstTo = 0.8f + volume * 3.0f;
            }
            ScaleAnimation animation;
            // 主动圆
            animation = new ScaleAnimation(firstFrom, firstTo, firstFrom,
                    firstTo, width / 2, height / 2);
            animation.setDuration(animateTime);
            animation.setFillAfter(true);
            animation.setInterpolator(AnimationUtils.loadInterpolator(
                    getContext(), R.anim.linear_interpolator));
            firstCircle.startAnimation(animation);
            firstFrom = firstTo;

            // 被动圆
            boolean animateFlag = false;
            if (firstTo > secondTo) {
                animateFlag = true;
                secondTo = (float) (firstTo);
            } else {
                countDownStep++;
                if (countDownStep > countDownStepMax) {
                    countDownStep = 0;
                    animateFlag = true;
                    secondTo = firstTo;
                }
            }
            if (animateFlag) {
                animation = new ScaleAnimation(secondFrom, secondTo,
                        secondFrom, secondTo, width / 2, height / 2);
                animation.setDuration(animateTime);
                animation.setFillAfter(true);
                animation.setInterpolator(AnimationUtils.loadInterpolator(
                        getContext(), android.R.anim.accelerate_interpolator));
                secondCircle.startAnimation(animation);
                secondFrom = secondTo;
            }

            postDelayed(this, animateTime);
        }
    };

    public void setVoiceSurroundColor(int color) {
        colorVoiceWaveSurround = color;
    }
}