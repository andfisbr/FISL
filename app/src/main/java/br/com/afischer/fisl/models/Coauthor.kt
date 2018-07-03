package br.com.afischer.fisl.models

import com.google.gson.annotations.SerializedName

data class Coauthor(
        @SerializedName("name") var name: String = "",
        @SerializedName("resume") var resume: String = ""
)