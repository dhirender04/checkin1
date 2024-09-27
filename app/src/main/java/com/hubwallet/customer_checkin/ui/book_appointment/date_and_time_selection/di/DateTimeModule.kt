package com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.di

import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.network.DateTimeApi
import com.hubwallet.customer_checkin.ui.book_appointment.date_and_time_selection.repository.DateTimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class DateTimeModule {
    @Provides
    fun providesDateApi(retrofit: Retrofit): DateTimeApi {
        return retrofit.create(DateTimeApi::class.java)
    }

    @Provides
    fun providesStylistRepository(api: DateTimeApi): DateTimeRepository {
        return DateTimeRepository(api)
    }
}