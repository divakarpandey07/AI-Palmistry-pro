package com.example.palmistry.di

import android.content.Context
import com.example.palmistry.data.local.PalmistryDatabase
import com.example.palmistry.data.local.ReadingDao
import com.example.palmistry.network.PalmistryApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://your-fastapi-backend.onrender.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePalmistryApiService(retrofit: Retrofit): PalmistryApiService {
        return retrofit.create(PalmistryApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePalmistryDatabase(@ApplicationContext context: Context): PalmistryDatabase {
        return PalmistryDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideReadingDao(db: PalmistryDatabase): ReadingDao {
        return db.readingDao()
    }
}
