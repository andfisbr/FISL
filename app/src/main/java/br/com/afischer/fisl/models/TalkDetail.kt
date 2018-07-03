package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class TalkDetail(
	@SerializedName("resource") val resource: Resource = Resource()
)