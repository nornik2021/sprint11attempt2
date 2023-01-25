package com.example.sprint11attempt2

import android.content.SharedPreferences
import com.example.sprint11attempt2.App.Companion.SEARCH_HISTORY_KEY
import com.google.gson.Gson


class SearchHistory(private val sharedPrefs: SharedPreferences) {

    var tracksHistory = ArrayList<Track>()
    private val gson = Gson()


    fun loadTracksFromJson() {

        val json = sharedPrefs.getString(SearchActivity.SEARCH_HISTORY_KEY, "")
        if (json !== "") tracksHistory.addAll(gson.fromJson(json, Array<Track>::class.java))
    }

    fun saveItem(newTrack: Track) { // сохраняем трек
        for (track in tracksHistory) {  // если в истории уже есть этот трек - удалить трек
            if (track.trackId == newTrack.trackId) { // если Ийди трека равно новому треку
                tracksHistory.remove(track) // перезаписать новый трек в историю
                break // конец цикла
            }
        }
        tracksHistory.add(0, newTrack) // добавить в исторю 10 треков
        if (tracksHistory.size > SEARCH_HISTORY_SIZE) {  // если список больше 10
            tracksHistory.removeLast() // перезаписать последний трек
        }
        sharedPrefs.edit() // вводим переменную в режим редактирования
            .putString(SearchActivity.SEARCH_HISTORY_KEY, toJson()) // кладем строку
            .apply() // сохраняем изменения
    }

    fun deleteItems() { // очистить историю
        tracksHistory.clear()
        sharedPrefs.edit().remove(SearchActivity.SEARCH_HISTORY_KEY).apply()
    }

    private fun toJson(): String {
        return gson.toJson(tracksHistory)
    }

   companion object { // константа
        const val SEARCH_HISTORY_SIZE = 10
    }
}