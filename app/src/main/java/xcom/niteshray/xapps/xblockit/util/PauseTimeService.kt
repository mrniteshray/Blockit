package xcom.niteshray.xapps.xblockit.util

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder

class PauseTimeService :  Service() {
    private var countDownTimer: CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sharedPref by lazy { BlockSharedPref(this) }
        val pauseTime = intent?.getLongExtra("pause_time", 0L) ?: 0L
        sharedPref.setPauseEndTime(pauseTime)
        startCountdown(pauseTime)
        return START_STICKY
    }

    private fun startCountdown(timeInMillis: Long) {
        val sharedPref by lazy { BlockSharedPref(this) }
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                sharedPref.setPauseEndTime(millisUntilFinished)
            }

            override fun onFinish() {
                stopSelf()
                sharedPref.setPauseEndTime(0L)
                sharedPref.setBlock(true)
                NotificationHelper(this@PauseTimeService).showBreakNotification()
            }
        }
        countDownTimer?.start()
    }

    override fun onDestroy() {
        val sharedPref by lazy { BlockSharedPref(this) }
        countDownTimer?.cancel()
        sharedPref.setBlock(true)
        sharedPref.setPauseEndTime(0L)
        NotificationHelper(this).showBreakNotification()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}