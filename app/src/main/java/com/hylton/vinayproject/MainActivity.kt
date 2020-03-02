package com.hylton.vinayproject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONException

const val API_KEY = "c8b6581188030b2fde6f306360757296"
const val PERMISSION_ID = 1

class MainActivity : AppCompatActivity(), UserCustomAdapter.OnUserClickListener {

    private val newUserActivityRequestCode = 1
    var baseUrl: String = "http://api.weatherstack.com/current?access_key=" + API_KEY + "&query=New York&units=f&interval=6"

    private lateinit var locationTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var feelsLikeTextView: TextView
    private lateinit var weatherDescriptionTextView: TextView
    private lateinit var imageView: ImageView

    private lateinit var requestQueue : RequestQueue

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserCustomAdapter

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)
        locationTextView = findViewById(R.id.city_text_field)
        temperatureTextView = findViewById(R.id.temp_text_field)
        feelsLikeTextView = findViewById(R.id.feels_like_text_field)
        weatherDescriptionTextView = findViewById(R.id.weather_desc_text_field)
        imageView = findViewById(R.id.image_view)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = UserCustomAdapter(this)
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
            //returnArray(baseUrl)
        }
    }

    private fun returnArray(url: String) {

        Log.d("Alele", "returnArray(url)")
        val arrayList = mutableListOf<String>()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                try {

                    Log.d("Alele", "Try block")
                    Log.d("Alele", response.toString())

                    val locationJsonObject = response.getJSONObject("location")
                    val currentJsonObject = response.getJSONObject("current")

                    var locationName = locationJsonObject.getString("name")
                    var temperature = currentJsonObject.getString("temperature")
                    var feelsLike = currentJsonObject.getString("feelslike")
                    var weatherDescription = currentJsonObject.getJSONArray("weather_descriptions").getString(0)
                    var weatherIcon = currentJsonObject.getJSONArray("weather_icons").getString(0)

                    Log.d("Alele", " locationName : ${locationName}")
                    Log.d("Alele", " temperature : ${temperature}")
                    Log.d("Alele", " feels like : ${feelsLike}")
                    Log.d("Alele", " weatherDescription : $weatherDescription")
                    Log.d("Alele", " weatherIcon : $weatherIcon")

                    arrayList.apply {
                        add(locationName)
                        add(temperature)
                        add(feelsLike)
                        add(weatherDescription)
                        add(weatherIcon)
                    }.also {
                        getResultFromApi(it)
                    }

                    Log.d("Alele", " arrayList[1] : ${arrayList[0]}")
                    Log.d("Alele", " arrayList[1] : ${arrayList[1]}")
                    Log.d("Alele", " arrayList[1] : ${arrayList[2]}")
                    Log.d("Alele", " arrayList[1] : ${arrayList[3]}")
                    Log.d("Alele", " arrayList[1] : ${arrayList[4]}")

                } catch (e: JSONException) {
                    Log.d("Alele", "catch block")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                it.printStackTrace()
                Log.d("Alele", it.printStackTrace().toString())
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun getResultFromApi(input: MutableList<String>) {
        setText(input)
    }

    private fun setText(input: MutableList<String>){
        locationTextView.text = input[0]
        temperatureTextView.text = input[1]
        feelsLikeTextView.text = input[2]
        weatherDescriptionTextView.text = "feels like " + input[3]
        Picasso.get().load(input[4]).into(imageView)
    }

    // This checks if the user has granted permission to use their location
    private fun checkPermissions(): Boolean{
        if ((ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) &&
            (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)){
            return true
        }
        return false
    }

    // This method will request our necessary permissions to the user if not granted
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }

    // This returns the user's location, Longitude and Latitude
    private fun userLocation(){

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

    // This populates the Menu with the MenuItems
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.delete_menu, menu)
        return true
    }

    // This gives you the ability to to select a Menu Option
    // and give it some functionality
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
