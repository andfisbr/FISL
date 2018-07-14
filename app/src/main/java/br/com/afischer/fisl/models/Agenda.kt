package br.com.afischer.fisl.models

import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.util.Consts
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import org.jsoup.Jsoup

class Agenda (var app: FISLApplication) {
        var items = mutableListOf<Item>()
        
        var summary: Summary? = null
        
        
        var aux: MutableList<Item> = mutableListOf()
        var tracks: MutableList<String> = mutableListOf()
        var talk: Talk = Talk()
        var keywords: MutableList<Keyword> = mutableListOf()
        
        
        var filter = ""
        var yearMonth = "2018-07-"
        var day: String = "11"
        var date: String = ""
                get() = "$yearMonth$day"
        
        
        
        private val site: Site = Site()
        private var result: String = ""
        
        
        
        
        fun retrieve(day: String = ""): FISLResult {
                result = ""
        
                items.clear()
                
                
                val rooms = 1..11
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
                                        d.items.forEach { item ->
                                                items.add(item)

                                                
                                                //
                                                // solução de contorno para fazer com que palestras com mais de uma hora apareçam nos outros horários
                                                //
                                                val slots = (Math.ceil(item.duration / 60.0)).toInt()
                                                if (slots > 1) {
                                                        (1 until slots).forEach {
                                                                val aux = item.copy()
                                                                aux.continuation = true
                                                                aux.hour = aux.hour + it
                                                                aux.begins = "${aux.begins.split("T")[0]}T${aux.hour.pad("<00")}:00:00"

                                                                items.add(aux)
                                                        }
                                                }
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
                try {
                        app.prefs.edit().putString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(items)).apply()
                        
                } catch(ex: Exception) {
                        Crashlytics.logException(ex)
                }
        }
        fun doLoad() {
                try {
                        val t1a = mutableListOf<Item>()
                        items = Gson().fromJson(app.prefs.getString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(t1a)))
                        
                } catch(ex: Exception) {
                        Crashlytics.logException(ex)
                }
        }
        
        
        
        
        fun doKeywords() {
                //
                // acerta as keywords para os filtros
                //
                val aux = mutableListOf<Item>()
                aux.addAll(items)
                
                aux.forEachIndexed { i, a ->
                        if (a.talk == null) {
                                return@forEachIndexed
                        }
                        
                        val title = a.talk?.title!!.toLowerCase()
                        val owner = a.talk?.owner!!.toLowerCase()
                        val track = a.talk?.track!!.toLowerCase().split(" - ")[1]
                        val room = a.roomName.toLowerCase()
                        if (items[i].keywords.none { it == title }) { items[i].keywords.add(title) }
                        if (items[i].keywords.none { it == owner }) { items[i].keywords.add(owner) }
                        if (items[i].keywords.none { it == track }) { items[i].keywords.add(track) }
                        if (items[i].keywords.none { it == room }) { items[i].keywords.add(room) }
                        
                        
                        
                        if (keywords.none { it.text == title }) { keywords.add(Keyword(title, "título")) }
                        if (keywords.none { it.text == owner }) { keywords.add(Keyword(owner, "palestrante")) }
                        if (keywords.none { it.text == track }) { keywords.add(Keyword(track, "trilha")) }
                        if (keywords.none { it.text == room }) { keywords.add(Keyword(room, "sala")) }
                }
        }
        
        
        
        
        
        
        
        fun doFilter(withEmpty: Boolean = false) {
                aux.clear()
                
                
                aux.addAll(
                        items.filter {
                                if (!withEmpty) it.status != "empty" else true
                        }.filter {
                                it.begins.startsWith(date)
                        }.filter {
                                if (filter.isNotEmpty()) it.keywords.contains(filter) else true
                        }
                )
        }
        
        
        
        
        
        
        
        
        
        
        
        
        fun retrieveTalk(id: Int?): FISLResult {
                result = ""
        
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
        
        

        fun retrieveSummary(listener: () -> Unit = {}): FISLResult {
                result = ""
        
                summary = Summary()
                
                
                //
                // obtem do servidor as informações
                //
                try {
                        result = site.get(site.url.summary)
                        if (result == "") {
                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                        }
                        
                        summary = Gson().fromJson(result)
                        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter o resumo")
                }
                
                
                listener()
                return FISLResult(ResultType.SUCCESS)
        }
        
        
        
        
        
        
        fun retrieveAbout(): FISLResult {
                result = ""
                
                try {
                        result = site.get(site.url.about)
                        if (result == "") {
                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                        }
                
                        
                        val doc = Jsoup.parse(result)
                        val html = doc.select("#sobre").html()
                        
                        app.about = html.replace("><strong>", ">").replace("</strong><", "<").replace("</strong>:<", "<")
                        
                        
                
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter o sobre")
                }
        
        
                return FISLResult(ResultType.SUCCESS)
        }
        
}