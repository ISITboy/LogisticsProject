package com.example.diploma.di

import com.example.diploma.data.impl.service.AccountServiceImpl
import com.example.diploma.data.impl.repository.ConsumerRepositoryImpl
import com.example.diploma.data.impl.repository.MapKitRoutingRepositoryImpl
import com.example.diploma.data.impl.repository.MapKitSearchRepositoryImpl
import com.example.diploma.data.impl.repository.OpenRouteServiceRepositoryImpl
import com.example.diploma.data.impl.repository.ProducerRepositoryImpl
import com.example.diploma.data.impl.repository.TransportRepositoryImpl
import com.example.diploma.data.impl.service.RealtimeServiceImpl
import com.example.diploma.domain.repository.ConsumerRepository
import com.example.diploma.domain.repository.MapKitRoutingRepository
import com.example.diploma.domain.repository.MapKitSearchRepository
import com.example.diploma.domain.repository.OpenRouteServiceRepository
import com.example.diploma.domain.repository.ProducerRepository
import com.example.diploma.domain.repository.TransportRepository
import com.example.diploma.domain.service.AccountService
import com.example.diploma.domain.service.RealtimeService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MapKitRepositoryModule {
    @Binds
    abstract fun provideMapKitSearchRepository(impl: MapKitSearchRepositoryImpl):MapKitSearchRepository
    @Binds
    abstract fun provideMapKitCreateRouteRepository(impl: MapKitRoutingRepositoryImpl):MapKitRoutingRepository
    @Binds
    abstract fun provideConsumerRepository(impl: ConsumerRepositoryImpl):ConsumerRepository
    @Binds
    abstract fun provideProviderRepository(impl: ProducerRepositoryImpl):ProducerRepository
    @Binds
    abstract fun provideTransportRepository(impl: TransportRepositoryImpl): TransportRepository
    @Binds
    abstract fun provideOpenRouteServiceRepository(impl: OpenRouteServiceRepositoryImpl):OpenRouteServiceRepository
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
    @Binds
    abstract fun provideRealtimeService(impl: RealtimeServiceImpl): RealtimeService
}