package com.rainy.customview.loading

import android.os.Bundle
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.shape.CornerFamily
import com.rainy.customview.R
import com.rainy.customview.base.BaseActivity
import com.rainy.customview.databinding.ActivityShimmerBinding
import com.rainy.dp
import kotlinx.android.synthetic.main.activity_shimmer.*
import kotlinx.coroutines.delay

/**
 * @author jiangshiyu
 * @date 2023/4/30
 */
class ShimmerActivity : BaseActivity<ActivityShimmerBinding, AndroidViewModel>() {


    private var mAdapter: ShimmerAdapter? = null

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

//        //set the ShapeAppearanceModel to CornerFamily.ROUNDED and the radius in pixels
//        binding?.shapeImage?.apply {
//            shapeAppearanceModel =
//                this.shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, 4.dp)
//                    .build()
//        }
        val url = "http://baidu.com/1.png"

        val list = ArrayList<ImageBean>()
        repeat(10) {
            list.add(ImageBean(url))
        }

        mAdapter = ShimmerAdapter(list)


        rv_shimmer.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rv_shimmer.adapter = mAdapter
        rv_shimmer.showShimmerAdapter()

        lifecycleScope.launchWhenResumed {
            delay(3000L)
            rv_shimmer.hideShimmerAdapter()
        }


//        //load url from Glide and add shimmerDrawable as placeholder
//        binding?.shapeImage?.let {
//            Glide.with(this).load(url)
//                .placeholder(shimmerDrawable)
//                .into(it)
//        }


    }

    override fun createObserver() {
    }
}