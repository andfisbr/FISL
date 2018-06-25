package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName
data class Resource(
        @SerializedName("coauthors") var coauthors: MutableList<Any>? = null,
        @SerializedName("full") var full: String = "",
        @SerializedName("id") var id: Int = -1,
        @SerializedName("owner") var owner: Owner? = null,
        @SerializedName("slots") var slots: MutableList<Slot>? = null,
        @SerializedName("title") var title: String = "",
        @SerializedName("track") var track: String = ""
)