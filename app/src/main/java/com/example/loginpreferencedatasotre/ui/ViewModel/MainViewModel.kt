package com.example.loginpreferencedatasotre.ui.ViewModel

import androidx.datastore.dataStore
import androidx.lifecycle.*
import com.example.loginpreferencedatasotre.DataStore.Repository.DataStoreRepository
import com.example.loginpreferencedatasotre.DataStore.Repository.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (private val dataStoreRepository: DataStoreRepository)
    : ViewModel() {

    val userPreferences = dataStoreRepository.readFromDataStore.asLiveData()

//    fun getUserPreferences() { //, remember: Boolean
//        viewModelScope.launch(Dispatchers.IO) {
//            dataStoreRepository.getUser().asLiveData() //, remember
//        }
//    }

    fun saveUser(name: String, password: String) {
        viewModelScope.launch {
            dataStoreRepository.setUser(name, password)
        }}

    fun getUserLogin(): LiveData<Boolean> {
        return dataStoreRepository.getUserLogin().asLiveData()
    }

    fun saveUserLogged(islogin : Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setUserLogin(islogin)
        }}

        fun getUser(): LiveData<UserPreferences> {
            return dataStoreRepository.getUser().asLiveData()
        }


        fun saveUserPreferences(
            username: String,
            password: String,
            remember: Boolean
        ) { //, remember: Boolean
            viewModelScope.launch(Dispatchers.IO) {
                dataStoreRepository.saveToDataStore(username, password, remember) //, remember
            }
        }

        fun clearUserPreferences() {
            viewModelScope.launch(Dispatchers.IO) {
                dataStoreRepository.clearDataStore()
            }
        }
    }

    class LoginViewModelFactory(
        private val dataStoreRepository: DataStoreRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(dataStoreRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
