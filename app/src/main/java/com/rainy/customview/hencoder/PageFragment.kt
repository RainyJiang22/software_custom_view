package com.rainy.customview.hencoder


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.rainy.customview.R
import com.rainy.customview.databinding.FragmentPageBinding


/**
 * @author jiangshiyu
 * @date 2022/10/27
 */
class PageFragment : Fragment() {

    @LayoutRes
    private var sampleLayoutRes: Int = -1

    @LayoutRes
    private var practiceLayoutRes: Int = -1

    companion object {

        fun newInstance(
            @LayoutRes sampleLayoutRes: Int,
            @LayoutRes practiceLayoutRes: Int
        ): PageFragment {
            val bundle = Bundle().apply {
                putInt("sampleLayoutRes", sampleLayoutRes)
                putInt("practiceLayoutRes", practiceLayoutRes)
            }
            val fragment = PageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentPageBinding = FragmentPageBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            sampleStub.layoutResource = sampleLayoutRes
            sampleStub.inflate()

            practiceStub.layoutResource = practiceLayoutRes
            practiceStub.inflate()
        }

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            sampleLayoutRes = this.getInt("sampleLayoutRes")
            practiceLayoutRes = this.getInt("practiceLayoutRes")
        }
    }
}