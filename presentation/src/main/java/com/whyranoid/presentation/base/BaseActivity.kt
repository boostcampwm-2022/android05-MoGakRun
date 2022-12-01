package com.whyranoid.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

internal abstract class BaseActivity<VDB : ViewDataBinding>(
    @LayoutRes val layoutRes: Int
) : AppCompatActivity() {

    private var _binding: VDB? = null
    protected val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
