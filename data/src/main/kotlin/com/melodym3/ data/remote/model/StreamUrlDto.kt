package com.melodym3.data.remote.model

import com.google.gson.annotations.SerializedName

data class StreamUrlDto(
    @SerializedName("url")
    val url: String,
    
    @SerializedName("format")
    val format: String? = null
)
