package com.example.leanbacktest.test

import androidx.leanback.widget.GuidedAction
import com.example.leanbacktest.R
import com.example.leanbacktest.my.BaseGuidedStepFragment

class TestFragment1 : BaseGuidedStepFragment() {

    companion object {
        private const val TO_2_ACTION = 1L
        private const val DUMMY_ACTION = 2L
    }

    override val guidedActions: List<Pair<Long, Int>> = mutableListOf(
        TO_2_ACTION to R.string.action1tofragment2,
        DUMMY_ACTION to R.string.dismiss_error

    )

    override fun onSafeGuidedActionClicked(action: GuidedAction): Boolean {
        return when (action.id) {
            TO_2_ACTION -> {
                add(parentFragmentManager, TestFragment2())
                true
            }
            DUMMY_ACTION -> {
                println("baf")
                true
            }
            else -> false
        }
    }
}