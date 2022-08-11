package com.rainy.customview.scenery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rainy.customview.R
import kotlinx.android.synthetic.main.activity_scenery.*

/**
 * @author jiangshiyu
 * @date 2022/8/11
 */
class SceneryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scenery)

        scenery_view.playAnimator()
        scenery_view.setOnAnimationListener(object : DribbleSceneryView.AnimationListener {
            override fun onAnimationEnd() {
                scenery_view.playAnimator()
            }

        })
    }
}