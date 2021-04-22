package com.steven.scannerapp.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.steven.scannerapp.R
import com.steven.scannerapp.data.model.Item
import javax.inject.Inject

class HistoryAdapter @Inject constructor(private val historyItems: MutableList<Item>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.history_item_name)
        val dateTextView: TextView = view.findViewById(R.id.history_item_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.nameTextView.text = historyItems[position].name
        holder.dateTextView.text = historyItems[position].date.time.toString()
    }

    override fun getItemCount(): Int {
        return historyItems.size
    }

    fun addItem(item: Item) {
        historyItems.add(item)
        notifyItemInserted(historyItems.size - 1)
    }

    fun addItems(itemList: List<Item>) {
        historyItems.addAll(itemList)
        notifyDataSetChanged()
    }
}