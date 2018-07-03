package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName
data class Slot(
        @SerializedName("begins") var begins: String = "",
        @SerializedName("duration") var duration: Int = -1,
        @SerializedName("hour") var hour: Int = -1,
        @SerializedName("id") var id: Int = -1,
        @SerializedName("last_updated") var lastUpdated: String = "",
        //@SerializedName("recordings") var recordings: MutableList<Any>? = null,
        @SerializedName("room") var room: Int = -1,
        @SerializedName("room_name") var roomName: String = "",
        @SerializedName("status") var status: String = ""
)