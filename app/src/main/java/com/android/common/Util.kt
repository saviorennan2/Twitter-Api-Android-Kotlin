package com.android.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.TApp

class Util {
    companion object {
        fun getBase64String(value: String): String? {
            return Base64.encodeToString(value.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
        }

        fun makeToast(context: Context, text: CharSequence) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }

        fun makeLog(key: String, value: String) {
            Log.e("$key : ", value)
        }

        fun isNetworkAvailable(): Boolean {
            val connectivityManager = TApp.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }
}