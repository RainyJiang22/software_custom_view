package com.rainy.customview.fontview

import androidx.core.content.res.ResourcesCompat
import com.rainy.customview.AppApplication
import com.rainy.customview.R

/**
 * @author jiangshiyu
 * @date 2023/5/31
 */
object FontManager {

    val FONT_HANYI_YAKU by lazy {
        ResourcesCompat.getFont(AppApplication.getApplication()!!, R.font.hanyiyaku85w)
    }
}