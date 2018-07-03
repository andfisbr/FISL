package br.com.afischer.fisl

import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.asString
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity




class MainActivity: ParentActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
        
        
        
                main_about.text = R.string.about.asString(this).asHtml()


                initTitle()
                initListeners()
                initSponsors()
        }
        
        
        
        
        
        
        

        
        
        
        
        
        private fun initListeners() {
                main_calendar_button.setOnClickListener {
                        startActivity<AgendaActivity>()
                }
        }
        
        
        
        private fun initSponsors() {
                val list = mutableListOf(
                        R.drawable.ic_sponsor01,
                        R.drawable.ic_sponsor02,
                        R.drawable.ic_sponsor03,
                        R.drawable.ic_sponsor04,
                        R.drawable.ic_sponsor05,
                        R.drawable.ic_sponsor06,
                        R.drawable.ic_sponsor07,
                        R.drawable.ic_sponsor08,
                        R.drawable.ic_sponsor09,
                        R.drawable.ic_sponsor10,
                        R.drawable.ic_sponsor11,
                        R.drawable.ic_sponsor12,
                        R.drawable.ic_sponsor13,
                        R.drawable.ic_sponsor14,
                        R.drawable.ic_sponsor15,
                        R.drawable.ic_sponsor16,
                        R.drawable.ic_sponsor17,
                        R.drawable.ic_sponsor18,
                        R.drawable.ic_sponsor19,
                        R.drawable.ic_sponsor20,
                        R.drawable.ic_sponsor21,
                        R.drawable.ic_sponsor22
                        )
        
        
                main_sponsors.pageCount = list.size
                main_sponsors.setImageListener { position, imageView ->
                        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                        imageView.setImageResource(list[position])
                }
        }



        private fun initTitle() {
                //
                // carregando fonts
                //
                val orbitronBlack = Typeface.createFromAsset(assets, "fonts/orbitron-black.otf")
                val orbitronBold = Typeface.createFromAsset(assets, "fonts/orbitron-bold.otf")
                val orbitronLight = Typeface.createFromAsset(assets, "fonts/orbitron-light.otf")
                val orbitronMedium = Typeface.createFromAsset(assets, "fonts/orbitron-medium.otf")
        
        
        
        
                //
                // alterando fonts
                //
                main_fisl.typeface = orbitronBold
                main_fisl_18.typeface = orbitronBold
                main_fisl_forum.typeface = orbitronMedium
                main_fisl_software.typeface = orbitronMedium
                main_fisl_tecnologia.typeface = orbitronMedium
                
                
                
                
                
                main_dev.setOnClickListener {
                        toastyShow("s", "Página do Fulano de Tal")
                        //browse("https://play.google.com/store/apps/developer?id=Andr%C3%A9+Fischer")
                }
        }
}
