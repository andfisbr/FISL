package br.com.afischer.fisl.extensions

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show


//
// não permite que o softkey aparece
//
fun Activity.hideKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

fun Activity.showKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
}




fun View.asRect(): Rect {
        val l = IntArray(2)
        
        this.getLocationOnScreen(l)
        val rect = Rect(l[0], l[1], l[0] + this.width, l[1] + this.height)
        
        return rect
}


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)





fun View.toggle() {
        if (this.isShown)
                this.hide()
        else
                this.show()
}




fun TextView.linkfy(links: MutableList<String>, clickableSpans: MutableList<ClickableSpan>) {
        val spannableString = SpannableString(this.text)
        links.forEachIndexed { i, s ->
                val clickableSpan = clickableSpans[i]
                
                val startIndexOfLink = this.text.toString().indexOf(s)
                spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        
        this.highlightColor = Color.TRANSPARENT // prevent TextView change background when highlight
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
}
