package br.com.afischer.fisl.extensions

import br.com.afischer.fisl.util.Consts
import java.text.DecimalFormat
import java.util.*


fun Double.asCoin(locale: Locale = Consts.ptBR): String = DecimalFormat.getCurrencyInstance(locale).format(this)
fun Double.asPercentage(): String = String.format("%.2f%", this)