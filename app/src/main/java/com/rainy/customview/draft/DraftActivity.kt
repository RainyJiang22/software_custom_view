package com.rainy.customview.draft

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.rainy.customview.R
import kotlinx.android.synthetic.main.activity_draft.*

/**
 * @author jiangshiyu
 * @date 2022/10/11
 */
class DraftActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        btn_add_float_view.setOnClickListener {
            //通过decorView添加到
            val contentView =
                this.window.decorView.findViewById(android.R.id.content) as FrameLayout
            contentView.addView(FloatView(this))
        }
    }
}