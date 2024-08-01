package com.rainy.customview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rainy.customview.countdown.CountDownActivity
import com.rainy.customview.draft.DraftActivity
import com.rainy.customview.fontview.FontActivity
import com.rainy.customview.hencoder.MainActivity
import com.rainy.customview.loading.LoadingActivity
import com.rainy.customview.loading.ShimmerActivity
import com.rainy.customview.noise.NoiseSplineActivity
import com.rainy.customview.notification.NotificationActivity
import com.rainy.customview.notification.TestActivity
import com.rainy.customview.randomtext.RandomTextActivity
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

    fun learnView(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun shimmerView(view: View) {
        startActivity(Intent(this, ShimmerActivity::class.java))
    }

    fun fontView(view: View) {
        startActivity(Intent(this, FontActivity::class.java))
    }


    fun notification(view: View) {
        startActivity(Intent(this, NotificationActivity::class.java))
    }

    fun notification2(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    fun randomText(view: View) {
        startActivity(Intent(this, RandomTextActivity::class.java))
    }

}