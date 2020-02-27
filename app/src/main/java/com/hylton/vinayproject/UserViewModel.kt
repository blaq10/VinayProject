package com.hylton.vinayproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.RequestQueue
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    val allUser : LiveData<List<User>>
    private val url : String = "https://api.myjson.com/bins/12rkbs"

    init {
        val userDao = UserRoomDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUser = repository.allUsers
    }

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun deleteAllUsers() = viewModelScope.launch {
        repository.deleteAllUsers()
    }

    fun fetchJsonData() = viewModelScope.launch {
        repository.returnArray(url)
        //repository.getResultFromApi()
    }
}