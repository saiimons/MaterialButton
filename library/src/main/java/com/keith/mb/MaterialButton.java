package com.keith.mb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by keith on 14-9-9.
 * com.keith.mb
 */
public class MaterialButton extends View {

    private static final String TAG = MaterialButton.class.getSimpleName();

    private static final int AnimationDuration = 300;

    private String mText;

    private int mTextColor;

    private int mTextSize;

    private int mRadius;

    private int mColorNormal;

    private int mColorShadow;

    private int mColorRipple;

    private int mPaddingLeft;

    private int mPaddingRight;

    private int mPaddingTop;

    private int mPaddingBottom;

    private Point touchPoint;

    private Paint mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private RectF buttonRectF;

    private Path clipPath;

    private boolean isPressed = false;

    public MaterialButton(Context context) {
        this(context, null);
    }

    public MaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        touchPoint = new Point();
        mText = getString(R.string.button_text);
        mTextColor = getColor(R.color.text_color);
        mTextSize = getDimension(R.dimen.text_size);
        mRadius = getDimension(R.dimen.corner_radius);
        mColorNormal = getColor(R.color.color_normal);
        mColorShadow = getColor(R.color.color_shadow);
        mColorRipple = getColor(R.color.color_ripple);
        mPaddingLeft = getDimension(R.dimen.padding_left);
        mPaddingRight = getDimension(R.dimen.padding_right);
        mPaddingTop = getDimension(R.dimen.padding_top);
        mPaddingBottom = getDimension(R.dimen.padding_bottom);
        if (attributeSet != null) {
            initAttributes(context, attributeSet);
        }
        mButtonPaint.setColor(mColorNormal);
        mButtonPaint.setShadowLayer(5, 0, Math.round(mPaddingTop * 0.8), mColorShadow);
        setLayerType(LAYER_TYPE_SOFTWARE, mButtonPaint);

        mCirclePaint.setColor(mColorRipple);

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(TextPaint.Align.CENTER);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, R.styleable.MaterialButton);
        if (attr != null) {
            try {
                mText = attr.getString(R.styleable.MaterialButton_text);
                if (mText == null) {
                    mText = getString(R.string.button_text);
                }
                mTextSize = attr.getDimensionPixelSize(R.styleable.MaterialButton_text_size,
                        getDimension(R.dimen.text_size));
                mTextColor = attr.getColor(R.styleable.MaterialButton_text_color,
                        getColor(R.color.text_color));
                mColorNormal = attr.getColor(R.styleable.MaterialButton_color_normal,
                        getColor(R.color.color_normal));
                mColorShadow = attr.getColor(R.styleable.MaterialButton_color_shadow,
                        getColor(R.color.color_shadow));
                mColorRipple = attr.getColor(R.styleable.MaterialButton_color_ripple,
                        getColor(R.color.color_ripple));
                mRadius = attr.getDimensionPixelSize(R.styleable.MaterialButton_corner_radius,
                        getDimension(R.dimen.corner_radius));
            } finally {
                attr.recycle();
            }
        }
    }

    private String getString(int id) {
        return getResources().getString(id);
    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }

    private int getDimension(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        touchPoint.set(Math.round(event.getX()), Math.round(event.getY()));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                mButtonPaint.setShadowLayer(10, 0, mPaddingTop + 4, mColorShadow);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                mButtonPaint.setShadowLayer(5, 0, Math.round(mPaddingTop * 0.8), mColorShadow);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (buttonRectF == null) {
            buttonRectF = new RectF();
            buttonRectF.left = mPaddingLeft;
            buttonRectF.top = mPaddingTop;
            buttonRectF.right = getWidth() - mPaddingLeft - mPaddingRight;
            buttonRectF.bottom = getHeight() - mPaddingTop - mPaddingBottom;
        }
        // Draw button background
        canvas.drawRoundRect(buttonRectF, mRadius, mRadius, mButtonPaint);
        canvas.save();
        // Draw button ripple
        if (isPressed) {
            if (clipPath == null) {
                clipPath = new Path();
                clipPath.addRoundRect(buttonRectF, mRadius, mRadius, Path.Direction.CW);
            }
            canvas.clipPath(clipPath);
            canvas.drawCircle(touchPoint.x, touchPoint.y, getWidth() / 2, mCirclePaint);
        }
        // Draw button text
        canvas.restore();
        drawText(canvas);
    }

    public void drawText(Canvas canvas) {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
        float y = (canvas.getHeight() - mTextPaint.ascent()) / 2 - mPaddingBottom;
        canvas.drawText(mText, canvas.getWidth() / 2, y, mTextPaint);
    }
}
