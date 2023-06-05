package com.rainy.customview.fontview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.rainy.customview.R
import com.rainy.dp
import kotlinx.android.synthetic.main.activity_font.*


/**
 * @author jiangshiyu
 * @date 2023/6/4
 */
class FontActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font)


        item_card.layoutParams.apply {
            this.width = 300.dp.toInt()
            this.height = 600.dp.toInt()
        }


        item_view.layoutParams.apply {
            this.width = item_card.layoutParams.width
            this.height = item_card.layoutParams.height
        }
        item_view.rotation = 5f
        setMargins(item_view, 10.dp.toInt(), 0, 0, 0)
    }

    private fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }


}