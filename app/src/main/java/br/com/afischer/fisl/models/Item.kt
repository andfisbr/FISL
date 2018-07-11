package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName


data class Item(
        @SerializedName("begins") var begins: String = "",
        @SerializedName("duration") var duration: Int = -1,
        @SerializedName("hour") var hour: Int = -1,
        @SerializedName("id") var id: Int = -1,
        @SerializedName("last_updated") var lastUpdated: String = "",
        @SerializedName("recordings") var recordings: MutableList<String> = mutableListOf(),
        @SerializedName("room") var room: Int = -1,
        @SerializedName("room_name") var roomName: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("talk") var talk: Talk? = null
) {
        var alarmID: Int = -1
        var keywords: MutableList<String> = mutableListOf()
        
        
        
        data class Talk(
                @SerializedName("coauthors") var coauthors: MutableList<String> = mutableListOf(),
                @SerializedName("id") var id: Int = -1,
                @SerializedName("last_updated") var lastUpdated: String = "",
                @SerializedName("owner") var owner: String = "",
                @SerializedName("owner_email") var ownerEmail: String = "",
                @SerializedName("status") var status: String = "",
                @SerializedName("title") var title: String = "",
                @SerializedName("track") var track: String = ""
        )
}