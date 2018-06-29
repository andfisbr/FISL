package br.com.afischer.fisl.mvp

import br.com.afischer.fisl.TabFragment
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.bases.BasePresenter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.models.FISLResult
import br.com.afischer.fisl.models.TalkDetail
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class TalkPresenter: BasePresenter {
        override var app: FISLApplication? = null
        override lateinit var result: FISLResult
        
        private var model: TalkModel = TalkModel(this)
        private var view: TabFragment? = null
        
        
        
        var talk: TalkDetail = TalkDetail()
        
        
        
        
        
        override fun attachView(view: BaseView) {
                this.view = view as TabFragment
                this.app = view.activity!!.application as FISLApplication
        }
        override fun detachView() {
                this.view = null
                this.app = null
        }
        
        
        
        
        
        
        
        
        
        
        
        fun retrieveTalk(id: Int) = launch(UI) {
                
                result = async(CommonPool) { model.retrieveTalk(id) }.await()
                
                
                if (result.type != ResultType.SUCCESS) {
                        return@launch
                }


                view!!.updateTalkDetail(talk)
        }
}