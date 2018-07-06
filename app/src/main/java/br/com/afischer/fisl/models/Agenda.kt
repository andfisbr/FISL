package br.com.afischer.fisl.models

import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.util.Consts
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson

class Agenda (var app: FISLApplication) {
        var items = mutableListOf<Item>()
        
        
        var aux: MutableList<Item> = mutableListOf()
        var tracks: MutableList<String> = mutableListOf()
        var talk: TalkDetail = TalkDetail()
        var keywords: MutableList<Keyword> = mutableListOf()
        
        
        var filter = ""
        var yearMonth = "2018-07-"
        var day: String = "11"
        var date: String = ""
                get() = "$yearMonth$day"
        
        
        
        private val site: Site = Site()
        private var result: String = ""
        
        
        
        
        fun retrieve(day: String = ""): FISLResult {
                items.clear()
                
                
                val rooms = 1..10
                var days = 11..14
                
                
                if (day.isNotEmpty()) {
                        days = day.toInt()..day.toInt()
                }
                
                
                
                try {
                        //
                        // obtem do servidor as informações
                        //
                        
                        
                        days.forEach {
                                val date = "$yearMonth${it.pad("<00")}"
                                
                                rooms.forEach { room ->
                                        result = site.get(site.url.agenda.format(room, date))
                                        if (result == "") {
                                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                                        }
                                        
                                        val d: Day = Gson().fromJson(result)
                                        d.items.forEach {
                                                items.add(it)
                                        }
                                }
                        }
                        
                        
                        
                        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter a agenda")
                }
                
                
                return FISLResult(ResultType.SUCCESS)
        }
        
        
        
        
        fun doSave() {
                app.prefs.edit().putString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(items)).apply()
        }
        fun doLoad() {
                val t1a = mutableListOf<Item>()
                items = Gson().fromJson(app.prefs.getString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(t1a)))
        }
        
        
        
        
        fun doKeywords() {
                //
                // acerta as keywords para os filtros
                //
                items.forEach { item ->
                        if (item.talk == null) {
                                return@forEach
                        }
                        
                        val title = item.talk?.title!!.toLowerCase()
                        val owner = item.talk?.owner!!.toLowerCase()
                        val track = item.talk?.track!!.toLowerCase().split(" - ")[1]
                        item.keywords.add(title)
                        item.keywords.add(owner)
                        item.keywords.add(track)
                        
                        
                        if (keywords.none { it.text == title }) {
                                keywords.add(Keyword(title, "título"))
                        }
                        
                        if (keywords.none { it.text == owner }) {
                                keywords.add(Keyword(owner, "palestrante"))
                        }
                        
                        if (keywords.none { it.text == track }) {
                                keywords.add(Keyword(track, "trilha"))
                        }
                }
        }
        
        
        
        
        
        
        
        fun doFilter() {
                aux.clear()
                
                
                aux.addAll(
                        items.filter {
                                it.begins.startsWith(date)
                        }.filter {
                                if (filter.isNotEmpty()) it.keywords.contains(filter) else true
                        }
                )
        }
        
        
        
        
        
        
        
        fun doTracks() {
                tracks.clear()
                
                
                items.filter {
                        it.begins.startsWith(date)
                }.filter {
                        if (filter.isNotEmpty()) it.keywords.contains(filter) else true
                }.forEach {
                        it.talk?.let { talk ->
                                if (!tracks.contains(talk.track)) {
                                        tracks.add(talk.track)
                                }
                        }
                }
        }
        
        
        
        
        
        fun retrieveTalk(id: Int?): FISLResult {
                try {
                        result = site.get(site.url.talk.format(id.toString()))
                        if (result == "") {
                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                        }
                        
                        
                        talk = Gson().fromJson(result)
                        
                        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter os detalhes da palestra")
                }
                
                
                return FISLResult(ResultType.SUCCESS)
        }
        
        
        
        
}