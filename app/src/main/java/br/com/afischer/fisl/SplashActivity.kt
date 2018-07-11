package br.com.afischer.fisl

import android.os.Bundle
import br.com.afischer.fisl.adapters.TextTagsAdapter
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.models.FISLResult
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class SplashActivity: ParentActivity() {
        
        private lateinit var textTagsAdapter: TextTagsAdapter
        
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_splash)
        
        
        
                
                
                
                
                splash_version.text = BuildConfig.VERSION_NAME
        
        
        
                val tracks = mutableListOf(
                        "Administração de sistemas",
                        "Computação em Nuvem, Névoa e P2P",
                        "Desenvolvimento",
                        "Hardware aberto e livre",
                        "Tópicos Emergentes",
                        "Software Livre no dia a dia",
                        "Comunicação e Cultura",
                        "Ecossistema",
                        "Educação"
                )
                
                textTagsAdapter = TextTagsAdapter(tracks)
                splash_tags.setAdapter(textTagsAdapter)
                
                
                
                //
                // reprograma os alarmes
                //
                app.alarm.schedule()
        
        
                
                

                
                doAsync {
                        var result = FISLResult(ResultType.SUCCESS)
        
        
        
                        //
                        // atualiza o sobre
                        //
                        app.agenda.retrieveAbout()




                        //
                        // obtém a agenda completa da internet somente se ela foi alterada
                        //
                        app.agenda.retrieveSummary()
                        if (app.agenda.summary!!.hashCode() != app.summaryHashCode) {
                                app.summaryHashCode = app.agenda.summary!!.hashCode()
                                app.settings.agendaSummaryHashCode = app.summaryHashCode
        
                                result = app.agenda.retrieve()
                        }
                        
                        
                        
                        
                        
                        
                        
                        
                        uiThread {
                                if (result.type == ResultType.SUCCESS) {
                                        app.agenda.doKeywords()
                                        app.agenda.doSave()
                                }
        
        
                                startActivity<MainActivity>()
                                finish()
                        }
                }
        }
}
