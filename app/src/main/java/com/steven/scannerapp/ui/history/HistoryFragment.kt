package com.steven.scannerapp.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.steven.scannerapp.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAdapter = HistoryAdapter(mutableListOf())
        val rv = view.findViewById<RecyclerView>(R.id.rv_history_items)
        rv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }
        historyViewModel.allHistoryItems.observe(viewLifecycleOwner, { itemList ->
            itemList?.let {
                Log.d(
                    "HistoryFragment",
                    "Observing all history items. List size = ${itemList.size}"
                )
                Log.d(
                    "HistoryFragment",
                    "All items $itemList"
                )
                rvAdapter.addItems(itemList)
            }
        })
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): HistoryFragment {
            return HistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}