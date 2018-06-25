package br.com.afischer.fisl.mvp

import br.com.afischer.fisl.AgendaActivity
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.bases.BasePresenter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.models.FISLResult
import br.com.afischer.fisl.models.Item
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class AgendaPresenter: BasePresenter {
        override var app: FISLApplication? = null
        override lateinit var result: FISLResult
        
        private var model: AgendaModel = AgendaModel(this)
        private var view: AgendaActivity? = null
        
        
        
        var agenda: MutableList<Item> = mutableListOf()
        
        
        
        
        
        override fun attachView(view: BaseView) {
                this.view = view as AgendaActivity
                this.app = view.application as FISLApplication
        }
        override fun detachView() {
                this.view = null
                this.app = null
        }
        
        
        
        
        
        
        
        
        
        
        
        fun retrieveAgenda() = launch(UI) {
                
                result = async(CommonPool) { model.retrieveAgenda() }.await()
                
                
                if (result.type != ResultType.SUCCESS) {
                        return@launch
                }
                
                
                app!!.agenda.clear()
                app!!.agenda = agenda
        }
        
        
        
        
        fun retrieveAgenda(day: String) = launch(UI) {
                result = async(CommonPool) { model.retrieveAgenda(day) }.await()
                
                if (result.type != ResultType.SUCCESS)
                        return@launch
                
                app!!.agenda.clear()
                app!!.agenda = agenda
                
                
                view!!.updateAgenda()
        }
}