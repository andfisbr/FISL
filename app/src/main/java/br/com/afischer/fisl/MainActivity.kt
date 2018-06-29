package br.com.afischer.fisl

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.afischer.fisl.adapters.PagerAdapter
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.asString
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity







class MainActivity: AppCompatActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
        
        
                
                /**
                 * carregando fonts
                 */
                val orbitronBlack = Typeface.createFromAsset(assets, "fonts/orbitron-black.otf")
                val orbitronBold = Typeface.createFromAsset(assets, "fonts/orbitron-bold.otf")
                val orbitronLight = Typeface.createFromAsset(assets, "fonts/orbitron-light.otf")
                val orbitronMedium = Typeface.createFromAsset(assets, "fonts/orbitron-medium.otf")
        
        
                
                
                /**
                 * alterando fonts
                 */
                main_fisl.typeface = orbitronBold
                main_fisl_18.typeface = orbitronBold
                main_fisl_forum.typeface = orbitronMedium
                main_fisl_software.typeface = orbitronMedium
                main_fisl_tecnologia.typeface = orbitronMedium
                
                
                
                main_about.text = R.string.about.asString(this).asHtml()
                
                
                
                main_calendar.bringToFront()
                main_calendar.setOnClickListener {
                        startActivity<AgendaActivity>()
                }
        
        
                
                
                
                /**
                 * iniciando patrocinadores
                 */
                val list = mutableListOf(R.color.green_900, R.color.yellow_900, R.color.red_900, R.color.blue_900, R.color.brown_900, R.color.cyan_900, R.color.deep_orange_900, R.color.deep_purple_900, R.color.grey_900, R.color.indigo_900, R.color.pink_900, R.color.lime_900)
                val adapter = PagerAdapter(supportFragmentManager)
                
                list.forEach {
                        adapter.addFragment(SponsorsFragment.newInstance(it), "")
                }
                main_sponsors.adapter = adapter
                main_sponsors.startAutoScroll(true)
        
        }
}
