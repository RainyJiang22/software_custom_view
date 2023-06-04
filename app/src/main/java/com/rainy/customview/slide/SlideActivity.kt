package com.rainy.customview.slide

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.google.gson.reflect.TypeToken
import com.rainy.customview.BuildConfig
import com.rainy.customview.R
import com.rainy.jsonholder.FaceVideoHotBean
import com.rainy.jsonholder.FaceVideoResourceBean
import com.rainy.jsonholder.SerializableHolder
import com.rainy.jsonholder.TypeTokenHolder
import com.rainy.jsonholder.gson
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


        val jsonString =
            "[{\"weight\":9999,\"is_show\":\"1\",\"id\":\"10\"},{\"weight\":4466,\"is_show\":\"1\",\"id\":\"42\"},{\"weight\":0,\"is_show\":\"1\",\"id\":\"\"}]"


        val jsonStringTwo = "[{\"video\":\"http://cdn.resource.unbing.cn/files/upload/34a56f8a8f6a4058a691d175d42381f8.mp4\",\"tab_title\":\"\",\"is_hot\":\"1\",\"name\":\"红色仙侠PPT\",\"is_vip\":\"1\",\"first_frame\":\"http://cdn.resource.unbing.cn/files/upload/5ba7ef21ac754093ae7c2b09be6a5f9e.png\",\"weight\":9999,\"is_show\":\"1\",\"cover\":\"http://cdn.resource.unbing.cn/files/upload/987f82a5fc9d46cfb508ab78ad8513cc.webp\",\"id\":12,\"ratio\":\"200*200\"}]"
        SerializableHolder.toBeanOrNull<List<FaceVideoResourceBean>>(jsonStringTwo)
            ?.filter { it.weight != 0 }?.forEach {
                Log.d("Json", "bean->$it")
            }


        gson.fromJson<List<FaceVideoResourceBean>>(jsonStringTwo,
            object : TypeToken<List<FaceVideoResourceBean>>() {}.type).forEach {
            Log.d("Json", "bean->$it ")
        }
      }
}