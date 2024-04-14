package com.diagnal.forimageloading.data

import com.google.gson.annotations.SerializedName

data class ForImageDTOItem(
    @SerializedName("urls")
    val urls: Urls
)