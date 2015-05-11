package lehman.android;


import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Matrix;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by Krirk on 3/27/2015.
 */
public class MyView extends View {

    private final String TAG = getClass().getSimpleName();
    private final boolean D = Log.isLoggable(TAG, Log.DEBUG);





    private Paint mPaint = new Paint();
    public static Bitmap fan;
    private Bitmap stand;
    private int mDegrees = 0;
    private int mDesiredWidth=300;
    private int mDesiredHeight=300;


    public MyView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);

    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleAttr);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mDegrees = 0;
        stand = BitmapFactory.decodeResource(getResources(), R.drawable.pole);
        fan = BitmapFactory.decodeResource(getResources(), R.drawable.fan2);



    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        int x = 30;
        int y = 0;

        canvas.drawBitmap(stand, x, fan.getHeight()/2, mPaint);
        canvas.drawBitmap(fan, rotate(fan, x, y), mPaint);


        mDegrees = mDegrees + Lab2Fragment.btnVal;

        if (mDegrees >= 360) {
            mDegrees = 0;
        }

        invalidate();

    }

    public Matrix rotate(Bitmap img, int x, int y) {
        Matrix mtx = new Matrix();
        mtx.postRotate(mDegrees, img.getWidth() / 2, img.getHeight() / 2);
        mtx.postTranslate(x, y);
        return mtx;
    }
/*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (D) {
            Log.v(TAG, "onMeasure:widthMeasureSpec: " + widthMeasureSpec
                    + " heightMeasureSpec: " + heightMeasureSpec);
        }
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            int width;
            int height;

            //Measure Width
            if (widthMode == MeasureSpec.EXACTLY) {
                //Must be this size
                width = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                //Can't be bigger than...
                width = Math.min(mDesiredWidth, widthSize);
            } else {
                //Be whatever you want
                width = mDesiredWidth;
            }

            //Measure Height
            if (heightMode == MeasureSpec.EXACTLY) {
                //Must be this size
                height = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                //Can't be bigger than...
                height = Math.min(mDesiredHeight, heightSize);
            } else {
                //Be whatever you want
                height = mDesiredHeight;
            }
            if (D) { Log.v(TAG, "width: " + width + ", height: " + height); }
                setMeasuredDimension(width, height);
            }
*/
        }
