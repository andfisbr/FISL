package br.com.afischer.fisl

import android.os.Bundle
import android.os.Handler
import br.com.afischer.fisl.adapters.PagerAdapter
import br.com.afischer.fisl.adapters.SearcherAdapter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.events.AgendaActivity_OnAgendaFilter
import br.com.afischer.fisl.extensions.asColor
import br.com.afischer.fisl.extensions.asDrawable
import br.com.afischer.fisl.extensions.hideKeyboard
import br.com.afischer.fisl.extensions.tuc
import br.com.afischer.fisl.models.AgendaRN
import br.com.afischer.fisl.models.Keyword
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
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
        
                
                
                initDayButtons()
                initListeners()                
                initAutocompleteSearcher()
                initPager()                
                initFilter()        
                
                agendaUpdate()
        }
        
        
        override fun onStart() {
                super.onStart()
                EventBus.getDefault().register(this)
        }
        
        override fun onStop() {
                EventBus.getDefault().unregister(this)
                super.onStop()
        }
        
        
        
        

        
        
        
        
        
        
        
        
        private fun initFilter() {
                if (app.filter.isNotEmpty()) {
                        agenda_filter.text = app.filter
                        agenda_filter.isEnabled = true
                        agenda_filter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_black_12dp.asDrawable(this), null, null, null)
                }
        }
        
        
        
        
        private fun initListeners() {
                agenda_back_button.setOnClickListener {
                        finish()
                }


                agenda_search_button.setOnClickListener {
                        if (agenda_search_container.isShown) {
                                searchReset()
                                agendaUpdate()
                                
                        } else {
                                searchInit()
                        }
                }
        
        
        
        
                agenda_filter.setOnClickListener {
                        if (agenda_filter.text == "TODAS" ) {
                                return@setOnClickListener
                        }
                
                        agenda_filter.text = "TODAS"
                        app.filter = ""
                        agenda_filter.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                        agenda_filter.isEnabled = false
                
                        agendaUpdate()
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
        
        



                agenda_search.setOnItemClickListener { adapterView, _, i, _ ->
                        app.keyword = (adapterView.getItemAtPosition(i) as Keyword).text.replace("<strong>", "").replace("</strong>", "")
                        agenda_search.setText(app.keyword)
                
                        hideKeyboard()
                
                
                        /**
                         * obtém os dias que tem palestra
                         */
                        filterDays()
                
                
                        agendaUpdate()
                }
                agenda_search.setOnClickListener {
                        agenda_search.requestFocus()
                        Handler().postDelayed({ agenda_search.selectAll() }, 200)
                }
        
        }





        private fun initPager() {
                pagerAdapter = PagerAdapter(supportFragmentManager)
                agenda_pager.adapter = pagerAdapter
        }
        
        
        
        private fun initViewPager() {
                val tabs = mutableListOf<String>()
                app.aux.filter {
                        it.begins.startsWith(app.date)
                }.map {
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
        }
        
        

        private fun initAutocompleteSearcher() {
                val searcherAdapter = SearcherAdapter(this, R.layout.searcher_view, app.keywords.sortedBy { it.text }.toMutableList())
                agenda_search.setAdapter(searcherAdapter)
        }
        
        
        
        private fun initTracks() {
                app.agenda.filter {
                        if (app.keyword.isNotEmpty()) it.keywords.contains(app.keyword) else true
                }.filter {
                        it.begins.startsWith(app.date)
                }.forEach {
                        it.talk?.let { talk ->
                                if (!app.tracks.contains(talk.track)) {
                                        app.tracks.add(talk.track)
                                }
                        }
                }
        }


        
        
        
        
        
        
        private fun filterDays() {
                /**
                 * esconde os botões 
                 */
                agenda_day11.hide()
                agenda_day12.hide()
                agenda_day13.hide()
                agenda_day14.hide()
        
        
                /**
                 * - filtra pelas keywords
                 * - mapeia os dias
                 * - mostra botões conforme os dias mapeados
                 * - acerta o dia
                 */
                app.agenda.filter {
                        if (app.keyword.isNotEmpty()) it.keywords.contains(app.keyword) else true
                }.map {
                        it.begins.slice(8..9)
                }.toMutableList().forEach {
                        when (it) {
                                "11" -> agenda_day11.show()
                                "12" -> agenda_day12.show()
                                "13" -> agenda_day13.show()
                                "14" -> agenda_day14.show()
                        }
                }
                when {
                        agenda_day11.isShown -> app.day = "11"
                        agenda_day12.isShown -> app.day = "12"
                        agenda_day13.isShown -> app.day = "13"
                        agenda_day14.isShown -> app.day = "14"
                }
                
                
                initDayButtons()
        }
        
        
        private fun searchInit() {
                agenda_search_container.show()
                agenda_search.requestFocus()
                agenda_search_button.setImageResource(R.drawable.ic_close_black_24dp)
        }
        
        private fun searchReset() {
                agenda_search_container.hide()
                agenda_search_button.setImageResource(R.drawable.ic_search_black_24dp)
                agenda_search_button.requestFocus()
        
                
                agenda_search.setText("")
                agenda_filter.text = "TODAS"
                agenda_filter.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                agenda_filter.isEnabled = false
                
                
                app.keyword = ""
                app.filter = ""
                app.day = "11"
        
        
                hideKeyboard()
        
                
                agenda_day11.show()
                agenda_day12.show()
                agenda_day13.show()
                agenda_day14.show()
                
                
                initDayButtons()
        }
        
        
        
        private fun initDayButtons() {
                /**
                 * define o estilo padrão dos botões
                 */
                agenda_day11.backgroundResource = R.drawable.ic_circle_whitesmoke
                agenda_day12.backgroundResource = R.drawable.ic_circle_whitesmoke
                agenda_day13.backgroundResource = R.drawable.ic_circle_whitesmoke
                agenda_day14.backgroundResource = R.drawable.ic_circle_whitesmoke
                
                agenda_day11.setTextColor(R.color.black.asColor(this))
                agenda_day12.setTextColor(R.color.black.asColor(this))
                agenda_day13.setTextColor(R.color.black.asColor(this))
                agenda_day14.setTextColor(R.color.black.asColor(this))
        
        
                
                /**
                 * muda o estilo do botão conforme o dia selecionado
                 */
                when (app.day) {
                        "11" -> {
                                agenda_day11.backgroundResource = R.drawable.ic_circle_green
                                agenda_day11.setTextColor(R.color.white.asColor(this))
                        }
                        "12" -> {
                                agenda_day12.backgroundResource = R.drawable.ic_circle_green
                                agenda_day12.setTextColor(R.color.white.asColor(this))
                        }
                        "13" -> {
                                agenda_day13.backgroundResource = R.drawable.ic_circle_green
                                agenda_day13.setTextColor(R.color.white.asColor(this))
                        }
                        "14" -> {
                                agenda_day14.backgroundResource = R.drawable.ic_circle_green
                                agenda_day14.setTextColor(R.color.white.asColor(this))
                        }
                }
        }
        
        
        
        
        private fun onDayButtonClick() {
                initDayButtons()
                //agendaInit()
                agendaUpdate()
        }
        
        
        
        

        



        
        
        
        
        private fun agendaInit() {
                progressShow()
        
                launch(UI) {
                        val result = async(CommonPool) { AgendaRN(app).retrieve(app.day) }.await()
        
                        if (result.type != ResultType.SUCCESS) {
                                progressHide()
                                toastyShow("e", "Houve um imprevisto na obtenção da agenda, tente mais tarde.")
                                return@launch
                        }
        
                        agendaUpdate()
                }
        }
        
        
        
        private fun agendaFilter() {
                app.aux.clear()
                if (app.filter.isNotEmpty()) {
                        app.agenda.filter {
                                if (app.keyword.isNotEmpty()) it.keywords.contains(app.keyword) else true
                        }.filter {
                                it.begins.startsWith(app.date)
                        }.forEach {  item ->
                                item.talk?.let {
                                        if (it.track == app.filter) {
                                                app.aux.add(item)
                                        }
                                }
                        }
                        
                } else {
                        app.aux.addAll(
                                app.agenda.filter {
                                        if (app.keyword.isNotEmpty()) it.keywords.contains(app.keyword) else true
                                }.filter {
                                        it.begins.startsWith(app.date)
                                }
                        )
                }
        }
        
        
        
        fun agendaUpdate() {
                /**
                 * obtém as trilhas da agenda antes do filtro
                 */
                initTracks()
        
        

                
                /**
                 * filtra pela trilha escolhida
                 */
                agendaFilter()
        
        
                
                
                
                /**
                 * monta as tabs das horas baseado no conteúdo filtrado
                 */
                initViewPager()

                
                
                
                progressHide()
        }
        
        
        
        
        
        
        
        
        
        
        

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        @Subscribe fun onEvent(event: AgendaActivity_OnAgendaFilter) {
                agenda_filter.text = app.filter.split(" - ")[1].tuc()
                agenda_filter.isEnabled = true
        

                agenda_filter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel_black_12dp.asDrawable(this), null, null, null)
                
                
                agendaUpdate()
        }
}
