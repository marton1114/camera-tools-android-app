package com.example.phototools.startrailsapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.view.View;


public class DrawingTable extends View {

    Paint starPaint, trailPaint;

    float starRadius = 10.0f;
    float trailWidth = 10.0f;

    float width, height;

    float position1_x, position1_y;
    float position2_x, position2_y;
    float position3_x, position3_y;
    float position4_x, position4_y;
    float position5_x, position5_y;
    float position6_x, position6_y;
    float center_x, center_y;
    float radius1, radius2, radius3, radius4, radius5, radius6;


    public DrawingTable(Context context) {

        super(context);


        starPaint = new Paint();
        trailPaint = new Paint();
        starPaint.setColor(Color.WHITE);
        trailPaint.setColor(Color.BLUE);

        trailPaint.setStrokeWidth(trailWidth);

        trailPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        radius1 = (float)Math.sqrt(((center_x - position1_x) * (center_x - position1_x)) +
                ((center_y - position1_y) * (center_y - position1_y)));
        radius2 = (float)Math.sqrt(((center_x - position2_x) * (center_x - position2_x)) +
                ((center_y - position2_y) * (center_y - position2_y)));
        radius3 = (float)Math.sqrt(((center_x - position3_x) * (center_x - position3_x)) +
                ((center_y - position3_y) * (center_y - position3_y)));
        radius4 = (float)Math.sqrt(((center_x - position4_x) * (center_x - position4_x)) +
                ((center_y - position4_y) * (center_y - position4_y)));
        radius5 = (float)Math.sqrt(((center_x - position5_x) * (center_x - position5_x)) +
                ((center_y - position5_y) * (center_y - position5_y)));
        radius6 = (float)Math.sqrt(((center_x - position6_x) * (center_x - position6_x)) +
                ((center_y - position6_y) * (center_y - position6_y)));

        drawStarTrail(radius1, position1_x, position1_y, canvas);
        drawStarTrail(radius2, position2_x, position2_y, canvas);
        drawStarTrail(radius3, position3_x, position3_y, canvas);
        drawStarTrail(radius4, position4_x, position4_y, canvas);
        drawStarTrail(radius5, position5_x, position5_y, canvas);
        drawStarTrail(radius6, position6_x, position6_y, canvas);

        // csillagok rajtolása
        canvas.drawCircle(center_x, center_y, starRadius, starPaint);
        canvas.drawCircle(position1_x, position1_y, starRadius, starPaint);
        canvas.drawCircle(position2_x, position2_y, starRadius, starPaint);
        canvas.drawCircle(position3_x, position3_y, starRadius, starPaint);
        canvas.drawCircle(position4_x, position4_y, starRadius, starPaint);
        canvas.drawCircle(position5_x, position5_y, starRadius, starPaint);
        canvas.drawCircle(position6_x, position6_y, starRadius, starPaint);

        invalidate();
    }

    private void drawStarTrail(float radius, float position_x, float position_y, Canvas canvas) {
        Path path = new Path();
        path.addCircle(width / 2, height / 2, radius2, Path.Direction.CW);
        final RectF oval = new RectF();
        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);

        float startAngle = 0;
        if (getQuarter(position_x, position_y) == 0) {
            startAngle = ((float) Math.acos((Math.abs(center_x - position_x)) / radius)) * 57.296f;
        } else if (getQuarter(position_x, position_y) == 1) {
            startAngle = 90.0f + ((float) Math.asin((Math.abs(center_x - position_x)) / radius)) * 57.296f;
        } else if (getQuarter(position_x, position_y) == 2) {
            startAngle = 180.0f + ((float) Math.acos((Math.abs(center_x - position_x)) / radius)) * 57.296f;
        } else if (getQuarter(position_x, position_y) == 3) {
            startAngle = 270.0f + ((float) Math.asin((Math.abs(center_x - position_x)) / radius)) * 57.296f;
        }

        canvas.drawArc(oval, startAngle, StarTrailsAppActivity.angle, false, trailPaint);
    }

    private int getQuarter(float position_x, float position_y) {
        if (position_y < center_y && position_x > center_x) {
            return 3;
        } else if (position_y < center_y && position_x < center_x) {
            return  2;
        } else if (position_y > center_y && position_x < center_x) {
            return 1;
        } else if (position_y > center_y && position_x > center_x) {
            return 0;
        }
        return 0;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        width = (float) getWidth();
        height = (float) getHeight();

        center_x = width / 2;
        center_y = height / 2;

        position1_x = center_x - 200.0f;
        position1_y = center_y + 60.0f;

        position2_x = center_x - 400.0f;
        position2_y = center_y - 120.0f;

        position3_x = center_x + 350.0f;
        position3_y = center_y - 100.0f;

        position4_x = center_x;
        position4_y = center_y + 400.0f;

        position5_x = center_x + 60.0f;
        position5_y = center_y - 300.0f;

        position6_x = center_x + 180.0f;
        position6_y = center_y + 220.0f;

    }
}

