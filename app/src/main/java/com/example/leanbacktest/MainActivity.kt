package com.example.leanbacktest

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.leanbacktest.test.TestFragment1

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment : Fragment = TestFragment1()

            supportFragmentManager.commitTransaction { it.add(R.id.single_pane_content, fragment) }
        }
    }
}

inline fun FragmentManager.commitTransaction(
    addToBackStack: Boolean = false,
    name: String? = null,
    allowStateLoss: Boolean = false,
    transaction: (FragmentTransaction) -> Unit
) {
    with(beginTransaction()) {
        transaction.invoke(this)

        if (addToBackStack) addToBackStack(name)

        if (allowStateLoss) {
            commitAllowingStateLoss()
        } else {
            commit()
        }
    }
}