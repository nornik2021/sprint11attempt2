package com.example.sprint11attempt2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var main_picture: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var timeRemained: TextView
    private lateinit var collectionName: TextView
    private lateinit var year: TextView
    private lateinit var arrowReturn: ImageView
    private lateinit var genre: TextView
    private lateinit var country: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val returnArrow = findViewById<ImageView>(R.id.return_arrow)
        initViews()
        returnArrow.setOnClickListener {
            finish()
        }
        val extras = intent.extras
        val stringTrack = extras?.getString(KEY_BUNDLE, "")

        val gson = App.instance.gson

        val track = gson.fromJson(stringTrack, Track::class.java)

        trackTitle.text = track.trackName
        artistName.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        timeRemained.text = duration.text
        collectionName.text = track.collectionName
        year.text = track.releaseDate
        genre.text = track.primaryGenreName
        country.text = track.country
        Glide.with(main_picture)
            .load(track.getArtworkUrl())
            .centerCrop()
            .transform(RoundedCorners(5))
            .placeholder(R.drawable.placeholder_image)
            .into(main_picture)

    }

    private fun initViews() {
        arrowReturn = findViewById(R.id.return_arrow)
        main_picture = findViewById(R.id.main_picture)
        trackTitle = findViewById(R.id.trackTitle)
        artistName = findViewById(R.id.artist_name)
        duration = findViewById(R.id.duration)
        timeRemained = findViewById(R.id.time_remained)
        collectionName = findViewById(R.id.collectionName)
        year = findViewById(R.id.year)
        genre = findViewById(R.id.genre)
        country = findViewById(R.id.country)
    }


    companion object {
        const val KEY_BUNDLE = "KEY_BUNDLE"
    }
}

