package br.com.afischer.fisl

import android.os.Bundle
import br.com.afischer.fisl.adapters.PagerAdapter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.events.AgendaActivity_OnAgendaFilter
import br.com.afischer.fisl.extensions.tuc
import br.com.afischer.fisl.models.AgendaRN
import kotlinx.android.synthetic.main.activity_agenda.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.backgroundResource


class AgendaActivity: ParentActivity(), BaseView {
        
        private var pagerAdapter: PagerAdapter? = null
        
        
        
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_agenda)
        
                
                
                changeButtonsBackground()
                
                
                
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
        
        
        
        

                
                
                pagerAdapter = PagerAdapter(supportFragmentManager)
                agenda_pager.adapter = pagerAdapter

                
        
                
                if (app.filter.isNotEmpty()) {
                        agenda_filter.text = app.filter
                }
        
                agenda_filter.setOnClickListener {
                        if (agenda_filter.text != "TODAS" ) {
                                agenda_filter.text = "TODAS"
                                app.filter = ""
                        }
                        agenda_filter.isEnabled = false

                        updateAgenda()
                }
                
                
                
                
                
                updateAgenda()

        }
        
        
        override fun onStart() {
                super.onStart()
                EventBus.getDefault().register(this)
        }
        
        override fun onStop() {
                EventBus.getDefault().unregister(this)
                super.onStop()
        }
        
        
        
        

        private fun changeButtonsBackground() {
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
        }
        
        private fun onDayButtonClick() {
                changeButtonsBackground()
                initAgenda()
        }
        
        
        
        

        



        private fun initAgenda() {
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
        
                /**
                 * obtém as trilhas da agenda antes do filtro
                 */
                app.agenda.forEach {
                        it.talk?.let { talk ->
                                if (!app.tracks.contains(talk.track)) {
                                        app.tracks.add(talk.track)
                                }
                        }
                }
        
                
                
                
                /**
                 * filtra pela trilha escolhida
                 */
                app.aux.clear()
                if (app.filter.isNotEmpty()) {
                        app.agenda.forEach {  item ->
                                item.talk?.let {
                                        if (it.track == app.filter) {
                                                app.aux.add(item)
                                        }
                                }
                        }
                
                } else {
                        app.aux.addAll(app.agenda)
                }
        
        
                
                
                
                /**
                 * monta as tabs das horas baseado no conteúdo filtrado
                 */
                val tabs = mutableListOf<String>()
                app.aux.map {
                        it.begins.split("T")[1]
                }.distinct().sorted().toMutableList().forEach {
                        tabs.add(it.slice(0..4))
                }
        
        
                
                
                
                /**
                 * cria uma página no viewpager pra cada tab
                 */
                pagerAdapter?.fragmentList?.clear()
                pagerAdapter?.fragmentTitleList?.clear()
                tabs.forEach {
                        pagerAdapter?.addFragment(TabFragment.newInstance(it), it)
                }
                pagerAdapter?.notifyDataSetChanged()
                


                agenda_pager.currentItem = 0
                
                
                agenda_tabs.setupWithViewPager(agenda_pager)
                

                
                
                
                progressHide()
        }
        
        
        
        
        
        
        
        @Subscribe fun onEvent(event: AgendaActivity_OnAgendaFilter) {
                agenda_filter.text = app.filter.split(" - ")[1].tuc()
                agenda_filter.isEnabled = true
                
                
                updateAgenda()
        }
}
