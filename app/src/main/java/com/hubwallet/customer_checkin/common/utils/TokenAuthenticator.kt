package com.hubwallet.customer_checkin.common.utils

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.hubwallet.customer_checkin.common.constants.ConstantValues
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class TokenAuthenticator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val pref = PreferenceManager(context = context)
        if (response.code == 401) {
            var token = ""

             runBlocking {
                val data = Retrofit.Builder().baseUrl(ConstantValues.baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .build().create(AuthenticatorTokenAPi::class.java)
                    .generateToken(pref.getValueString(PreferenceManager.tokenRefresh))
                try {
                    Log.e("authenticate: ",data.body().toString() )
                    val json = JSONObject(data.body().toString())
                    token = json.getString("token")
                    val refresh = json.getString("refresh_token")
                    Log.e( "authenticateToken ",token.toString() )
                    Log.e( "authenticateRefresh ",refresh.toString() )
                    pref.putValueString(PreferenceManager.token, "Bearer $token")
                    pref.putValueString(PreferenceManager.tokenRefresh, "Bearer $refresh")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                token
            }
            if (token.isNotEmpty()) {
                return response.request.newBuilder()
                    .header("Authorization", pref.getValueString(PreferenceManager.token))
                    .build()
            }
        }
        return null
    }

}
//    @Override
//    public Request authenticate(Proxy proxy, Response response) throws IOException
//    {
//        // Refresh your access_token using a synchronous api request
//        newAccessToken = service.refreshToken();
//
//        // Add new header to rejected request and retry it
//        return response.request().newBuilder()
//            .header(AUTHORIZATION, newAccessToken)
//            .build();
//    }
//
//    @Override
//    public Request authenticateProxy(Proxy proxy, Response response) throws IOException
//    {
//// Null indicates no attempt to authenticate.
//