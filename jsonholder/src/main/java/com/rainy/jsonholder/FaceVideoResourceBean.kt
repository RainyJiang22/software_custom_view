package com.rainy.jsonholder

import com.google.gson.annotations.SerializedName

/**
 * @author jiangshiyu
 * @date 2023/4/24
 */
data class FaceVideoResourceBean(
    //素材id
    @SerializedName("id")
    val id: Int,

    //素材名称
    @SerializedName("name")
    val name: String,

    //素材封面
    @SerializedName("cover")
    val cover: String,

    //素材第一帧
    @SerializedName("first_frame")
    val firstFrame: String,

    //素材资源
    @SerializedName("video")
    val video: String,

    //分类名称
    @SerializedName("tab_title")
    val tabTitle: String,


    //是否热门 0:否 1:是
    @SerializedName("is_hot")
    val isHot: Int,

    //权重
    @SerializedName("weight")
    val weight: Int,

    //是否展示
    @SerializedName("is_show")
    val isShow: Int,

    //是否vip素材
    @SerializedName("is_vip")
    val isVip: Int,
)