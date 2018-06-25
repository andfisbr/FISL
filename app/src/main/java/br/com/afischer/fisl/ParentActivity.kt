package br.com.afischer.fisl


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import br.com.afischer.fisl.app.FISLApplication
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.app_progress.*


open class ParentActivity: AppCompatActivity() {
        lateinit var app: FISLApplication
        var currentFragment: String = ""
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                
                app = application as FISLApplication
                
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        
        
        fun progressShow(message: String = "") {
                main_progress_message.text = message
                
                if (!main_progress.isShown) {
                        main_progress.show()
                }
        }
        
        
        fun progressHide() {
                if (main_progress.isShown) {
                        main_progress.hide()
                        main_progress_message.text = ""
                }
        }
        
        
        fun toastyShow(type: String = "", message: String, length: Int = Toast.LENGTH_LONG) {
                when (type) {
                        "w" -> Toasty.warning(this, message, length, true).show()
                        "i" -> Toasty.info(this, message, length, true).show()
                        "s" -> Toasty.success(this, message, length, true).show()
                        "e" -> Toasty.error(this, message, length, true).show()
                }
        }
        
        
        fun setupAds(ad: AdView) {
                val adBuilder = AdRequest.Builder()
                val adRequest = adBuilder.build()
                
                
                ad.show()
                ad.bringToFront()
                ad.loadAd(adRequest)
                
                
                ad.adListener = object: AdListener() {
                        override fun onAdFailedToLoad(i: Int) {
                                super.onAdFailedToLoad(i)
                                ad.visibility = View.GONE
                        }
                        
                        
                        override fun onAdLoaded() {
                                super.onAdLoaded()
                                ad.visibility = View.VISIBLE
                        }
                }
        }
}
