package com.rainy.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rainy.customview.utils.ColorHelper
import com.rainy.customview.utils.RxTransformer
import com.rainy.dp
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        scenery_view.playAnimator()
//        scenery_view.setOnAnimationListener(object : DribbleSceneryView.AnimationListener {
//            override fun onAnimationEnd() {
//                scenery_view.playAnimator()
//            }
//
//        })
//
//        (iv_loading.layoutParams as ConstraintLayout.LayoutParams).let {
//            it.width = (260.dp + 10.dp).toInt()
//            it.height = (260.dp + 10.dp).toInt()
//        }
//        startProgress()


        slide_color.setColor(ContextCompat.getColor(this, R.color.purple_500))


    }

//    var distProgress: Disposable? = null
//    private fun startProgress() {
//        iv_loading.visibility = View.VISIBLE
////        总时长1.5秒
//        val totalTime = 1500
//        val totalProgress = 100
//        val period = (totalTime / totalProgress.toFloat()).toLong()
//        if (distProgress?.isDisposed == false) {
//            distProgress?.dispose()
//        }
//        distProgress =
//            Observable.intervalRange(
//                0,
//                totalProgress.toLong(),
//                0,
//                period,
//                TimeUnit.MILLISECONDS
//            ).compose(RxTransformer.async())
//                .subscribe {
//                    iv_loading?.progress = (it.toInt() / 100f)
//                }
//    }
}