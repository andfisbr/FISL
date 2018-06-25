package br.com.afischer.fisl.extensions


import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import br.com.afischer.fisl.util.Consts
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URLEncoder
import java.security.NoSuchAlgorithmException
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*







/**
 * String extensions
 */
fun String.asDate(format: String = Consts.DATE_FORMAT): Date = if (format != Consts.DATE_FORMAT || this.matches("(?is)\\d{2}/(\\d{2})/\\d{4}".toRegex()))
        SimpleDateFormat(format, Consts.ptBR).parse(this)
else
        Date(this)




fun String.asDateLong(format: String = Consts.DATE_FORMAT): Long = SimpleDateFormat(format, Consts.ptBR).parse(this).time
fun String.captalise(): String {
        val exceptions = "da das de do dos pda"
        var result = this.toLowerCase()
        
        if ("" != result) {
                val words = result.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (index in words.indices) {
                        val s = words[index]
                        if (!exceptions.contains(s))
                                if (s.isNotEmpty())
                                        words[index] = s.substring(0, 1).toUpperCase() + s.substring(1)
                }
                result = TextUtils.join(" ", words)
        }
        return result
}


fun String.openUrl(): String {
        var result: String = ""
        
        try {
                val client = OkHttpClient()
                val request = Request.Builder().url(this).build()
        
                val response: Response
                response = client.newCall(request).execute()
                result = response.body()!!.string()
                
        } catch (e: IOException) {
                result = ""
                //e.printStackTrace()
        }
        
        return result
}


fun String.asHtml(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        else
                return Html.fromHtml(this)
}

fun String.encoded(): String {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                return URLEncoder.encode(this, "UTF-8" )
        //else
        //        return URLEncoder.encode(this)
}




fun String.md5(): String {
        try {
                // Create MD5 Hash
                val digest = java.security.MessageDigest.getInstance("MD5")
                digest.update(this.toByteArray())
                val messageDigest = digest.digest()
                
                // Create Hex String
                val hexString = StringBuffer()
                for (i in messageDigest.indices)
                        hexString.append(Integer.toHexString(0xFF and messageDigest[i].toInt()))
                return hexString.toString()
                
        } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
        }
        
        return ""
}



fun String.normalise(): String = Normalizer.normalize(this, Normalizer.Form.NFD).replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")


fun String.tlc(): String = this.toLowerCase()
fun String.tuc(): String = this.toUpperCase()


fun String.toRange(): IntRange {
        val ini = this.split("..")[0].toInt()
        val fim = this.split("..")[1].toInt()
        return IntRange(ini, fim)
}





/**
 * Calculates the similarity (a number within 0 and 1) between two strings.
 */
fun String.similarity(other: String): Double {
        var longer = this
        var shorter = other
        
        
        //
        // longer should always have greater length
        //
        if (this.length < other.length) {
                longer = other
                shorter = this
        }
        
        
        val longerLength = longer.length
        
        //
        // both strings are zero length
        //
        if (longerLength == 0) {
                return 1.0
        }
        
        /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        
        
        
        
        val s1 = longer.toLowerCase()
        val s2 = shorter.toLowerCase()
        var costs = IntArray(s2.length + 1)
        
        for (i in 0..s1.length) {
                var lastValue = i
                
                for (j in 0..s2.length) {
                        if (i == 0) {
                                costs[j] = j
        
                        } else if (j > 0) {
                                var newValue = costs[j - 1]
                                if (s1[i - 1] != s2[j - 1]) {
                                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1
                                }
                                costs[j - 1] = lastValue
                                lastValue = newValue
                        }
                }

                if (i > 0) {
                        costs[s2.length] = lastValue
                }
        }
        
        return (longerLength - costs[s2.length]) / longerLength.toDouble()
}




fun String.pad(format: String = "<0"): String {
        val length = format.length - 1
        val char = format[1]
        val direction = format[0]
        
        return when (direction) {
                '>' -> this.padEnd(length, char)
                '<' -> this.padStart(length, char)
                else -> this.padStart(length, char)
        }
}


fun String.charTo(pos: Int, char: Char): String {
        val chars = this.toCharArray()
        chars[pos] = char
        return chars.joinToString("", "")
}




fun String.weekF(): String {
        val days = arrayOf("dom", "seg", "ter", "qua", "qui", "sex", "sáb")
        var aux = ""
        

        this.forEachIndexed { i, c ->
                aux += if (c == '1') "<strong>${days[i]}</strong>" else days[i]
                if (i < this.length - 1)
                        aux += " "
        }
        
        return aux
}