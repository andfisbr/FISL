package br.com.afischer.fisl.models

import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.extensions.tlc
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
                        //
                        // obtem do servidor as informações
                        //
                        
                        
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
                        
                        
                        app.agendaSave()
                        
                        
                        
                        
                        
        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter a agenda")
                }
                
                
                return FISLResult(ResultType.SUCCESS)
        }
        
        
        
        
        
        
        
        fun loadKeywords() {
                /**
                 * acerta as keywords para os filtros
                 */
                app.agenda.forEach { item ->
                        if (item.talk == null) {
                                return@forEach
                        }
                
                        val title = item.talk?.title!!.tlc()
                        val owner = item.talk?.owner!!.tlc()
                        val track = item.talk?.track!!.tlc().split(" - ")[1]
                        item.keywords.add(title)
                        item.keywords.add(owner)
                        item.keywords.add(track)
                
                
                        if (app.keywords.none { it.text == title }) {
                                app.keywords.add(Keyword(title, "título"))
                        }
                
                        if (app.keywords.none { it.text == owner }) {
                                app.keywords.add(Keyword(owner, "palestrante"))
                        }
                
                        if (app.keywords.none { it.text == track }) {
                                app.keywords.add(Keyword(track, "trilha"))
                        }
                }
        }
        
        
        
        
        
        

        fun filter() {
                app.aux.clear()

                
                app.aux.addAll(
                        app.agenda.filter {
                                it.begins.startsWith(app.date)
                        }.filter {
                                if (app.filter.isNotEmpty()) it.keywords.contains(app.filter) else true
                        }
                )
        }
        
        
        
        



        fun initTracks() {
                app.tracks.clear()
                
                
                app.agenda.filter {
                        it.begins.startsWith(app.date)
                }.filter {
                        if (app.filter.isNotEmpty()) it.keywords.contains(app.filter) else true
                }.forEach {
                        it.talk?.let { talk ->
                                if (!app.tracks.contains(talk.track)) {
                                        app.tracks.add(talk.track)
                                }
                        }
                }
        }
        
        
}