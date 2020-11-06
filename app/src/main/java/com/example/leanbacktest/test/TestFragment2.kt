package com.example.leanbacktest.test

import androidx.leanback.widget.GuidedAction
import com.example.leanbacktest.R
import com.example.leanbacktest.my.BaseGuidedStepFragment

class TestFragment2 : BaseGuidedStepFragment() {

    companion object {
        private const val TO_1_ACTION = 1L
    }

    override val guidedActions: List<Pair<Long, Int>> = mutableListOf(
        TO_1_ACTION to R.string.back
    )

    override fun onSafeGuidedActionClicked(action: GuidedAction): Boolean {
        return when (action.id) {
            TO_1_ACTION -> {
                add(parentFragmentManager, TestFragment1())
                true
            }
            else -> false
        }
    }
}