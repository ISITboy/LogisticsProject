package com.example.diploma.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.diploma.data.local.room.Database
import com.example.diploma.data.local.room.ProducerDao
import com.example.diploma.data.local.room.TransportDao
import com.example.diploma.data.remote.RoutesApi
import com.example.diploma.data.remote.utils.Constants.BASE_URL
import com.example.diploma.domain.Constants.DATABASE_NAME
import com.example.mapkitresultproject.data.local.room.ConsumerDao
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): Database {
        return Room.databaseBuilder(
            context = application,
            klass = Database::class.java,
            name = DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideShipperDao(
        database: Database
    ): ProducerDao = database.producerDao

    @Provides
    @Singleton
    fun provideTransportDao(
        database: Database
    ): TransportDao = database.transportDao

    @Provides
    @Singleton
    fun provideConsigneeDao(
        database: Database
    ): ConsumerDao = database.consumerDao


    @Singleton
    @Provides
    fun providesPostService(retrofit: Retrofit) = retrofit.create(RoutesApi::class.java)

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()



    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseDatabase = FirebaseDatabase.getInstance("https://fir-practic-80202-default-rtdb.europe-west1.firebasedatabase.app/")

}