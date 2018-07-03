package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class Resource(
	@SerializedName("owner") val owner: Owner = Owner(),
	@SerializedName("slots") val slots: MutableList<Slot> = mutableListOf(),
	@SerializedName("id") val id: Int = -1,
	@SerializedName("coauthors") val coauthors: MutableList<Coauthor> = mutableListOf(),
	@SerializedName("title") val title: String = "",
	@SerializedName("track") val track: String = "",
	@SerializedName("full") val full: String = ""
)