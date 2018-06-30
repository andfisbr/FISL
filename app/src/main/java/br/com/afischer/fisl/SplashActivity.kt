package br.com.afischer.fisl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.afischer.fisl.adapters.TextTagsAdapter
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.extensions.tlc
import br.com.afischer.fisl.models.AgendaRN
import br.com.afischer.fisl.models.Keyword
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity
import java.util.*

class SplashActivity: AppCompatActivity() {
        
        private val tags = mutableListOf("Administração de sistemas", "Computação em Nuvem, Névoa e P2P", "Desenvolvimento", "Hardware aberto e livre", "Tópicos Emergentes", "Software Livre no dia a dia", "Comunicação e Cultura", "Ecossistema", "Educação")
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_splash)
        
        
                val app = application as FISLApplication
        
        
        
        
        
        
                val tagsAdapter = TextTagsAdapter(tags)
                splash_tags.setAdapter(tagsAdapter)
        
        
        
        
        
        
        
        
        
                /**
                 * verifica qual é o dia atual para poder abrir a agenda corretamente
                 */
                val dd = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
                app.day = if (dd.pad("<00") !in arrayOf("11", "12", "13", "14"))
                        "11"
                else
                        dd.pad("<00")
        
                
                
                
                
        
                /**
                 * obtém a agenda completa da internet
                 */
                launch(UI) {
                        async(CommonPool) { AgendaRN(app).retrieve() }.await()
        
        
                        
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
        
        
                        
                        
                        
                        /**
                         * inicializa a activity principal
                         */
                        startActivity<MainActivity>()
                        finish()
                }
        
        }
}
