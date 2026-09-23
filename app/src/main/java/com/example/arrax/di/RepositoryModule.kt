package com.example.arrax.di


import com.example.arrax.data.repository.AuthRepositoryImpl
import com.example.arrax.data.repository.CatalogCutRepositoryImpl
import com.example.arrax.data.repository.EventRepositoryImpl
import com.example.arrax.data.repository.TenantRepositoryImpl
import com.example.arrax.domain.repository.AuthRepository
import com.example.arrax.domain.repository.CutCatalogRepository
import com.example.arrax.domain.repository.EventRepository
import com.example.arrax.domain.repository.TenantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTenantRepository(impl: TenantRepositoryImpl): TenantRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Binds
    @Singleton
    abstract fun bindCutCatalogRepository(impl: CatalogCutRepositoryImpl): CutCatalogRepository
}