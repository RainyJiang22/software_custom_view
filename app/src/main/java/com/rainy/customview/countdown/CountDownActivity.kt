package com.rainy.customview.countdown

import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.rainy.customview.base.BaseActivity
import com.rainy.customview.databinding.ActivityCountDownBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author jiangshiyu
 * @date 2022/10/25
 */
class CountDownActivity : BaseActivity<ActivityCountDownBinding, CountDownViewModel>() {

    companion object {
        //倒计时30分钟
        const val COUNTDOWN_IN_MILL = 30 * 60 * 1000
        val COUNTDOWN_SDF = SimpleDateFormat("mm:ss:SS", Locale.US)
    }

    override fun onBundle(bundle: Bundle) {
    }

    override fun init(savedInstanceState: Bundle?) {
        viewModel.countdownCoroutines(COUNTDOWN_IN_MILL, {
           try {
               COUNTDOWN_SDF.format(it).apply {
                   val times = this.split(":")
                   binding?.tvSubCountdownMin?.text = times[0]
                   binding?.tvSubCountdownSec?.text = times[1]
                   binding?.tvSubCountdownMill?.text = times[2]
               }
           }catch (e:Throwable) {
               e.printStackTrace()
           }
        }, {
            ToastUtils.showShort("countdown finish")
        })
    }

    override fun createObserver() {
    }
}