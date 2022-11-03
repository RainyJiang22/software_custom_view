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

    init {
//        pageModels.add(PageModel(R.layout.sample_linear_gradient,
//            R.string.title_linear_gradient,
//            R.layout.practice_linear_gradient))
//        pageModels.add(PageModel(R.layout.sample_radial_gradient,
//            R.string.title_radial_gradient,
//            R.layout.practice_radial_gradient))
//        pageModels.add(PageModel(R.layout.sample_sweep_gradient,
//            R.string.title_sweep_gradient,
//            R.layout.practice_sweep_gradient))
//        pageModels.add(PageModel(R.layout.sample_bitmap_shader,
//            R.string.title_bitmap_shader,
//            R.layout.practice_bitmap_shader))
//        pageModels.add(PageModel(R.layout.sample_compose_shader,
//            R.string.title_compose_shader,
//            R.layout.practice_compose_shader))
//        pageModels.add(PageModel(R.layout.sample_lighting_color_filter,
//            R.string.title_lighting_color_filter,
//            R.layout.practice_lighting_color_filter))
//        pageModels.add(PageModel(R.layout.sample_color_mask_color_filter,
//            R.string.title_color_matrix_color_filter,
//            R.layout.practice_color_matrix_color_filter))
//        pageModels.add(PageModel(R.layout.sample_xfermode,
//            R.string.title_xfermode,
//            R.layout.practice_xfermode))
//        pageModels.add(PageModel(R.layout.sample_stroke_cap,
//            R.string.title_stroke_cap,
//            R.layout.practice_stroke_cap))
//        pageModels.add(PageModel(R.layout.sample_stroke_join,
//            R.string.title_stroke_join,
//            R.layout.practice_stroke_join))
//        pageModels.add(PageModel(R.layout.sample_stroke_miter,
//            R.string.title_stroke_miter,
//            R.layout.practice_stroke_miter))
//        pageModels.add(PageModel(R.layout.sample_path_effect,
//            R.string.title_path_effect,
//            R.layout.practice_path_effect))
//        pageModels.add(PageModel(R.layout.sample_shadow_layer,
//            R.string.title_shader_layer,
//            R.layout.practice_shadow_layer))
//        pageModels.add(PageModel(R.layout.sample_mask_filter,
//            R.string.title_mask_filter,
//            R.layout.practice_mask_filter))
//        pageModels.add(PageModel(R.layout.sample_fill_path,
//            R.string.title_fill_path,
//            R.layout.practice_fill_path))
//        pageModels.add(PageModel(R.layout.sample_text_path,
//            R.string.title_text_path,
//            R.layout.practice_text_path))
        pageModels.add(PageModel(R.layout.sample_draw_text,
            R.string.title_draw_text,
            R.layout.practice_draw_text))
        pageModels.add(PageModel(R.layout.sample_static_layout,
            R.string.title_static_layout,
            R.layout.practice_static_layout))
        pageModels.add(PageModel(R.layout.sample_set_text_size,
            R.string.title_set_text_size,
            R.layout.practice_set_text_size))
        pageModels.add(PageModel(R.layout.sample_set_typeface,
            R.string.title_set_typeface,
            R.layout.practice_set_typeface))
        pageModels.add(PageModel(R.layout.sample_set_fake_bold_text,
            R.string.title_set_fake_bold_text,
            R.layout.practice_set_fake_bold_text))
        pageModels.add(PageModel(R.layout.sample_set_strike_thru_text,
            R.string.title_set_strike_thru_text,
            R.layout.practice_set_strike_thru_text))
        pageModels.add(PageModel(R.layout.sample_set_underline_text,
            R.string.title_set_underline_text,
            R.layout.practice_set_underline_text))
        pageModels.add(PageModel(R.layout.sample_set_text_skew_x,
            R.string.title_set_text_skew_x,
            R.layout.practice_set_text_skew_x))
        pageModels.add(PageModel(R.layout.sample_set_text_scale_x,
            R.string.title_set_text_scale_x,
            R.layout.practice_set_text_scale_x))
        pageModels.add(PageModel(R.layout.sample_set_text_align,
            R.string.title_set_text_align,
            R.layout.practice_set_text_align))
        pageModels.add(PageModel(R.layout.sample_get_font_spacing,
            R.string.title_get_font_spacing,
            R.layout.practice_get_font_spacing))
        pageModels.add(PageModel(R.layout.sample_measure_text,
            R.string.title_measure_text,
            R.layout.practice_measure_text))
        pageModels.add(PageModel(R.layout.sample_get_text_bounds,
            R.string.title_get_text_bounds,
            R.layout.practice_get_text_bounds))
        pageModels.add(PageModel(R.layout.sample_get_font_metrics,
            R.string.title_get_font_metrics,
            R.layout.practice_get_font_metrics))


    }

    override fun init(savedInstanceState: Bundle?) {
//        pageModels.add(
//            PageModel(
//                R.layout.sample_color,
//                R.string.title_draw_color,
//                R.layout.practice_color
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_circle,
//                R.string.title_draw_circle,
//                R.layout.practice_circle
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_rect,
//                R.string.title_draw_rect,
//                R.layout.practice_rect
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_point,
//                R.string.title_draw_point,
//                R.layout.practice_point
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_oval,
//                R.string.title_draw_oval,
//                R.layout.practice_oval
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_line,
//                R.string.title_draw_line,
//                R.layout.practice_line
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_round_rect,
//                R.string.title_draw_round_rect,
//                R.layout.practice_round_rect
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_arc,
//                R.string.title_draw_arc,
//                R.layout.practice_arc
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_path,
//                R.string.title_draw_path,
//                R.layout.practice_path
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_histogram,
//                R.string.title_draw_histogram,
//                R.layout.practice_histogram
//            )
//        )
//        pageModels.add(
//            PageModel(
//                R.layout.sample_pie_chart,
//                R.string.title_draw_pie_chart,
//                R.layout.practice_pie_chart
//            )
//        )
//
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