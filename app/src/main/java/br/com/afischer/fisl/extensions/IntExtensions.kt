package br.com.afischer.fisl.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import java.util.*


fun Int.asDrawable(ctx: Context): Drawable? = ResourcesCompat.getDrawable(ctx.resources, this, null)
fun Int.asColor(ctx: Context): Int = ResourcesCompat.getColor(ctx.resources, this, null)
fun Int.asString(ctx: Context): String = ctx.getString(this)




fun Int.pxToDp(): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = this * (metrics.densityDpi / 160f)
        return Math.round(px)
}

fun Int.dpToPx(): Int = Math.round(this * (Resources.getSystem().displayMetrics.xdpi/ 160f))


fun Int.sec(): Int = this * 1000
fun Int.min(): Int = this * 60 * 1000
fun Int.h(): Int = this * 60 * 60 * 1000


fun Int.minusPercent(value: Int): Double = (this * (1 - (value / 100))).toDouble()
fun Int.plusPercent(value: Int): Double = (this * (1 + (value / 100))).toDouble()
fun Int.percent(value: Int): Double = (this * value / 100).toDouble()


fun Int.pad(format: String = "<0"): String {
        val length = format.length - 1
        val char = format[1]
        val direction = format[0]
        
        return when (direction) {
                '>' -> this.toString().padEnd(length, char)
                '<' -> this.toString().padStart(length, char)
                else -> this.toString().padStart(length, char)
        }
}



fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) + start