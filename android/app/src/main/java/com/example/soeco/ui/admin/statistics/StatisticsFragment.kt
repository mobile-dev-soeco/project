package com.example.soeco.ui.admin.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.soeco.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


class StatisticsFragment : Fragment() {

    private lateinit var lineChart: LineChart


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)


        lineChart = view.findViewById(R.id.reportingChart);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);

        // type Entry to hold values
        val estimate = ArrayList<Entry>()
        val actual = ArrayList<Entry>()

        // fill list with Entry objects
        estimate.add(Entry(0f, 100f))
        estimate.add(Entry(1f, 200f))
        estimate.add(Entry(2f, 150f))
        estimate.add(Entry(3f, 170f))
        estimate.add(Entry(4f, 180f))

        actual.add(Entry(0f, 120f))
        actual.add(Entry(1f, 210f))
        actual.add(Entry(2f, 170f))
        actual.add(Entry(3f, 170f))
        actual.add(Entry(4f, 200f))

        // create LineDataSet objects
        val setEstimate = LineDataSet(estimate, "Estimate hours")
        setEstimate.axisDependency = YAxis.AxisDependency.LEFT
        setEstimate.color = R.color.Green

        val setActual = LineDataSet(actual, "Actual hours")
        setActual.axisDependency = YAxis.AxisDependency.LEFT

        // IDataSets to build CharData object
        // interface ILineDataSet
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(setEstimate)
        dataSets.add(setActual)

        val data = LineData(dataSets)

        lineChart.data = data
        lineChart.invalidate()

        // set Xais labels
        val xLabel = ArrayList<String>()
        xLabel.add("order 1")
        xLabel.add("order 2")
        xLabel.add("order 3")
        xLabel.add("order 4")
        xLabel.add("order 5")

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setDrawGridLines(false);
        xAxis.granularity = 1f;
//        xAxis.isGranularityEnabled = true;
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        xAxis.labelRotationAngle = 300f

        return view
    }


}