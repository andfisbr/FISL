package br.com.afischer.fisl

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.style.ClickableSpan
import android.view.View
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.asString
import br.com.afischer.fisl.extensions.linkfy
import kotlinx.android.synthetic.main.activity_dev.*
import org.jetbrains.anko.browse


class DevActivity: AppCompatActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_dev)
        
        
                dev_about.text = R.string.about_dev.asString(this).asHtml()
                
                
                
                
                dev_back_button.setOnClickListener {
                        finish()
                }
        
        
        
        
        
                dev_social.text = "linkedin: https://www.linkedin.com/in/andfisbr/\n\nplay: https://play.google.com/store/apps/developer?id=André Fischer\n\nyoutube: https://www.youtube.com/user/andfisbr\n\ntwitter: https://twitter.com/andfisbr"
        
                dev_social.setLinkTextColor(Color.BLUE) // default link color for clickable span, we can also set it in xml by android:textColorLink=""
                val linkedinLinkClick = object: ClickableSpan() {
                        override fun onClick(view: View?) {
                                browse("https://www.linkedin.com/in/andfisbr/")
                        }
                }
        
                val playLinkClick = object: ClickableSpan() {
                        override fun onClick(view: View?) {
                                browse("https://play.google.com/store/apps/developer?id=André Fischer")
                        }
                }
        
                val tweeterLinkClick = object: ClickableSpan() {
                        override fun onClick(view: View?) {
                                browse("https://twitter.com/andfisbr")
                        }
                }
        
                val youtubeLinkClick = object: ClickableSpan() {
                        override fun onClick(view: View?) {
                                browse("https://www.youtube.com/user/andfisbr")
                        }
                }
                
        
        
                dev_social.linkfy(
                        mutableListOf("https://www.linkedin.com/in/andfisbr/", "https://play.google.com/store/apps/developer?id=André Fischer", "https://twitter.com/andfisbr", "https://www.youtube.com/user/andfisbr"),
                        mutableListOf(linkedinLinkClick, playLinkClick, tweeterLinkClick, youtubeLinkClick)
                )
                
        }
}
