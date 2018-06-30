package br.com.afischer.fisl.models

import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.extensions.pad
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson



class AgendaRN(var app: FISLApplication) {
        private val site: Site = Site()
        private var result: String = ""
        

        fun retrieve(day: String = ""): FISLResult {
                app.agenda.clear()
        
                
                val hours = 1..10
                var days = 11..14
                
                
                if (day.isNotEmpty()) {
                        days = day.toInt()..day.toInt()
                }

                
                
                try {
                        /**
                         * obtem do servidor as informações
                         */
                        
                        
                        days.forEach {
                                val date = "${app.yearMonth}${it.pad("<00")}"
                                
                                hours.forEach { room ->
                                        result = site.get(site.url.agenda.format(room, date))
                                        if (result == "") {
                                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                                        }
                                        
                                        val d: Day = Gson().fromJson(result)
                                        d.items.forEach {
                                                app.agenda.add(it)
                                        }
                                }
                        }
                        
                        
                        
                        
                        
        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter a agenda")
                }
                
                
                return FISLResult(ResultType.SUCCESS)
        }
        
        
}