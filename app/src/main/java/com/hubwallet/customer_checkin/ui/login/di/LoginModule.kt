package com.hubwallet.customer_checkin.ui.login.di

import com.hubwallet.customer_checkin.ui.login.data.LoginRepository
import com.hubwallet.customer_checkin.ui.login.network.LoginApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
class LoginModule {
    @Provides
     fun providesLoginApi(retrofit: Retrofit):LoginApi{
        return retrofit.create(LoginApi::class.java)
    }
    @Provides
     fun providesLoginRepository(api: LoginApi):LoginRepository{
        return LoginRepository(api)
    }
}