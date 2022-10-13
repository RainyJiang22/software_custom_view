package com.rainy.customview.draft

import android.content.Context
import android.view.View
import com.google.android.material.imageview.ShapeableImageView
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2022/10/13
 */
class AvatarFloatView(context: Context) : BaseFloatView(context) {

    private var mAdsorbType = ADSORB_VERTICAL

    override fun getChildView(): View {
        val imageView = ShapeableImageView(context)
        imageView.setImageResource(R.drawable.ic_avatar)
        return imageView
    }

    override fun getIsCanDrag(): Boolean {
        return true
    }

    override fun getAdsorbType(): Int {
        return ADSORB_VERTICAL
    }

    fun setAdsorbType(type: Int) {
        mAdsorbType = type
    }
}