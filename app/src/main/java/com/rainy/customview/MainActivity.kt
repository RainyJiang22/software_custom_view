package com.rainy.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rainy.customview.scenery.DribbleSceneryView
import kotlinx.android.synthetic.main.activity_main.*

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
    }
}