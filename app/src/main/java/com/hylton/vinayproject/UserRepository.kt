package com.hylton.vinayproject

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject

class UserRepository(private val userDao: UserDao) {
    val allUsers: LiveData<List<User>> = userDao.getAllUsers()
    private val url : String = "https://api.myjson.com/bins/12rkbs"
    //val requestQueue: RequestQueue = Volley.newRequestQueue()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers(){
        userDao.deleteAllUsers()
    }

    // this suspends the coroutine before the network request
    // however it will still run on the main thread
}