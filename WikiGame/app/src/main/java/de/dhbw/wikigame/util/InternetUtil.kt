package de.dhbw.wikigame.util

import java.lang.Exception
import java.net.InetAddress

class InternetUtil {
    companion object{
        fun isInternetAvailable(): Boolean {
            return try {
                val ipAddr: InetAddress = InetAddress.getByName("wikimedia.org")
                !ipAddr.equals("")
            } catch (e: Exception) {
                false
            }
        }
    }
}