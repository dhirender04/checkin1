package com.hubwallet.customer_checkin.common.utils

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

class NetworkClient @Inject constructor(
    @ApplicationContext context: Context,
    val isNetworkPresent: IsNetworkPresent
) : Interceptor {
    private val activeNetwork =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun intercept(chain: Interceptor.Chain): Response {

        val isConnected = activeNetwork.activeNetworkInfo != null &&
                activeNetwork.activeNetworkInfo?.isConnectedOrConnecting ?: false

        isNetworkPresent.isNetworkPresentLiveData.postValue(isConnected)

        if (!isConnected) {
            return Response.Builder()
                .code(418) // Whatever code
                .body(ResponseBody.create(null, "")) // Whatever body
                .protocol(Protocol.HTTP_2)
                .message("No Internet Connection")
                .request(chain.request())
                .build()
        } else return chain.proceed(chain.request());

    }
}