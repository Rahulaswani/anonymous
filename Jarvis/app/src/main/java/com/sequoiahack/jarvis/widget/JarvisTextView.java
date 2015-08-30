package com.sequoiahack.jarvis.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sequoiahack.jarvis.R;

public class JarvisTextView extends TextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 500; //Default 500ms delay

    public JarvisTextView(Context context) {
        super(context);
    }

    public JarvisTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.JarvisTypeface);

        String fontName = styledAttrs.getString(R.styleable.JarvisTypeface_typeface);

        styledAttrs.recycle();
        if (fontName != null) {
            Typeface typeface = TypefaceCache.getInstance().getTypeface(context.getAssets(),
                    fontName);
            this.setTextColor(getResources().getColor(R.color.jarvis_white));
            this.setTypeface(typeface, typeface.getStyle());
        }
    }

    public JarvisTextView(Context context, String fontName) {
        super(context);
        if (fontName != null) {
            Typeface typeface = TypefaceCache.getInstance().getTypeface(context.getAssets(),
                    fontName);
            this.setTypeface(typeface, typeface.getStyle());
        }
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(tf, style);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

}
