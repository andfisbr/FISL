package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class Owner(
	@SerializedName("resume") val resume: String? = "",
	@SerializedName("name") val name: String = ""
)