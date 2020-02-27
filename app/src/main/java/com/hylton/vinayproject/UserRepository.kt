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
    suspend fun returnArray(url: String){

        Log.d("Alele", "returnArray(url)")
        val arrayList = mutableListOf<String>()
        // withContext(Dispatchers.IO){

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                try {

                    Log.d("Alele", "Try block")
                    var jsonObject = response.getJSONObject("response").getJSONArray("docs").getJSONObject(0)

                    Log.d("Alele", jsonObject.toString())

                    var id = jsonObject.getString("id")
                    Log.d("Alele", id.toString())

                    var journal = jsonObject.getString("journal")
                    Log.d("Alele", journal)

                    arrayList.apply {
                        add(id)
                        add(journal)
                    }.let {
                        getResultFromApi(it[0], it[1]).add(it[0])
                    }

                    Log.d("Alele", " id : ${arrayList[0]}")
                    Log.d("Alele", " journal : ${arrayList[1]}")

                } catch (e: JSONException) {
                    Log.d("Alele", "catch block")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                it.printStackTrace()
                Log.d("Alele", it.printStackTrace().toString())
            })

        //requestQueue.add(jsonObjectRequest)
    }

    fun getResultFromApi(id: String, journal: String) : MutableList<String> {

        Log.d("Alele", " getResultFromApi() => id : $id")
        Log.d("Alele", " getResultFromApi() => journal : $journal")

        val list: MutableList<String> = mutableListOf()
        list.add(id)
        list.add(journal)

        return list
    }
}