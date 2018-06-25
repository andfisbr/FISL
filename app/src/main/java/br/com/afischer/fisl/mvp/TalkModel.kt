package br.com.afischer.fisl.mvp

import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.models.FISLResult
import br.com.afischer.fisl.models.Site
import br.com.afischer.fisl.models.TalkDetail
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson


class TalkModel(private val presenter: TalkPresenter) {
        
        private val site: Site = Site()
        private var result: String = ""
        
        
        
        fun retrieveTalk(id: Int): FISLResult {
                presenter.talk = TalkDetail()
        
        
                try {
                        result = site.get(site.url.talk.format(id.toString()))
                        if (result == "") {
                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                        }


                        presenter.talk = Gson().fromJson(result)
                        
                
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter os detalhes da palestra")
                }
        
        
                return FISLResult(ResultType.SUCCESS)
        }
}

