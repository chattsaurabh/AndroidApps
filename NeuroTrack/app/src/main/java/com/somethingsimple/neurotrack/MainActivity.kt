package com.somethingsimple.neurotrack

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var pingPongService: IPingPongAidlInterface? = null
    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            pingPongService = IPingPongAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            pingPongService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val serviceIntent = Intent(this, PingPongUtilityService::class.java)
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE)

        pingBtn.setOnClickListener {
            pingPongService?.ping()
        }

        pongBtn.setOnClickListener {
            pingPongService?.pong()
        }
    }
}
