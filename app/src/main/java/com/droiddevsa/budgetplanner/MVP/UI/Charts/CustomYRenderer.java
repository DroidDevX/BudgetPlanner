package com.droiddevsa.budgetplanner.MVP.UI.Charts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class CustomYRenderer extends YAxisRenderer {
    public CustomYRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }

    @Override
    public void renderLimitLines(Canvas c) {

        List<LimitLine> limitLines = mYAxis.getLimitLines();

        if (limitLines == null || limitLines.size() <= 0)
            return;

        float[] pts = mRenderLimitLinesBuffer;
        pts[0] = 0;
        pts[1] = 0;
        Path limitLinePath = mRenderLimitLines;
        limitLinePath.reset();

        for (int i = 0; i < limitLines.size(); i++) {

            LimitLine l = limitLines.get(i);

            if (!l.isEnabled())
                continue;

            int clipRestoreCount = c.save();
            mLimitLineClippingRect.set(mViewPortHandler.getContentRect());
            mLimitLineClippingRect.inset(0.f, -l.getLineWidth());
            c.clipRect(mLimitLineClippingRect);

            mLimitLinePaint.setStyle(Paint.Style.STROKE);
            mLimitLinePaint.setColor(l.getLineColor());
            mLimitLinePaint.setStrokeWidth(l.getLineWidth());
            mLimitLinePaint.setPathEffect(l.getDashPathEffect());

            pts[1] = l.getLimit();

            mTrans.pointValuesToPixel(pts);

            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1]);
            limitLinePath.lineTo(mViewPortHandler.contentRight(), pts[1]);

            c.drawPath(limitLinePath, mLimitLinePaint);
            limitLinePath.reset();
            // c.drawLines(pts, mLimitLinePaint);


            c.restoreToCount(clipRestoreCount);
        }

    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        super.drawYLabels(c, fixedPosition, positions, offset);

        List<LimitLine> limitlines = mYAxis.getLimitLines();

        float[] pts = new float[2];
        for(LimitLine line: limitlines){


            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(line.getLineColor());

            Paint textPaint = mAxisLabelPaint;
            textPaint.setColor(line.getTextColor());
            textPaint.setTextSize(mAxisLabelPaint.getTextSize());
            textPaint.setPathEffect(null);
            textPaint.setTypeface(line.getTypeface());
            textPaint.setStrokeWidth(0.5f);
            textPaint.setStyle(line.getTextStyle());
            pts[1] = line.getLimit();
            mTrans.pointValuesToPixel(pts);

            //Label format
            float paddingVert = Utils.convertDpToPixel(4);
            float paddingLeft = Utils.convertDpToPixel(4);
            float height = Utils.calcTextHeight(textPaint,line.getLabel());
            float width = Utils.calcTextWidth(textPaint,line.getLabel());
            float posY = pts[1] + height/2;

            //**
            c.drawRect(fixedPosition-paddingLeft, posY+height,fixedPosition+width*2,posY-height-paddingVert,paint);
            c.drawText(line.getLabel(),fixedPosition,posY,textPaint);
        }


    }
}
