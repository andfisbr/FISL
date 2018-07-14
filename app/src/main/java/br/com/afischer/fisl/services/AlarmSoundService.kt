package br.com.afischer.fisl.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import br.com.afischer.fisl.R





class AlarmSoundService: Service() {
        override fun onBind(intent: Intent): IBinder? = null
        
        
        private var mediaPlayer: MediaPlayer? = null
        


        
        override fun onCreate() {
                super.onCreate()
        
                //Start media player
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
                mediaPlayer!!.start()
                
                
                mediaPlayer!!.setOnCompletionListener {
                        stopSelf()
                }
        }
        

        override fun onDestroy() {
                super.onDestroy()
                
                //On destory stop and release the media player
                mediaPlayer?.let {
                        it.stop()
                        it.reset()
                        it.release()
                }
        }
}
