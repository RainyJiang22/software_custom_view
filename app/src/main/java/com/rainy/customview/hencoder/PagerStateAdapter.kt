package com.rainy.customview.hencoder

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author jiangshiyu
 * @date 2022/10/27
 */
class PagerStateAdapter(
    fragmentActivity: FragmentActivity,
    val pageModels: List<PageModel>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return pageModels.size
    }

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(
            pageModels[position].sampleLayoutRes,
            pageModels[position].practiceLayoutRes
        )
    }


}