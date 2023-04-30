package com.rainy.customview.loading

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.shape.CornerFamily
import com.rainy.customview.R
import com.rainy.customview.base.BaseActivity
import com.rainy.customview.databinding.ActivityShimmerBinding
import com.rainy.dp

/**
 * @author jiangshiyu
 * @date 2023/4/30
 */
class ShimmerActivity : BaseActivity<ActivityShimmerBinding, AndroidViewModel>() {


    override fun onBundle(bundle: Bundle) {

    }

    override fun init(savedInstanceState: Bundle?) {

        //initialize shimmer
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(ContextCompat.getColor(this, R.color.face_item_background))
            .setHighlightAlpha(0.6f)
            .setHighlightColor(ContextCompat.getColor(this, R.color.shimmer_color))
            .setDuration(1800)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

        //create ShimmerDrawable()
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        //set the ShapeAppearanceModel to CornerFamily.ROUNDED and the radius in pixels
        binding?.shapeImage?.apply {
            shapeAppearanceModel =
                this.shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, 4.dp)
                    .build()
        }
        val url = "http://baidu.com/1.png"

        //load url from Glide and add shimmerDrawable as placeholder
        binding?.shapeImage?.let {
            Glide.with(this).load(url)
                .placeholder(shimmerDrawable)
                .into(it)
        }


    }

    override fun createObserver() {
    }
}