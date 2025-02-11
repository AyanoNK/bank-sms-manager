package com.ayano.bank_sms_manager.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import com.ayano.bank_sms_manager.R


@Composable
fun GlanceText(
    text: String,
//    @FontRes font: Int,
    fontSize: TextUnit,
    modifier: GlanceModifier = GlanceModifier,
    color: Color = Color.Black,
    letterSpacing: TextUnit = 0.sp,
) {
    Image(
        modifier = modifier,
        provider = ImageProvider(
            LocalContext.current.textAsBitmap(
                text = text,
                fontSize = fontSize,
                color = color,
                letterSpacing = letterSpacing.value,
            )
        ),
        contentDescription = null,
    )
}

private fun Context.textAsBitmap(
    text: String,
    fontSize: TextUnit,
    color: Color = Color.Black,
    letterSpacing: Float = 0.1f,

    ): Bitmap {
    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    paint.textSize = spToPx(fontSize.value, this)
    paint.color = color.toArgb()
    paint.textAlign = Paint.Align.LEFT
    paint.letterSpacing = letterSpacing


    val fontResource = ResourcesCompat.getFont(this, R.font.mulish_semibold);
    paint.typeface = fontResource
    val baseline = -paint.ascent()
    val width = (paint.measureText(text)).toInt()
    val height = (baseline + paint.descent()).toInt()
    val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(image)
    canvas.drawText(text, 0f, baseline, paint)
    return image
}

private fun spToPx(sp: Float, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics
    )
}