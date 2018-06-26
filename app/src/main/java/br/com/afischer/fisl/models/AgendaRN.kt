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
        

        fun retrieve(): FISLResult {
                app.agenda.clear()
                
                
                try {
                        val date = "2018-07-${app.day.pad("<00")}"
                        
                        
                        (1..10).forEach { room ->
                                result = site.get(site.url.agenda.format(room, date))
                                if (result == "") {
                                        return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                                }
                                
                                val d: Day = Gson().fromJson(result)
                                
                                d.items.forEach item@{ item ->
                                        app.agenda.add(item)


                                        val slots = item.duration / 60
                                        if (slots == 1)
                                                return@item
                                        
                                        
                                        (1 until slots).forEach {
                                                val aux = item.copy()
                                                aux.status = "continuation"
                                                aux.begins = "${date}T${(aux.hour + it).pad("<00")}:00:00"
                                                
                                                app.agenda.add(aux)
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