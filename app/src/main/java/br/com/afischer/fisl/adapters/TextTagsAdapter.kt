package br.com.afischer.fisl.adapters

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moxun.tagcloudlib.view.TagsAdapter


class TextTagsAdapter(var list: MutableList<String>): TagsAdapter() {
        
        override fun getCount(): Int = list.size
        
        override fun getView(context: Context, position: Int, parent: ViewGroup): View {
                val tv = TextView(context)
                tv.text = list[position]
                tv.gravity = Gravity.CENTER
                return tv
        }
        
        override fun getItem(position: Int): Any {
                return list[position]
        }
        
        override fun getPopularity(position: Int): Int {
                return position % 7
        }
        
        override fun onThemeColorChanged(view: View?, themeColor: Int) {
                view?.setBackgroundColor(themeColor)
        }
}