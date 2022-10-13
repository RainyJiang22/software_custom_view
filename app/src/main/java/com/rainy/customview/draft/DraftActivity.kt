package com.rainy.customview.draft

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rainy.customview.R
import kotlinx.android.synthetic.main.activity_draft.*

/**
 * @author jiangshiyu
 * @date 2022/10/11
 */
class DraftActivity : AppCompatActivity() {


    private var mFloatView: AvatarFloatView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)
        mFloatView = AvatarFloatView(this)

        btn_add_float_view.setOnClickListener {
            FloatManager.with(this).add(mFloatView!!)
                .setClick(object : BaseFloatView.OnFloatClickListener {
                    override fun onClick(view: View) {
                        Toast.makeText(this@DraftActivity, "click", Toast.LENGTH_SHORT).show()
                    }
                })
                .show()
        }

        btn_horizontal.setOnClickListener {
            mFloatView?.setAdsorbType(BaseFloatView.ADSORB_HORIZONTAL)
        }

        btn_vertical.setOnClickListener {
            mFloatView?.setAdsorbType(BaseFloatView.ADSORB_VERTICAL)
        }

        btn_remove_view.setOnClickListener {
            FloatManager.hide()
        }
    }
}