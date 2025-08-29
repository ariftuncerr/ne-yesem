package com.ariftuncer.ne_yesem.di

import com.ariftuncer.ne_yesem.BuildConfig
import com.ariftuncer.ne_yesem.data.remote.api.SpoonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val req = chain.request()
        val url = req.url.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.SPOONACULAR_API_KEY)
            .build()
        chain.proceed(req.newBuilder().url(url).build())
    }

    @Provides @Singleton
    fun provideOkHttp(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideSpoonApi(retrofit: Retrofit): SpoonApi =
        retrofit.create(SpoonApi::class.java)
}
