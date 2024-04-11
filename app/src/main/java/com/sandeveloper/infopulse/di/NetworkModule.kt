package com.sandeveloper.infopulse.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.sandeveloper.Constants.BASE_URL
import com.sandeveloper.infopulse.api.NewsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    private var context: Context? = null

    fun AppModule(context: Context?) {
        this.context = context
    }
   //private var gson = GsonBuilder().setLenient().create()
    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
    }
    //for header purpose
    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun providesNewsAPI(retrofitBuilder: Retrofit.Builder): NewsAPI {
        return retrofitBuilder.build().create(NewsAPI::class.java)
    }



    @Provides
    @Singleton
    fun provideContext(): Context? {
        return context
    }
}