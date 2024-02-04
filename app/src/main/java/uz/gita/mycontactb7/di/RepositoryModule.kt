package uz.gita.mycontactb7.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.mycontactb7.domain.ContactRepository
import uz.gita.mycontactb7.domain.impl.ContactRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun getContactRepository(impl: ContactRepositoryImpl) : ContactRepository
}
