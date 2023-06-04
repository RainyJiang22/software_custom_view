package com.rainy.jsonholder

import com.google.gson.annotations.SerializedName

data class FaceVideoHotBean(
    //权重越大，越靠前
    @SerializedName("weight")
    val weight: Int? = 0,

    //是否展示
    @SerializedName("is_show")
    val isShow: String? = "1",

    //素材id
    @SerializedName("id")
    val id: String? = "0",
)
