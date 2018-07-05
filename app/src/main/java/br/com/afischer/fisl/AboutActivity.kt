package br.com.afischer.fisl

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.asString
import br.com.afischer.fisl.extensions.sec
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity: AppCompatActivity() {
        lateinit var countDownTimer: CountDownTimer
        
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_about)
        
        
                about_app.text = R.string.about_app.asString(this).asHtml()
                
                
                
                
                about_back_button.setOnClickListener {
                        finish()
                }
                about_toolbar_title.setOnClickListener {
                        finish()
                }
        
        
        
        
                initKenBurns()
        }
        
        
        override fun onDestroy() {
                countDownTimer.cancel()
                super.onDestroy()
        }
        
        
        
        
        private fun initKenBurns() {
                
                //
                // set transition
                //
                val generator = RandomTransitionGenerator(12000L, AccelerateDecelerateInterpolator())
                about_back.setTransitionGenerator(generator)
        
        
                countDownTimer = object: CountDownTimer(10.sec().toLong(), 10) {
                        override fun onTick(millisUntilFinished: Long) {
                                about_progress.progress = (10.sec() - millisUntilFinished).toInt()
                        }
                
                        override fun onFinish() {
                                try {
                                        //
                                        // choose image
                                        //
                                        val images = resources.obtainTypedArray(R.array.splash_backgrounds)
                                        val choice = (Math.random() * images.length()).toInt()
        
                                        ImageViewAnimatedChange(this@AboutActivity, about_back, images.getDrawable(choice))
        
                                        //about_back.setImageDrawable(images.getDrawable(choice))
                                
                                        images.recycle()
                                
                                
                                
                                        about_progress.progress = 0
                                        countDownTimer.start()
                                
                                } catch (ex: Exception) {
                                }
                        }
                }
        
        
                countDownTimer.start()
        }
        
        
        
        
        
        
        fun ImageViewAnimatedChange(c: Context, v: ImageView, new_image: Drawable) {
                val animOut = AnimationUtils.loadAnimation(c, android.R.anim.fade_out)
                val animIn = AnimationUtils.loadAnimation(c, android.R.anim.fade_in)
                animOut.setAnimationListener(object: AnimationListener {
                        override fun onAnimationStart(animation: Animation) {}
                        override fun onAnimationRepeat(animation: Animation) {}
                        override fun onAnimationEnd(animation: Animation) {
                                v.setImageDrawable(new_image)
                                animIn.setAnimationListener(object: AnimationListener {
                                        override fun onAnimationStart(animation: Animation) {}
                                        override fun onAnimationRepeat(animation: Animation) {}
                                        override fun onAnimationEnd(animation: Animation) {}
                                })
                                v.startAnimation(animIn)
                        }
                })
                v.startAnimation(animOut)
        }
        
}
