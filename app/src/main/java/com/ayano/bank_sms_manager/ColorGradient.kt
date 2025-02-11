package com.ayano.bank_sms_manager

import androidx.compose.ui.graphics.Color

class ColorGradient {
    companion object {


        fun pointBetweenColors(from: Color, to: Color, percent: Float) = Color(
            red = pointBetweenColors(from.red, to.red, percent),
            green = pointBetweenColors(from.green, to.green, percent),
            blue = pointBetweenColors(from.blue, to.blue, percent),
            alpha = pointBetweenColors(from.alpha, to.alpha, percent),
        )

        private fun pointBetweenColors(from: Float, to: Float, percent: Float): Float =
            from + percent * (to - from)
    }

}