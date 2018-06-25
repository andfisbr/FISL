package br.com.afischer.fisl.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class SchedulePagerAdapter(var manager: FragmentManager): FragmentPagerAdapter(manager) {
        private val mFragmentList = mutableListOf<Fragment>()
        private val mFragmentTitleList = mutableListOf<String>()
        
        
        override fun getItem(position: Int): Fragment = mFragmentList[position]
        
        override fun getCount(): Int = mFragmentList.size

        override fun getPageTitle(position: Int): CharSequence = mFragmentTitleList[position]
        
        
        
        fun addFragment(fragment: Fragment, title: String) {
                mFragmentList.add(fragment)
                mFragmentTitleList.add(title)
        }
}