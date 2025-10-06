package com.ifucolo.rickandmorty.data.remote.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.ifucolo.rickandmorty.data.remote.api.RickAndMortyApi
import com.ifucolo.rickandmorty.data.remote.di.qualifiers.RickAndMortyApiBaseUrl
import com.ifucolo.rickandmorty.data.remote.di.qualifiers.RickAndMortyRetrofit
import com.ifucolo.rickandmorty.data.remote.network.NetworkMonitor
import com.ifucolo.rickandmorty.data.remote.network.NetworkMonitorImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    @RickAndMortyApiBaseUrl
    fun provideRickAndMortyApiBaseUrl(): HttpUrl = "https://rickandmortyapi.com/api/".toHttpUrl()


    @Provides
    @Singleton
    @RickAndMortyRetrofit
    fun provideRetrofit(
        client: OkHttpClient,
        @RickAndMortyApiBaseUrl baseUrl: HttpUrl
    ): Retrofit  {
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        val contentType = "application/json".toMediaType()
        val converter = json.asConverterFactory(contentType)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converter)
            .build()
    }

    @Provides
    @Singleton
    fun provideOrdersApi(@RickAndMortyRetrofit retrofit: Retrofit): RickAndMortyApi =
        retrofit.create()

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        connectivityManager: ConnectivityManager
    ): NetworkMonitor {
        return NetworkMonitorImpl(connectivityManager = connectivityManager)
    }

}