package com.steven.scannerapp.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.steven.scannerapp.R
import com.steven.scannerapp.ui.history.HistoryFragment
import com.steven.scannerapp.ui.scanner.ScannerFragment
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SectionsStateAdapter @Inject constructor(
    @ApplicationContext val context: Context,
    fragmentActivity: FragmentActivity
) :
    FragmentStateAdapter(fragmentActivity) {

    companion object {
        private val TAB_TITLES = arrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 1) HistoryFragment.newInstance(position + 1) else ScannerFragment.newInstance(
            position + 1
        )
    }
}