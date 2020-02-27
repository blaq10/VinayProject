package com.hylton.vinayproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONException

class MainActivity : AppCompatActivity(), UserCustomAdapter.OnUserClickListener {

    private lateinit var requestQueue: RequestQueue
    private lateinit var locationTextView: TextView
    private lateinit var temperatureTextView: TextView

    private val newUserActivityRequestCode = 1
    private var url: String = "https://api.myjson.com/bins/14yf50"

    private lateinit var userViewModel: UserViewModel

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)
        locationTextView = findViewById(R.id.location_text_view)
        temperatureTextView = findViewById(R.id.temperature_text_view)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = UserCustomAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.allUser.observe(this, Observer { users ->
            users?.let { adapter.setUser(it) }
        })

        fab = findViewById(R.id.fab)
        fab.setOnClickListener{
            val intent = Intent(this@MainActivity, AddUserActivity::class.java)
            startActivityForResult(intent, newUserActivityRequestCode)
        }
        CoroutineScope(IO).launch {
            returnArray(url)
        }
    }

    private  fun returnArray(url: String) : Unit {

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
                    }.also {
                        getResultFromApi(it)
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

        requestQueue?.add(jsonObjectRequest)
    }

    private  fun getResultFromApi(input: MutableList<String>) {
        setNewText(input)
    }

    private fun setNewText(input: MutableList<String>) {

        Log.d("Alele", " getResultFromApi() => id : ${input[0]}")
        Log.d("Alele", " getResultFromApi() => journal : ${input[1]}")

        setText(input)
    }

    fun setText(input: MutableList<String>){
        locationTextView.text = input[0]
        temperatureTextView.text = input[1]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newUserActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringArrayListExtra(AddUserActivity.EXTRA_REPLY)?.let {
                val user = User(it[0], it[1], it[2], Address(it[3], it[4], it[5], it[6].toInt()))
                userViewModel.insert(user)
            }
        } else {
            Toast.makeText(
                applicationContext,
                "not saved",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.delete_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.delete_all_menu_item -> {
                userViewModel.deleteAllUsers()
                Toast.makeText(this@MainActivity, "Delete Worked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onUserClick(position: Int) {
        Toast.makeText(this@MainActivity, position, Toast.LENGTH_SHORT).show()
    }
}
