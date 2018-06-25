package br.com.afischer.fisl

import android.os.Bundle
import br.com.afischer.fisl.adapters.PagerAdapter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.models.AgendaRN
import kotlinx.android.synthetic.main.activity_agenda.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.backgroundResource






class AgendaActivity: ParentActivity(), BaseView {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_agenda)
        
                
                agenda_back.setOnClickListener {
                        finish()
                }
        
                
                agenda_day11.setOnClickListener {
                        app.day = "11"
                        onDayButtonClick()
                }
                agenda_day12.setOnClickListener {
                        app.day = "12"
                        onDayButtonClick()
                }
                agenda_day13.setOnClickListener {
                        app.day = "13"
                        onDayButtonClick()
                }
                agenda_day14.setOnClickListener {
                        app.day = "14"
                        onDayButtonClick()
                }
                
                
                
                onDayButtonClick()
                setupAgenda()
        }
        

        
        
        

        
        private fun onDayButtonClick() {
                agenda_day11.backgroundResource = R.drawable.ic_circle_whitesmoke
                agenda_day12.backgroundResource = R.drawable.ic_circle_whitesmoke
                agenda_day13.backgroundResource = R.drawable.ic_circle_whitesmoke
                agenda_day14.backgroundResource = R.drawable.ic_circle_whitesmoke
                
                when (app.day) {
                        "11" -> agenda_day11.backgroundResource = R.drawable.ic_circle_green
                        "12" -> agenda_day12.backgroundResource = R.drawable.ic_circle_green
                        "13" -> agenda_day13.backgroundResource = R.drawable.ic_circle_green
                        "14" -> agenda_day14.backgroundResource = R.drawable.ic_circle_green
                }
        
                setupAgenda()
        }
        
        
        
        
        private fun setupAgenda() {
                progressShow()
        
                launch(UI) {
                        val result = async(CommonPool) { AgendaRN(app).retrieve() }.await()
        
                        if (result.type != ResultType.SUCCESS) {
                                progressHide()
                                toastyShow("e", "Houve um imprevisto na obtenção da agenda, tente mais tarde.")
                                return@launch
                        }
        
                        updateAgenda()
                }
        }
        
        fun updateAgenda() {
                val hours = mutableListOf<String>()
                
                (8..20).forEach {
                        hours.add("${it.pad("<00")}:00")
                        //hours.add("${it.pad("<00")}:20")
                        //hours.add("${it.pad("<00")}:40")
                }
                

                val adapter = PagerAdapter(supportFragmentManager)
                hours.forEach {
                        adapter.addFragment(HourFragment.newInstance(it), it)
                }

                agenda_pager.adapter = adapter
                agenda_tabs.setupWithViewPager(agenda_pager)
                


                // Iterate over all tabs and set the custom view
                //(0 until agenda_tabs.tabCount).forEach {
                //        val tab = agenda_tabs.getTabAt(it)
                //        tab?.customView = adapter.getTabView(this, tab!!.text.toString())
                //
                //        if (!tab.text.toString().contains(":00")) {
                //                val layout = tab.customView as LinearLayout
                //                val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
                //                layoutParams.weight = 0.3f
                //                layout.layoutParams = layoutParams
                //        }
                //}
        

                progressHide()
        }
}
