package com.example.sprint11attempt2

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class TracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.source_track_name)
    private val artistName: TextView = itemView.findViewById(R.id.source_artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.source_track_duration)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.source_album_art)

    fun bind(model: Track){
        trackName.text= model.trackName
        artistName.text = model.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)

        Glide.with(itemView)// этот метод создает и возвращает экземпляр класса для формирования и отправки запроса на загрузку изображения
            .load(model.artworkUrl100)// скачивает изображение по передаваемой в него ссылке
            .centerCrop()// выравнивает изображение
            .transform(RoundedCorners(5))// скругляет углы
            .placeholder(R.drawable.placeholder_image)// помещает изображение заглушку если не удалось скачать изображение из интернета
            .into(artworkUrl100)// скачивает изображени епо передаваемой в него ссылке

    }

}