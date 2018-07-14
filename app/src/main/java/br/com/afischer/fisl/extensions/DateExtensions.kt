package br.com.afischer.fisl.extensions

import br.com.afischer.fisl.util.Consts
import java.text.SimpleDateFormat
import java.util.*


/**
 * Date extensions
 */
internal val calendar: Calendar by lazy {
        Calendar.getInstance()
}


fun Date.asString(format: String = Consts.DATE_FORMAT): String = SimpleDateFormat(format, Consts.ptBR).format(this)

fun Date.asLong(): Long = this.time
fun Date.asMillis(): Long = this.asLong()
fun Date.asTimeStamp(): Long = this.asLong()

val Date.today: Date
        get() = calendar.time



fun Date.addDays(days: Int = 0): Date {
        calendar.time = this
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
}


fun Date.year(): Int = SimpleDateFormat("yyyy", Consts.ptBR).format(this).toInt()
fun Date.month(): Int = SimpleDateFormat("MM", Consts.ptBR).format(this).toInt()
fun Date.day(): Int = SimpleDateFormat("dd", Consts.ptBR).format(this).toInt()
fun Date.hour(): Int = SimpleDateFormat("HH", Consts.ptBR).format(this).toInt()
fun Date.min(): Int = SimpleDateFormat("mm", Consts.ptBR).format(this).toInt()
fun Date.sec(): Int = SimpleDateFormat("ss", Consts.ptBR).format(this).toInt()

