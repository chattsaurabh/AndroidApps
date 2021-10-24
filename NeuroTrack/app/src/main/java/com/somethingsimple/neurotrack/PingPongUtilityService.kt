package com.somethingsimple.neurotrack

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import java.util.concurrent.TimeUnit


class PingPongUtilityService : Service() {

    private val TAG = PingPongUtilityService::class.java.simpleName

    private var isSessionActive = false
    private var msRemaining:Long = 0
    var sessionTimer : CountDownTimer? = null
    var startTimeMil: Long? = null
    var endTimeMil: Long? = null


    private fun setSession(timeToAdd: Long = 0) {
        isSessionActive = false
        sessionTimer = object : CountDownTimer(600000 + timeToAdd, 1) {
            override fun onTick(miliSecondsRemaining: Long) {
                isSessionActive = true
                msRemaining = miliSecondsRemaining
            }

            override fun onFinish() {
                isSessionActive = false
                endTimeMil = System.currentTimeMillis()
                var sessionTime = (endTimeMil!! - startTimeMil!!)/1000
                val minutes = sessionTime / 60
                val seconds = sessionTime % 60
                Log.d(TAG, "Session timed out ! Session Length : $minutes minutes and $seconds seconds.")
            }
        }
    }

    private fun resetSession() {
        sessionTimer?.cancel()
        setSession()
        sessionTimer?.start()
        startTimeMil = System.currentTimeMillis()
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    private val mBinder = object : IPingPongAidlInterface.Stub() {
        override fun ping() {
            resetSession()
        }

        override fun pong() {
            if (isSessionActive) {
                sessionTimer?.cancel()
                val resetTimeValue: Long = (600000 - msRemaining) + 120000
                setSession(resetTimeValue)
                sessionTimer?.start()
            } else {
                resetSession()
            }
        }
    }



}
