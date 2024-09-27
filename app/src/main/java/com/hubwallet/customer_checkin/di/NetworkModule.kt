package com.hubwallet.customer_checkin.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hubwallet.customer_checkin.common.constants.ConstantValues
import com.hubwallet.customer_checkin.common.utils.IsNetworkPresent
import com.hubwallet.customer_checkin.common.utils.NetworkClient
import com.hubwallet.customer_checkin.common.utils.PreferenceManager
import com.hubwallet.customer_checkin.common.utils.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

//        if (BuildConfig.DEBUG) {
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
//        }
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun providesAuthenticator(@ApplicationContext context: Context): TokenAuthenticator {
        return TokenAuthenticator(context)
    }

    @Singleton
    @Provides
    fun providesHttpsClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context,
        pref: PreferenceManager,
        isNetworkPresent: IsNetworkPresent,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val builder: Headers.Builder = Headers.Builder()
                builder.add("Authorization", pref.getValueString(PreferenceManager.token))
                val value = builder.build()
                val request: Request = chain.request().newBuilder().headers(value).build()
                chain.proceed(request)
            }
            .callTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(NetworkClient(context, isNetworkPresent))
            .build()

    @Singleton
    @Provides
    fun providesGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder().baseUrl(ConstantValues.baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
}