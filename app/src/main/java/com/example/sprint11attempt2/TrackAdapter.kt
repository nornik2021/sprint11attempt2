package com.example.sprint11attempt2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter( private val context: Context, private val searchHistory: SearchHistory):RecyclerView.Adapter<TracksViewHolder>() {

    var tracks = ArrayList<Track>()
    private val gson = App.instance.gson

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder { // в этом методе создается элемент ViewHolder
        // в процессе работы с onCreateViewHolder мы получаем из xml-разметки элемента готовый к работе объект ViewHolder с инициилизированными
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.track_view,parent,false)// вью надуваем содержимым из разметки
        return TracksViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) { // здесь происходит установка значений UI компонент из модели данных
        holder.bind(tracks[position]) // вызываем bind созданный в TracksViewHolder, и передаем в качестве параметра объект tracs
        holder.itemView.setOnClickListener {
        val track = tracks[position]

            searchHistory.saveItem(track)
            val playerPres = Intent(context, PlayerActivity::class.java)
            val jTrack = gson.toJson(track)
            playerPres.putExtra(PlayerActivity.KEY_BUNDLE, jTrack)
            context.startActivity(playerPres)


        }
    }

    override fun getItemCount(): Int { // возвращает количество элементов списка
        return tracks.size
    }
}