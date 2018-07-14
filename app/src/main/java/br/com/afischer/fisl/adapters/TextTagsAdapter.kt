package br.com.afischer.fisl.adapters

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moxun.tagcloudlib.view.TagsAdapter


class TextTagsAdapter(var data: MutableList<String>): TagsAdapter() {
        override fun onThemeColorChanged(view: View?, themeColor: Int) {
                val color = Color.argb(((1 - 1) * 255), 255, 255, 255)

                view?.setBackgroundColor(color)
        }
        
        override fun getCount(): Int {
                return data.size
        }
        
        override fun getView(context: Context, position: Int, parent: ViewGroup): View {
                val tv = TextView(context)
                tv.text = data[position]
                tv.gravity = Gravity.CENTER
                tv.setTextColor(Color.WHITE)
                return tv
        }
        
        override fun getItem(position: Int): Any {
                return data[position]
        }
        
        override fun getPopularity(position: Int): Int {
                return position % 7
        }
        
//        override fun onThemeColorChanged(view: View, themeColor: Int, alpha: Float) {
//                view.setBackgroundColor(themeColor)
//        }
}