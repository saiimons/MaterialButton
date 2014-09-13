package com.keith.mb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

    private static final long AnimationDuration = 200;

    private static final int StateNormal = 1;
    private int state = StateNormal;
    private static final int StateTouchDown = 2;
    private static final int StateTouchUp = 3;
    private String mText;
    private Bitmap mBitmap;
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
    private long startTime;
    private Point touchPoint;
    private RectF buttonRectF;
    private Path clipPath;
    private int rippleRadius;

    private Paint mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

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
                Drawable drawable = attr.getDrawable(R.styleable.MaterialButton_icon);
                if (drawable != null) {
                    mBitmap = ((BitmapDrawable) drawable).getBitmap();
                }
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

    private Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        touchPoint.set(Math.round(event.getX()), Math.round(event.getY()));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                state = StateTouchDown;
                startTime = System.currentTimeMillis();
                mButtonPaint.setShadowLayer(10, 0, mPaddingTop + 4, mColorShadow);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                state = StateTouchUp;
                startTime = System.currentTimeMillis();
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
        if (state == StateTouchDown || state == StateTouchUp) {
            if (clipPath == null) {
                clipPath = new Path();
                clipPath.addRoundRect(buttonRectF, mRadius, mRadius, Path.Direction.CW);
            }
            canvas.clipPath(clipPath);
        }
        int radius = 0;
        long elapsed = System.currentTimeMillis() - startTime;
        switch (state) {
            case StateTouchDown: {
                mCirclePaint.setAlpha(255);
                if (elapsed < AnimationDuration) {
                    radius = Math.round(elapsed * getWidth() / 2 / AnimationDuration);
                    postInvalidate();
                } else {
                    radius = getWidth() / 2;
                }
                rippleRadius = radius;
            }
            break;
            case StateTouchUp: {
                if (elapsed < AnimationDuration) {
                    int alpha = Math.round((AnimationDuration - elapsed) * 255 / AnimationDuration);
                    mCirclePaint.setAlpha(alpha);
                    radius = rippleRadius + Math.round(elapsed * getWidth() / 2 / AnimationDuration);
                    postInvalidate();
                } else {
                    mCirclePaint.setAlpha(0);
                    radius = 0;
                    state = StateNormal;
                    postInvalidate();
                }
            }
            break;
            case StateNormal:
                radius = 0;
                break;
        }
        canvas.drawCircle(touchPoint.x, touchPoint.y, radius, mCirclePaint);
        // Draw button text
        canvas.restore();
        if (mBitmap != null) {
            float x = (getWidth() - mBitmap.getWidth()) / 2;
            float y = (getHeight() - mBitmap.getHeight()) / 2 - mPaddingBottom + mPaddingTop;
            canvas.drawBitmap(mBitmap, x, y, mIconPaint);
        } else {
            drawText(canvas);
        }
    }

    public void drawText(Canvas canvas) {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
        float y = (canvas.getHeight() - mTextPaint.ascent()) / 2 - mPaddingBottom;
        canvas.drawText(mText, canvas.getWidth() / 2, y, mTextPaint);
    }
}
