package com.example.soeco.ui.carpentry.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.mongo.TimeReport
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class TimeReportAdapter(
    private val onDeleteClickListener: (view: View, timeReport: TimeReport) -> Unit
): ListAdapter<TimeReport, TimeReportAdapter.TimeReportViewHolder>(TimeReportAdapter.DataDiffCallback()) {

    inner class TimeReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateView: TextView
        val hoursView: TextView
        val deleteBtn: ImageButton

        init {
            dateView = itemView.findViewById(R.id.tvDate)
            hoursView = itemView.findViewById(R.id.tvHours)
            deleteBtn = itemView.findViewById(R.id.btnDelete)
        }

        fun bind(timeReport: TimeReport){
            dateView.text = formatDate(timeReport.date)
            hoursView.text = formatTime(timeReport.hours)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeReportAdapter.TimeReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_report_list_item, parent, false)

        return TimeReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeReportViewHolder, position: Int) {
        holder.apply {
            bind(getItem(position))
            deleteBtn.setOnClickListener { view ->
                onDeleteClickListener.invoke(view, getItem(position))
            }
        }
    }

    private fun formatTime(hours: Float): String {
        val hour = hours.toInt()
        val minutes = ((hours - hour) * 60).toInt()
        return "$hour tim $minutes min"
    }

    private fun formatDate(date: Date): String {
        val convertedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("yyyy MM dd")
        val text = convertedDate.format(formatter)
        return LocalDate.parse(text, formatter).toString()
    }

    class DataDiffCallback: DiffUtil.ItemCallback<TimeReport>(){
        override fun areItemsTheSame(oldItem: TimeReport, newItem: TimeReport): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: TimeReport, newItem: TimeReport): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: TimeReport, newItem: TimeReport): Any? {
            return super.getChangePayload(oldItem, newItem)
        }
    }

}