package com.rainy.customview.loading

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.rainy.customview.R
import com.rainy.dp

/**
 * @author jiangshiyu
 * @date 2023/5/1
 */

data class ImageBean(val url:String)

class ShimmerAdapter(list:List<ImageBean>) : BaseQuickAdapter<ImageBean,BaseViewHolder>(R.layout.layout_item_image,list.toMutableList()) {

    override fun convert(holder: BaseViewHolder, item: ImageBean) {

        holder.getView<ShapeableImageView>(R.id.iv_cover).apply {
            shapeAppearanceModel =
                this.shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, 4.dp)
                    .build()
        }

    }

}