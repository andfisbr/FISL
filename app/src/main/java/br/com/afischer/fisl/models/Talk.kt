package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class Talk(
	@SerializedName("resource") val resource: Resource = Resource()
) {
	
	data class Resource(
		@SerializedName("owner") val owner: Owner = Owner(),
		@SerializedName("slots") val slots: MutableList<Slot> = mutableListOf(),
		@SerializedName("id") val id: Int = -1,
		@SerializedName("coauthors") val coauthors: MutableList<Coauthor> = mutableListOf(),
		@SerializedName("title") val title: String = "",
		@SerializedName("track") val track: String = "",
		@SerializedName("full") val full: String = ""
	) {
		
		data class Owner(
			@SerializedName("resume") val resume: String = "",
			@SerializedName("name") val name: String = ""
		)
		
		
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
		
		
		data class Coauthor(
			@SerializedName("name") var name: String = "",
			@SerializedName("resume") var resume: String = ""
		)
	}
}