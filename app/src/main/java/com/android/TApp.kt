package com.android
import android.app.Application
import android.content.Context

class TApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: TApp? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}