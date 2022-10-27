package com.rainy.customview.hencoder

import androidx.annotation.LayoutRes


data class PageModel(
    @LayoutRes val sampleLayoutRes: Int,
    val titleRes: Int,
    @LayoutRes val practiceLayoutRes: Int
)
