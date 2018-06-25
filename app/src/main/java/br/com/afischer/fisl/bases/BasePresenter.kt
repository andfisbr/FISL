package br.com.afischer.fisl.bases

import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.models.FISLResult

interface BasePresenter {
        var app: FISLApplication?
        var result: FISLResult
        
        fun attachView(view: BaseView)
        fun detachView()
}