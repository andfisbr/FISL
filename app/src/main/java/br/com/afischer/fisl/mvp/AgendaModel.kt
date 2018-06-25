package br.com.afischer.fisl.mvp

import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.models.Day
import br.com.afischer.fisl.models.FISLResult
import br.com.afischer.fisl.models.Site
import com.google.gson.Gson


class AgendaModel(private val presenter: AgendaPresenter) {
        
        private val site: Site = Site()
        private var result: String = ""
        
        
        
        fun retrieveAgenda(): FISLResult {
                presenter.agenda.clear()
        
        
                try {
                        (11..14).forEach d@{ day ->
                                val data = "2018-07-${day.pad("<00")}"
                                
                                
                                (1..15).forEach r@{ room ->
                                        result = site.get(site.url.agenda.format(room, data))
                                        if (result == "") {
                                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                                        }

                                        if (result.contains("\"count\": 0"))
                                                return@r
                                        
                                        val d: Day = Gson().fromJson(result)
                                        presenter.agenda.addAll(d.items)
                                }
                        }
                
                
                
                } catch (ex: Exception) {
                        //Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter a agenda")
                }
        
        
                return FISLResult(ResultType.SUCCESS)
        }
        
        
        
        fun retrieveAgenda(day: String): FISLResult {
                presenter.agenda.clear()
                
                
                try {
                        val date = "2018-07-${day.pad("<00")}"
                        
                        
                        (1..10).forEach { room ->
                                result = site.get(site.url.agenda.format(room, date))
                                if (result == "") {
                                        return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                                }
                                
                                val d: Day = Gson().fromJson(result)
                                presenter.agenda.addAll(d.items)
                        }
                        
                        
                        
                } catch (ex: Exception) {
                        //Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter a agenda")
                }
                
                
                return FISLResult(ResultType.SUCCESS)
        }
        
}

