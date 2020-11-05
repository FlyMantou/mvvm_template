package com.viet.news.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import javax.inject.Inject

/**
 * Created by aaron on 02/11/2017.
 */
class TabFragmentAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    companion object {
        private const val TAG = "TabFragmentAdapter"
    }

    private var titleList: MutableList<String> = mutableListOf()
    private var fragmentList: MutableList<BaseFragment> = ArrayList()

    fun addFragment(fragment: BaseFragment?) {
        if (fragment != null)
            fragmentList.add(fragment)
    }

    fun removeFragment(fragment: BaseFragment) {
        fragmentList.remove(fragment)
    }

    fun setFragment(fragments: MutableList<BaseFragment>) {
        if (fragments.size > 0) {
            fragmentList = fragments
        }
    }

    fun clear() {
        for (fragment in fragmentList) {
            if (fragment.isAdded) {
                fragment.onDestroy()
            }
            fragmentList.clear()
        }
    }


    fun addTitle(ctx: Context, @StringRes title: Int) {
        titleList.add(ctx.getString(title))
    }

    fun addTitle(title: String) {
        titleList.add(title)
    }

    fun removeTitle(fragment: String) {
        titleList.remove(fragment)
    }

    fun setTitles(titles: MutableList<String>) {
        titleList = titles
    }

    fun getTitles(): MutableList<String> {
        return titleList
    }


    override fun getItem(position: Int): Fragment {
        return if (fragmentList.size > 0) {
            fragmentList[position]
        } else {
            Fragment()
        }
    }

    override fun getCount(): Int {
        return if (fragmentList.size > 0) {
            fragmentList.size
        } else {
            0
        }
    }

    override fun getPageTitle(position: Int): CharSequence? = if (titleList.size > 0) titleList[position] else ""
}