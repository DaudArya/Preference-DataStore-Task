package com.example.loginpreferencedatasotre.DataStore.Repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.loginpreferencedatasotre.DataStore.Repository.DataStoreRepository.PreferencesKeys.PASSWORD
import com.example.loginpreferencedatasotre.DataStore.Repository.DataStoreRepository.PreferencesKeys.REMEMBER
import com.example.loginpreferencedatasotre.DataStore.Repository.DataStoreRepository.PreferencesKeys.USERNAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreRepository (private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("Username")
        val PASSWORD = stringPreferencesKey("Password")
        val REMEMBER = booleanPreferencesKey("Remember")
    }

    suspend fun saveToDataStore(username: String,password: String , remember: Boolean) { //, remember: Boolean
        dataStore.edit { preference ->
            preference[USERNAME] = username
            preference[PASSWORD] = password
            preference[REMEMBER] = remember
        }
    }

    val readFromDataStore : Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStoreRepository", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val username = preference[USERNAME] ?: ""
            val password = preference[PASSWORD] ?: ""
            val remember = preference[REMEMBER] ?: false
            UserPreferences(username, password, remember)
        }

    suspend fun setUser(name: String, password: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = name
            preferences[PASSWORD] = password
        }
    }

    suspend fun setUserLogin(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER] = isLogin
        }
    }

    fun getUserLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[REMEMBER] ?: false
        }
    }

    suspend fun clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun removeUsername() {
        dataStore.edit { preference ->
            preference.remove(USERNAME)
        }
    }

    fun getUserLog(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[REMEMBER] ?: false
        }
    }
    fun getUser(): Flow<UserPreferences> {
        return dataStore.data.map { preferences ->
            UserPreferences(
                preferences[USERNAME] ?: "",
                preferences[PASSWORD] ?: "",
                preferences[REMEMBER] ?: false)
        }
    }



    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"

        val Context.dataStore by preferencesDataStore(
            name = USER_PREFERENCES_NAME
        )
    }
}