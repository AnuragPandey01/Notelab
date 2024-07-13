package xyz.droidev.notelab.data.repository

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.extensions.fromJson
import io.appwrite.extensions.toJson
import io.appwrite.models.Session
import io.appwrite.services.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.droidev.notelab.data.local.PrefManager
import xyz.droidev.notelab.util.NetworkResult

/**
 * Project : Notelab.
 * @author PANDEY ANURAG.
 */
class AuthRepository (
    client: Client,
    private val prefManager: PrefManager
) {

    private val account = Account(client)

    suspend fun login(
        email: String,
        password: String
    ): Flow<NetworkResult<Session>> = flow{
        emit(NetworkResult.Loading())
        try{
            val res = account.createEmailPasswordSession(email, password)
            prefManager.saveString(PrefManager.Keys.USER_ID, res.userId)
            emit(NetworkResult.Success(res))
        }catch (e: Exception){
            emit(NetworkResult.Error(e.message))
        }
    }

    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Flow<NetworkResult<Session>> = flow{
        emit(NetworkResult.Loading())
        try{
            val res = account.create(
                userId = ID.unique(),
                email = email,
                password = password,
                name = name
            )
            prefManager.saveString(PrefManager.Keys.USER_ID, res.id)
            emit(NetworkResult.Success(res.toJson().fromJson()))
        }catch (e: Exception){
            emit(NetworkResult.Error(e.message))
        }
    }

    suspend fun isLoggedIn(): Flow<NetworkResult<Boolean>> = flow{
        emit(NetworkResult.Loading())
        try{
            account.get()
            emit(NetworkResult.Success(true))
        }catch (e: Exception){
            emit(NetworkResult.Success(false))
        }
    }

    suspend fun logout(): Flow<NetworkResult<Boolean>> = flow{
        emit(NetworkResult.Loading())
        try{
            account.deleteSession("current")
            prefManager.clear()
            emit(NetworkResult.Success(true))
        }catch (e: Exception){
            emit(NetworkResult.Error(e.message))
        }
    }


}