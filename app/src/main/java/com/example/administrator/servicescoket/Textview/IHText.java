package com.example.administrator.servicescoket.Textview;

import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface IHText {
    void init(HTextView hTextView, AttributeSet attrs, int defStyle);
    void animateText(CharSequence text);
    void onDraw(Canvas canvas);
    void reset(CharSequence text);
}
