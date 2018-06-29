package br.com.afischer.fisl.models

import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson



class AgendaRN(var app: FISLApplication) {
        private val site: Site = Site()
        private var result: String = ""
        

        fun retrieve(): FISLResult {
                app.agenda.clear()
                
                
                try {
                        /**
                         * obtem do servidor as informações
                         */
                        (1..10).forEach { room ->
                                result = site.get(site.url.agenda.format(room, app.date))
                                if (result == "") {
                                        return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                                }
                                
                                val d: Day = Gson().fromJson(result)
                                d.items.forEach {
                                        app.agenda.add(it)
                                }
                        }
                        
        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter a agenda")
                }
                
                
                return FISLResult(ResultType.SUCCESS)
        }
        
        
}