package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class Day(
        @SerializedName("count") var count: Int,
        @SerializedName("items") var items: MutableList<Item>
)