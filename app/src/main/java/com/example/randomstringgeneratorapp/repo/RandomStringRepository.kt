package com.example.randomstringgeneratorapp.repo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.randomstringgeneratorapp.models.RandomStringData
import org.json.JSONObject
import javax.inject.Inject

class RandomStringRepository @Inject constructor(private val context: Context) {

    private val URI: Uri = Uri.parse("content://com.iav.contestdataprovider/text")

    fun fetchRandomString(maxLength: Int): RandomStringData? {
        try {
            val resolver: ContentResolver = context.contentResolver
            val cursor = resolver.query(
                URI,
                arrayOf("data"),
                "lenght = ?",
                arrayOf(maxLength.toString()),
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val jsonData = it.getString(it.getColumnIndexOrThrow("data"))
                    val jsonObject = JSONObject(jsonData).getJSONObject("randomText")

                    return RandomStringData(
                        value = jsonObject.getString("value"),
                        length = jsonObject.getInt("length"),
                        created = jsonObject.getString("created")
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("RandomStringRepo", "Error fetching random string: ${e.message}")
        }
        return null
    }
}
