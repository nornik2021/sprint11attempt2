package com.example.sprint11attempt2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val searchHistory: SearchHistory):RecyclerView.Adapter<TracksViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder { // в этом методе создается элемент ViewHolder
        // в процессе работы с onCreateViewHolder мы получаем из xml-разметки элемента готовый к работе объект ViewHolder с инициилизированными
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.track_view,parent,false)// вью надуваем содержимым из разметки
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) { // здесь происходит установка значений UI компонент из модели данных
        holder.bind(tracks[position]) // вызываем bind созданный в TracksViewHolder, и передаем в качестве параметра объект tracs
        holder.itemView.setOnClickListener {

            searchHistory.saveItem(tracks[position])


        }
    }

    override fun getItemCount(): Int { // возвращает количество элементов списка
        return tracks.size
    }
}