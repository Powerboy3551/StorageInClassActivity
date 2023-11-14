package com.example.networkapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save and load comic info automatically when app starts)

    private lateinit var requestQueue: RequestQueue
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var numberEditText: EditText
    private lateinit var showButton: Button
    private lateinit var comicImageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById(R.id.comicTitleTextView)
        descriptionTextView = findViewById(R.id.comicDescriptionTextView)
        numberEditText = findViewById(R.id.comicNumberEditText)
        showButton = findViewById(R.id.showComicButton)
        comicImageView = findViewById(R.id.comicImageView)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }

        loadComic()
    }

    private fun downloadComic(comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add(
            JsonObjectRequest(url, { showComic(it) }, {})
        )
    }

    private fun showComic(comicObject: JSONObject) {
        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")
        Picasso.get().load(comicObject.getString("img")).into(comicImageView)

        saveComic(comicObject)
    }

    private fun saveComic(comicObject: JSONObject) {
        with(sharedPreferences.edit()) {
            putString("title", comicObject.getString("title"))
            putString("description", comicObject.getString("alt"))
            putString("image_url", comicObject.getString("img"))
            apply()
        }
    }
    private fun loadComic() {
        val title = sharedPreferences.getString("title", "")
        val description = sharedPreferences.getString("description", "")
        val imageUrl = sharedPreferences.getString("image_url", "")

        titleTextView.text = title
        descriptionTextView.text = description
        Picasso.get().load(imageUrl).into(comicImageView)
    }
}
