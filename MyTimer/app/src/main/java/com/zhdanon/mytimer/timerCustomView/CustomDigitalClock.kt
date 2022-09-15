package com.zhdanon.mytimer.timerCustomView

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.zhdanon.mytimer.R
import com.zhdanon.mytimer.TimeState

class CustomDigitalClock
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val tvHour: TextView
    private val tvMinute: TextView
    private val tvSecond: TextView

    private var timer = TimeState(0L, false)
        set(value) {
            if (value == field)
                return
            field = value
        }

    init {
        val root = inflate(context, R.layout.view_digital_timer, this)
        tvHour = root.findViewById(R.id.tv_hour)
        tvMinute = root.findViewById(R.id.tv_minute)
        tvSecond = root.findViewById(R.id.tv_second)
        tvHour.text = resources.getString(R.string.doubleZero)
        tvMinute.text = resources.getString(R.string.doubleZero)
        tvSecond.text = resources.getString(R.string.doubleZero)
    }

    fun setTime(timeState: TimeState) {
        val timeToSet = arrayListOf(0, 0, 0)
        timeToSet[0] = (timeState.time / (1000 * 3600)).toInt()
        timeToSet[1] = (timeState.time / (1000 * 60)).toInt()
        timeToSet[2] = (timeState.time / 1000).toInt()
        timer = timeState

        tvHour.text = when(timeToSet[0]) {
            in 0..9 -> resources.getString(R.string.tv_digital_time, timeToSet[0])
            in 10..24 -> timeToSet[0].toString()
            else -> if (timeToSet[0] % 24 < 10) resources.getString(
                R.string.tv_digital_time,
                timeToSet[0]
            ) else (timeToSet[0] % 24).toString()
        }
        tvMinute.text = when(timeToSet[1]) {
            in 0..9 -> resources.getString(R.string.tv_digital_time, timeToSet[1])
            in 10..60 -> timeToSet[1].toString()
            else -> if (timeToSet[1] % 60 < 10) resources.getString(
                R.string.tv_digital_time,
                timeToSet[1]
            ) else (timeToSet[1] % 60).toString()
        }
        tvSecond.text = when(timeToSet[2]) {
            in 0..9 -> resources.getString(R.string.tv_digital_time, timeToSet[2])
            in 10..59 -> timeToSet[2].toString()
            else -> if (timeToSet[2] % 60 < 10) resources.getString(
                R.string.tv_digital_time,
                timeToSet[2] % 60
            ) else (timeToSet[2] % 60).toString()
        }
        invalidate()
    }

    fun reset() {
        timer.apply {
            this.time = 0L
            this.isPlayed = false
        }
        setTime(timer)
    }
}