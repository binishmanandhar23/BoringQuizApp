package com.treeleaf.quiz.utils

import java.util.concurrent.TimeUnit

object TimerUtils {
    inline fun convertMillisToIndividualDigits(
        millis: Long,
        digits: (p0: Int, p1: Int, p2: Int, p3: Int) -> Unit
    ) {
        val p1 = millis / (60 * 1000)

        val onlySeconds =
            if (millis > TimeUnit.MINUTES.toMillis(1L)) millis - TimeUnit.MINUTES.toMillis(1L) else millis

        val p2 = (onlySeconds / 1000) / 10

        val p3 = (onlySeconds / 1000) % 10

        digits(0, p1.toInt(), p2.toInt(), p3.toInt())
    }
}