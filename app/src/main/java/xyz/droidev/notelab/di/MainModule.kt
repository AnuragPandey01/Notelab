package xyz.droidev.notelab.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Databases
import xyz.droidev.notelab.data.local.PrefManager
import xyz.droidev.notelab.data.repository.AuthRepository
import xyz.droidev.notelab.data.repository.NotesRepository
import javax.inject.Singleton

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideAppWriteClient(
        @ApplicationContext context: Context
    ) = Client(context)
        .setEndpoint("https://cloud.appwrite.io/v1")
        .setProject("6689857600380fe1992c")
        .setSelfSigned(true)

    @Provides
    @Singleton
    fun provideAppWriteDatabase(client: Client) = Databases(client)

    @Provides
    @Singleton
    fun provideAuthRepository(
        client: Client,
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(client, PrefManager(context))
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        databases: Databases,
        client: Client,
        @ApplicationContext context: Context
    ): NotesRepository {
        return NotesRepository(databases,client,PrefManager(context))
    }



}