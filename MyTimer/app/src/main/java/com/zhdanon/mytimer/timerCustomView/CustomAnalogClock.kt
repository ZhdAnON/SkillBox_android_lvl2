package com.zhdanon.mytimer.timerCustomView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.zhdanon.mytimer.TimeState
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CustomAnalogClock : View {
    private val timerListeners = mutableSetOf<(TimeState) -> Unit>()

    // My CoroutineScope
    private val myScope = CoroutineScope(Job() + Dispatchers.Default)

    var timer = TimeState(0L, false)
        set(value) {
            if (value == field) return
            field = value
            timerListeners.forEach { it(value) }
        }

    private val mClockHoursMap = mapOf(
        1 to "I",
        2 to "II",
        3 to "III",
        4 to "IV",
        5 to "V",
        6 to "VI",
        7 to "VII",
        8 to "VIII",
        9 to "IX",
        10 to "X",
        11 to "XI",
        12 to "XII"
    )

    private var mHeight = 0
    private var mWight = 0

    private var mPadding = 0
    private val mNumeralSpacing = 0

    private var mHandTruncation = 0
    private var mHourHandTruncation = 0

    private var mRadius = 0
    private lateinit var mPaint: Paint
    private var mRect = Rect()
    private var isInit = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int) : super(
        context,
        attrs,
        defStyleAttrs
    )

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (!isInit) {
            mPaint = Paint()
            mHeight = height
            mWight = width
            mPadding = mNumeralSpacing + 50
            val minAttr = min(mHeight, mWight)
            mRadius = minAttr / 2 - mPadding

            mHandTruncation = minAttr / 20
            mHourHandTruncation = minAttr / 17

            isInit = true
        }
        canvas?.drawColor(Color.DKGRAY)

        mPaint.reset()
        mPaint.apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        }
        canvas?.drawCircle(
            (mWight / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mRadius + mPadding - 10).toFloat(), mPaint
        )

        val fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
        mPaint.textSize = fontSize

        for (hour in mClockHoursMap.keys) {
            val tmp = mClockHoursMap[hour]
            mPaint.getTextBounds(tmp, 0, tmp!!.length, mRect)

            val angle = Math.PI / 6 * (hour - 3)
            val x = (mWight / 2 + cos(angle) * mRadius - mRect.width() / 2)
            val y = (mHeight / 2 + sin(angle) * mRadius + mRect.height() / 2)

            canvas?.drawText(tmp, x.toFloat(), y.toFloat(), mPaint)
        }

        var hour = ((timer.time) / (1000 * 3600)).toInt()
        hour = if (hour > 12) hour - 12 else hour

        drawLine(
            canvas!!,
            (hour + (timer.time / (1000 * 60)) / 60) * 5f,
            isHour = true,
            isSecond = false
        )
        drawLine(
            canvas,
            (timer.time / (1000 * 60)).toFloat(),
            isHour = false,
            isSecond = false
        )
        drawLine(
            canvas,
            (timer.time / 1000).toFloat(),
            isHour = false,
            isSecond = true
        )
    }

    private fun drawLine(canvas: Canvas, moment: Float, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * moment / 30 - Math.PI / 2
        val handRadius =
            if (isHour) mRadius - mHandTruncation - mHourHandTruncation else mRadius - mHandTruncation
        if (isSecond) mPaint.color = Color.YELLOW
        canvas.drawLine(
            (mWight / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mWight / 2 + cos(angle) * handRadius).toFloat(),
            (mHeight / 2 + sin(angle) * handRadius).toFloat(),
            mPaint
        )
    }

    fun start() {
        timer.isPlayed = true

        // Timer coroutine
        val timerProcess = myScope.launch(Dispatchers.Main, start = CoroutineStart.LAZY) {
            val startTime = currentTime()
            while (true) {
                delay(998)
                timer = TimeState(currentTime() - startTime, true)
                invalidate()
            }
        }

        // TimerState-coroutine
        myScope.launch {
            while (true) {
                if (!timer.isPlayed) {
                    timerProcess.cancel()
                } else {
                    timerProcess.start()
                }
            }
        }
    }

    fun stop() { timer.isPlayed = false }

    fun reset() {
        timer.apply {
            this.time = 0L
            this.isPlayed = false
        }
        invalidate()
    }

    private fun currentTime(): Long {
        return if (timer.isPlayed) System.currentTimeMillis() else 0L
    }

    fun addUpdateListener(listener: (TimeState) -> Unit) {
        timerListeners.add(listener)
        listener(timer)
    }
}