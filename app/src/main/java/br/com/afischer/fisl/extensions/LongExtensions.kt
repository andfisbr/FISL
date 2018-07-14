package br.com.afischer.fisl.extensions

import android.text.format.DateFormat
import br.com.afischer.fisl.util.Consts
import java.util.*



fun Long.asDateString(format: String = Consts.DATE_FORMAT): String = DateFormat.format( format, this ).toString()
fun Long.asDate(format: String = Consts.DATE_FORMAT): Date = DateFormat.format( format, this ).toString().asDate(format)
fun Long.asFullDate(format: String = Consts.FULL_DATE_FORMAT): Date = this.asDate(format)

fun Long.pad(format: String = "<0"): String {
        val length = format.length - 1
        val char = format[1]
        val direction = format[0]
        
        return when (direction) {
                '>' -> this.toString().padEnd(length, char)
                '<' -> this.toString().padStart(length, char)
                else -> this.toString().padStart(length, char)
        }
}