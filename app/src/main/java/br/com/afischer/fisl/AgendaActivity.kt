package br.com.afischer.fisl

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import br.com.afischer.fisl.adapters.PagerAdapter
import br.com.afischer.fisl.adapters.SearcherAdapter
import br.com.afischer.fisl.events.AgendaActivity_OnAgendaFilter
import br.com.afischer.fisl.events.AgendaActivity_ProgressHide
import br.com.afischer.fisl.events.AgendaActivity_ProgressShow
import br.com.afischer.fisl.events.AgendaActivity_ShowToast
import br.com.afischer.fisl.extensions.*
import br.com.afischer.fisl.models.Keyword
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import khronos.Dates
import khronos.with
import kotlinx.android.synthetic.main.activity_agenda.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.startActivity


class AgendaActivity: ParentActivity() {
        
        private var pagerAdapter: PagerAdapter? = null
        private val buttons = mutableListOf<TextView>()
        

        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_agenda)
        
        
                buttons.add(agenda_day11)
                buttons.add(agenda_day12)
                buttons.add(agenda_day13)
                buttons.add(agenda_day14)
                
                
                
                
                
                
                //initButtons()
                filterDays()
                
                if (app.agenda.filter.isNotEmpty()) {
                        agenda_search.setText(app.agenda.filter)
                        searchInit(false)
                }
                
                
                
                initListeners()                
                initAutocompleteSearcher()
                initPager()                
        }
        
        
        override fun onResume() {
                super.onResume()

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
        
        override fun onDestroy() {
                setResult(1000)
                super.onDestroy()
        }
        

        
        
        
        
        
        
        
        
        
        
        
        
        private fun initListeners() {
                setResult(1000)
                
                
                agenda_back_button.setOnClickListener {
                        finish()
                }
                agenda_toolbar_title.setOnClickListener {
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
        

                agenda_alarm_button.setOnClickListener {
                        startActivity<AlarmActivity>()
                }
                
                
                buttons.forEach { b ->
                        b.setOnClickListener {
                                app.agenda.day = b.text.toString()
                                onDayButtonClick()
                        }
                }
                
                
                
                agenda_show_without_talks.setOnClickListener {
                        agenda_show_without_talks.isChecked = !agenda_show_without_talks.isChecked
                        
                        agendaUpdate()
                }
        }





        private fun initPager() {
                pagerAdapter = PagerAdapter(supportFragmentManager)
                agenda_pager.adapter = pagerAdapter
        }
        
        
        
        private fun initViewPager() {
                val tabs = mutableListOf<String>()
                app.agenda.aux.filter {
                        it.begins.startsWith(app.agenda.date)
                }.map {
                        it.begins.split("T")[1]
                }.distinct().sorted().toMutableList().forEach {
                        tabs.add(it.slice(0..4))
                }
                
                
                
                
                
                //
                // cria uma página no viewpager pra cada tab
                //
                pagerAdapter?.fragmentList?.clear()
                pagerAdapter?.fragmentTitleList?.clear()
                tabs.forEach {
                        pagerAdapter?.addFragment(TabFragment.newInstance(it), it)
                }
                pagerAdapter?.notifyDataSetChanged()
                
                
                
                
                //
                // pula para página correta conforme a hora atual
                //
                agenda_pager.currentItem = getCurrentPage()
                
                
                
                
                
                agenda_tabs.setupWithViewPager(agenda_pager)
        }
        
        
        
        private fun initAutocompleteSearcher() {
                val searcherAdapter = SearcherAdapter(this, R.layout.searcher_view, app.agenda.keywords.sortedBy { it.text }.toMutableList())
                agenda_search.setAdapter(searcherAdapter)
                agenda_search.setOnItemClickListener { adapterView, _, i, _ ->
                        
                        app.agenda.filter = (adapterView.getItemAtPosition(i) as Keyword).text.replace("<strong>", "").replace("</strong>", "")
                        agenda_search.setText(app.agenda.filter)
                        
                        
                        hideKeyboard()
                        
                        
                        //
                        // obtém os dias que tem palestra
                        //
                        filterDays()
                        
                        
                        agendaUpdate()
                }
                agenda_search.setOnClickListener {
                        agenda_search.requestFocus()
                        Handler().postDelayed({ agenda_search.selectAll() }, 200)
                }
        }
        
        
        
        
        
        
        
        //
        // mostra botões dos dias baseado nos filtros
        //
        private fun filterDays() {

                buttons.forEach { it.hide() }

                
        
                //
                // - filtra pelas keywords
                // - mapeia os dias
                // - mostra botões conforme os dias mapeados
                //
                val aux = app.agenda.items.filter {
                        if (app.agenda.filter.isNotEmpty()) it.keywords.contains(app.agenda.filter) else true

                }.map {
                        it.begins.slice(8..9)

                }.toMutableList()
                
                buttons.forEach {
                        if (it.text in aux) {
                                it.show()
                        }
                }
        
        
                
                //
                // acerta o dia do filtro
                //
                app.agenda.day = getCurrentDay()
                
                
                
                
                //
                // se o botão referente ao dia estiver sendo mostrado
                // então deixar ele selecionado
                // senão, vai para o primeiro dia do evento
                //
                if (app.agenda.day !in aux) {
                        app.agenda.day = "11"
                }

                
                
                
                initButtons()
        }
        
        private fun searchInit(focus: Boolean = true) {
                agenda_search_button.setImageResource(R.drawable.ic_close_black_24dp)
                agenda_search_button.contentDescription = "Fechar caixa de pesquisa."
                

                agenda_search_container.show()
                

                if (focus) {
                        agenda_search.requestFocus()
                        showKeyboard()
                }
        }
        
        
        private fun searchReset() {
                hideKeyboard()
        
                agenda_search_container.hide()

                agenda_search_button.setImageResource(R.drawable.ic_search_black_24dp)
                agenda_search_button.contentDescription = "Pesquisar. Utilize a caixa a esquerda para a pesquisa."
                
                agenda_search.setText("")
        


                app.agenda.filter = ""
                app.agenda.day = getCurrentDay()
        
        
                buttons.forEach { it.show() }
                

                initButtons()
        }
        
        
        
        private fun initButtons() {
                buttons.forEach {
                        //
                        // define o estilo padrão dos botões
                        //
                        it.backgroundResource = R.drawable.ic_circle_whitesmoke
                        it.setTextColor(R.color.black.asColor(this))
                        
                        
                        //
                        // muda o estilo do botão conforme o dia selecionado
                        //
                        if (it.text == app.agenda.day) {
                                it.backgroundResource = R.drawable.ic_circle_green
                                it.setTextColor(R.color.colorAccent.asColor(this))
                        }
                }
        }
        
        
        
        
        private fun onDayButtonClick() {
                initButtons()
                //agendaInit()
                agendaUpdate()
        }
        
        
        
        

        



        
        
        
        
        private fun agendaUpdate() {
                //
                // filtra pela trilha escolhida
                //
                app.agenda.doFilter(agenda_show_without_talks.isChecked)
        
        
                
                
                
                //
                // monta as tabs das horas baseado no conteúdo filtrado
                //
                initViewPager()

                
                
                
                progressHide()
        }
        
        
        
        
        
        
        
        
        
        
        private fun getCurrentDay(): String {
                val startDate = Dates.today.with(2018, 7, 11, 0, 0, 1)
                val endDate = Dates.today.with(2018, 7, 14, 23, 59, 59)
                
                return if (Dates.today in startDate..endDate) {
                        Dates.today.day().toString().pad("<00")
                } else {
                        "11"
                }
        }
        
        
        private fun getCurrentPage(): Int {
                val hour = Dates.today.hour().pad("<00")
                var position = 0
                
                pagerAdapter?.fragmentTitleList?.forEachIndexed { index, s ->
                        if (s.startsWith(hour) && position == 0) {
                                position = index
                        }
                }
                
                return position
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        @Subscribe fun onEvent(event: AgendaActivity_OnAgendaFilter) {
                searchInit(false)

                agenda_search.setText(event.message)
                agenda_search.dismissDropDown()
        
        
                filterDays()
                
                agendaUpdate()
        }
        

        @Subscribe fun onEvent(event: AgendaActivity_ShowToast) {
                alerterShow(event.type, event.message)
        }
        

        @Subscribe fun onEvent(event: AgendaActivity_ProgressShow) {
                progressShow(event.message)
        }

        @Subscribe fun onEvent(event: AgendaActivity_ProgressHide) {
                progressHide()
        }
}
