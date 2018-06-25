package br.com.afischer.fisl.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import br.com.afischer.fisl.R
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.tab_view.view.*


class PagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {
        private val fragmentList = mutableListOf<Fragment>()
        private val fragmentTitleList = mutableListOf<String>()
        
        
        override fun getItem(position: Int): Fragment = fragmentList[position]
        
        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]
        
        
        
        fun addFragment(fragment: Fragment, title: String) {
                fragmentList.add(fragment)
                fragmentTitleList.add(title)
        }
        
        
        
        
        fun getTabView(context: Context, title: String): View {
                // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
                val v = LayoutInflater.from(context).inflate(R.layout.tab_view, null)
                
                
                v.tab_hour.hide()
                v.tab_minute.hide()
                
                
                if (title.contains(":00")) {
                        v.tab_hour.show()
                        v.tab_hour.text = title
                
                } else {
                        v.tab_minute.show()
                        v.tab_minute.text = title
                }

                return v
        }
}