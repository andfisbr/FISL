package br.com.afischer.fisl.models

import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.fromJson
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class TalkDetail(
        @SerializedName("resource") var resource: Resource = Resource()
) {
        
        private val site: Site = Site()
        private var result: String = ""

        
        fun retrieve(id: Int): FISLResult {
                try {
                        result = site.get(site.url.talk.format(id.toString()))
                        if (result == "") {
                                return FISLResult(ResultType.NO_RESPONSE, "[NO_RESPONSE] Sem resposta do website")
                        }
                        
                        
                        val td: TalkDetail = Gson().fromJson(result)
                        this.resource = td.resource
                        
                        
                        
                } catch (ex: Exception) {
                        //Crashlytics.logException(ex)
                        return FISLResult(ResultType.EXCEPTION, "[EXCEPTION] Problemas ao obter os detalhes da palestra")
                }
                
                
                return FISLResult(ResultType.SUCCESS)
        }
}