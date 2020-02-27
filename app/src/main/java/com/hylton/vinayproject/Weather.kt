package com.hylton.vinayproject

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException

class Weather() {

    private fun jsonParse(queue: RequestQueue, url: String) : Unit{
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->

                try {
                    val jsonArray = response.getJSONObject("response").getJSONArray("docs")

                    for (i in 0 until jsonArray.length()){
                        var docs = jsonArray.getJSONObject(i)

                        var id = docs.getString("id")
                        var journal = docs.getString("journal")
                        var eissn = docs.getString("eissn")


                    }
                }
                catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                it.printStackTrace()
            })

        // add the queue to the request
        queue.add(jsonObjectRequest)
    }
}