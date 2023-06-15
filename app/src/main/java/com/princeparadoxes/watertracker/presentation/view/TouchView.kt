package com.princeparadoxes.watertracker.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class TouchView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return true
    }

}