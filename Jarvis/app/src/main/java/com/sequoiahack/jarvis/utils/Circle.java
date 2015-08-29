package com.sequoiahack.jarvis.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class Circle extends View {
    private float radius = 0;
    private Paint paintFill;
    private Paint paintStroke;
    public Circle(Context context, float radius, int colorFill) {
        super(context);
        this.radius = radius;
        paintFill = new Paint();
        paintFill.setColor(colorFill);
        paintFill.setAntiAlias(true);
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


    public void setColorFill(int colorFill) {
        paintFill.setColor(colorFill);
    }

    public void setColorStroke(int colorStroke) {
        if (paintStroke == null) {
            paintStroke = new Paint();
            paintStroke.setStrokeWidth(10);
            paintStroke.setStyle(Paint.Style.STROKE);
            paintStroke.setAntiAlias(true);
        }
        paintStroke.setColor(colorStroke);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
//		canvas.drawCircle(width / 2, height / 2, radius, paintFill);

        if (paintStroke != null) {
            canvas.drawCircle(width / 2, height / 2, radius, paintStroke);
        }

        super.onDraw(canvas);
    }
}
