package com.rainy.customview.countdown

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.util.*

/**
 * @author jiangshiyu
 * @date 2022/10/25
 */
class CountDownViewModel(application: Application) : AndroidViewModel(application) {


    /**
     * 使用协程倒计时
     */
    fun countdownCoroutines(total: Int, onTick: (Int) -> Unit, onFinish: () -> Unit): Job {
        return flow {
            for (i in total downTo 0 step 10) {
                emit(i)
                delay(10)
            }
        }.flowOn(Dispatchers.Default)
            .onStart {
                Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            }
            .onCompletion {
                onFinish.invoke()
            }.onEach {
                onTick.invoke(it)
            }.flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
    }

}