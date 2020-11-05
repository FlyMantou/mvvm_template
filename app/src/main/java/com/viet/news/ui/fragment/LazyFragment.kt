package com.viet.news.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment


/**
 * @author null.
 */
abstract class LazyFragment : Fragment() {
    val targetView: View?
        get() = view

    abstract fun initNet()

}
