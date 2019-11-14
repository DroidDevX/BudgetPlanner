package com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;


public class RotatedTextView extends AppCompatTextView {


    boolean topdown;

    public RotatedTextView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        final int gravity = getGravity();
        if(Gravity.isVertical(gravity)&& (gravity& Gravity.VERTICAL_GRAVITY_MASK)==Gravity.BOTTOM)
        {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)|Gravity.TOP);
            topdown=false;
        }
        else
            topdown = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(),getMeasuredWidth());
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        return super.setFrame(l, t, l+(b-t),t+(r-1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();
        canvas.save();

        if(topdown){
            canvas.translate(getWidth(),0);
            canvas.rotate(90);
        }
        else{
            canvas.translate(0,getHeight());
            canvas.rotate(-90);
        }
    }
}
