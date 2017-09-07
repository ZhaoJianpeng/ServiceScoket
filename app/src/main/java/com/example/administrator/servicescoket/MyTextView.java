package com.example.administrator.servicescoket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/27.
 */

public class MyTextView extends TextView {
    //下面是实现动画需要的一些变量

    private int mViewWidth = 0;

    private Paint mPaint;

    private LinearGradient mLinearGradient;

    private Matrix mMatrix = null;

    private int mTranslate;


    public MyTextView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public MyTextView(Context context) {

        super(context, null);

    }

    @Override

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

/**

 * 这个方法在TextView大小的改变会被调用

 * int w 改变后的宽

 * int h 改变后的高

 * int oldw 改变前的宽

 * int oldh 改变前的高

 * 该方法里面只是为了初始花上面定义的成员变量

 */
        if (mViewWidth == 0) {

            mViewWidth = getMeasuredWidth();   //拿到TextView的宽

            if (mViewWidth > 0) {

                mPaint = getPaint();   //拿到画TextView的笔


//初始化渐变色给画笔用

/**

 * LinearGradient所需的

 * float x0, float y0, float x1, float y1, startColor, stopColor, TileMode tile

 * float x0, float y0, float x1, float y1, int[] colors, float[] positions, TileMode tile

 * float x0, float y0, float x1, float y1 在坐标上定了2个点,理解为在这个范围颜色渐变，渐变的颜色就是参数你设置的startColor, stopColor或则int[] colors

 * 这里没用到positions设为null就行

 */
                mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0, new int[]{Color.BLACK, 0xffffffff, Color.BLACK}, null, Shader.TileMode.CLAMP);

                mPaint.setShader(mLinearGradient);   //把你之前设置好渐变的颜色设置到上面初始画的笔中

                mMatrix = new Matrix();   //初始花颜色矩阵

            }

        }

    }

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
/**

 * 在onSizeChanged中初始化我们要的东西就可以开始画了

 */
        if (mMatrix != null) {

            mTranslate += mViewWidth / 5;

            if (mTranslate > 2 * mViewWidth) {

                mTranslate = -mViewWidth;

            }
/**

 * 画出动画的主要的就是下面的3个方法

 */
            mMatrix.setTranslate(mTranslate, 0);   //平移矩阵

            mLinearGradient.setLocalMatrix(mMatrix);   //把平移后的矩阵设置到颜色渐变中

            postInvalidateDelayed(150);   //最后一步就开始画了,这个方法会隔100毫秒画一次,但是每次颜色的矩阵是不同,这样你就会看到颜色动起来了

        }
    }


}
