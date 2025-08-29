package com.ariftuncer.ne_yesem.di
import com.ariftuncer.ne_yesem.data.repository.PantryRepositoryImpl
import com.ariftuncer.ne_yesem.domain.repository.PantryRepository
import com.ariftuncer.ne_yesem.domain.usecase.pantry.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PantryBindModule {
    @Binds @Singleton
    abstract fun bindPantryRepo(impl: PantryRepositoryImpl): PantryRepository
}

@Module
@InstallIn(SingletonComponent::class)
object PantryUseCaseModule {
    @Provides @Singleton fun provideAdd(repo: PantryRepository) = AddPantryItemUseCase(repo)
    @Provides @Singleton fun provideUpdate(repo: PantryRepository) = UpdatePantryItemUseCase(repo)
    @Provides @Singleton fun provideDelete(repo: PantryRepository) = DeletePantryItemUseCase(repo)
    @Provides @Singleton fun provideGetByCat(repo: PantryRepository) = GetItemsByCategoryUseCase(repo)
    @Provides @Singleton fun provideGetAll(repo: PantryRepository) = GetAllPantryItemsUseCase(repo)
    @Provides @Singleton fun provideUpdateExpiry(repo: PantryRepository) = UpdateExpiryUseCase(repo)
}
