package uz.gita.mycontactb7.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkStatusValidator private constructor() {
    companion object {
        var hasNetwork : Boolean = false
        private lateinit var instance: NetworkStatusValidator

        fun init(context: Context, availableNetworkBlock : () -> Unit) {
            if (!(::instance.isInitialized)) instance = NetworkStatusValidator()

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)   // wifi
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)  // sim
                .build()

            val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    hasNetwork = true
                    availableNetworkBlock.invoke()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)

                    hasNetwork = false
                }
            }

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.requestNetwork(networkRequest, connectivityCallback)
        }

        fun getInstance() = instance
    }
}