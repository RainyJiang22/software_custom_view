package com.rainy.customview.hencoder

import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.rainy.customview.R
import com.rainy.customview.base.BaseActivity
import com.rainy.customview.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding, AndroidViewModel>() {
    override fun onBundle(bundle: Bundle) {
    }

    private val pageModels = mutableListOf<PageModel>()


    override fun init(savedInstanceState: Bundle?) {
        pageModels.add(
            PageModel(
                R.layout.sample_color,
                R.string.title_draw_color,
                R.layout.practice_color
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_circle,
                R.string.title_draw_circle,
                R.layout.practice_circle
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_rect,
                R.string.title_draw_rect,
                R.layout.practice_rect
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_point,
                R.string.title_draw_point,
                R.layout.practice_point
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_oval,
                R.string.title_draw_oval,
                R.layout.practice_oval
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_line,
                R.string.title_draw_line,
                R.layout.practice_line
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_round_rect,
                R.string.title_draw_round_rect,
                R.layout.practice_round_rect
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_arc,
                R.string.title_draw_arc,
                R.layout.practice_arc
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_path,
                R.string.title_draw_path,
                R.layout.practice_path
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_histogram,
                R.string.title_draw_histogram,
                R.layout.practice_histogram
            )
        )
        pageModels.add(
            PageModel(
                R.layout.sample_pie_chart,
                R.string.title_draw_pie_chart,
                R.layout.practice_pie_chart
            )
        )

        binding?.tabLayout?.apply {
            for (mode in pageModels) {
                addTab(this.newTab().setText(mode.titleRes))
            }
        }
        binding?.tabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding?.viewpager?.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        binding?.viewpager?.apply {
            this.offscreenPageLimit = pageModels.size
            this.adapter = PagerStateAdapter(this@MainActivity, pageModels)
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(position))
                }
            })
        }
    }

    override fun createObserver() {
    }

}