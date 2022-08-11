package com.rainy.customview.slide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rainy.customview.R
import kotlinx.android.synthetic.main.activity_slide.*

/**
 * @author jiangshiyu
 * @date 2022/8/11
 */
class SlideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)

        slide_color.setColor(ContextCompat.getColor(this, R.color.purple_500))

    }
}