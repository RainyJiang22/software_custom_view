package com.rainy.customview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rainy.customview.countdown.CountDownActivity
import com.rainy.customview.draft.DraftActivity
import com.rainy.customview.loading.LoadingActivity
import com.rainy.customview.noise.NoiseSplineActivity
import com.rainy.customview.scenery.SceneryActivity
import com.rainy.customview.scratch.ScratchActivity
import com.rainy.customview.shine.ShineActivity
import com.rainy.customview.slide.SlideActivity

/**
 * @author jiangshiyu
 * @date 2022/8/11
 */
class RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)
    }


    fun scenery(view: View) {
        startActivity(Intent(this, SceneryActivity::class.java))
    }

    fun scratch(view: View) {
        startActivity(Intent(this, ScratchActivity::class.java))
    }

    fun loading(view: View) {
        startActivity(Intent(this, LoadingActivity::class.java))
    }

    fun slide(view: View) {
        startActivity(Intent(this, SlideActivity::class.java))
    }

    fun shine(view: View) {
        startActivity(Intent(this, ShineActivity::class.java))
    }

    fun noise(view: View) {
        startActivity(Intent(this, NoiseSplineActivity::class.java))
    }

    fun draft(view: View) {
        startActivity(Intent(this, DraftActivity::class.java))
    }

    fun countdown(view: View) {
        startActivity(Intent(this, CountDownActivity::class.java))
    }

}