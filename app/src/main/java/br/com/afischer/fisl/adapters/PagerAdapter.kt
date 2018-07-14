package br.com.afischer.fisl.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter


class PagerAdapter(manager: FragmentManager): FragmentStatePagerAdapter(manager) {
        val fragmentList = mutableListOf<Fragment>()
        val fragmentTitleList = mutableListOf<String>()
        
        
        override fun getItem(position: Int): Fragment = fragmentList[position]
        
        override fun getCount(): Int = fragmentList.size

        override fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]
        
        override fun getItemPosition(`object`: Any): Int {
                return PagerAdapter.POSITION_NONE
        }
        
        
        
        fun addFragment(fragment: Fragment, title: String) {
                fragmentList.add(fragment)
                fragmentTitleList.add(title)
        }
}