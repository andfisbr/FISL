package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class Talk(
        @SerializedName("coauthors") var coauthors: MutableList<Any>? = null,
        @SerializedName("id") var id: Int = -1,
        @SerializedName("last_updated") var lastUpdated: String = "",
        @SerializedName("owner") var owner: String = "",
        @SerializedName("owner_email") var ownerEmail: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("track") var track: String = ""
)