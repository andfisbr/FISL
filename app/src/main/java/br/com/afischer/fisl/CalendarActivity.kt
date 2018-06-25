package br.com.afischer.fisl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.afischer.fisl.adapters.PagerAdapter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.mvp.AgendaPresenter
import kotlinx.android.synthetic.main.activity_calendar.*


class CalendarActivity: AppCompatActivity(), BaseView {
        private var presenter: AgendaPresenter = AgendaPresenter()
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_calendar)
        
        
                
                
                presenter.attachView(this)
                presenter.retrieveAgenda()
        }
        

        override fun onDestroy() {
                presenter.detachView()
                super.onDestroy()
        }
        
        
        
        
        fun updateAgenda() {
                //
                // criando as páginas dos dias
                // deve ser executado depois de obter a agenda
                //
                val adapter = PagerAdapter(supportFragmentManager)
                adapter.addFragment(DayFragment.newInstance(11), "11")
                adapter.addFragment(DayFragment.newInstance(12), "12")
                adapter.addFragment(DayFragment.newInstance(13), "13")
                adapter.addFragment(DayFragment.newInstance(14), "14")
                calendar_pager.adapter = adapter
                calendar_pager.setPagingEnabled(false)
                
                
                calendar_tabs.setupWithViewPager(calendar_pager)
        }
}
