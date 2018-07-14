package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class Summary(
	@SerializedName("count") val count: Int = -1,
	@SerializedName("items") val items: MutableList<Item> = mutableListOf()
) {
	data class Item(
		@SerializedName("room_id") val roomId: Int = -1,
		@SerializedName("room_name") val roomName: String = "",
		@SerializedName("days") val days: MutableList<Day> = mutableListOf()
	) {
		data class Day(
			@SerializedName("last_update") val lastUpdate: String = "",
			@SerializedName("day") val day: String = ""
		)
	}
}