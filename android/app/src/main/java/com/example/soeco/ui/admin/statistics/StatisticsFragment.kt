package com.example.soeco.ui.admin.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.soeco.R
import com.example.soeco.data.Models.DB_Models.Delivery_Report_DB
import com.example.soeco.data.Models.DB_Models.Deviation_Report_DB
import com.example.soeco.data.Models.DB_Models.Tradesmen_Report_DB
import com.example.soeco.ui.viewmodels.StatsViewModel
import com.example.soeco.utils.viewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.realm.RealmResults


class StatisticsFragment : Fragment() {

    private lateinit var lineChart: LineChart
    private val statsViewModel by viewModels<StatsViewModel> { viewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = view.findViewById(R.id.reportingChart);
        setSelector(view)
        tradesmenReportsChart()

    }

    private fun setSelector(view: View){
        val selector : Spinner = view.findViewById(R.id.selector)
        val list = listOf("Snickare och Tapetserare","Leverans", "Avikelser")
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, list)
        selector.adapter = dataAdapter
        selector.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (selector.selectedItem.toString()){
                    "Snickare och Tapetserare" ->  tradesmenReportsChart()
                    "Leverans" -> deliveryReportChart()
                    "Avikelser" -> deviationReportChart()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
    private fun getExpectedTimesTradesmen(tradesmenReports : RealmResults<Tradesmen_Report_DB>, listOfOrders:ArrayList<String>): ArrayList<Float> {
        val expectedTimes    = ArrayList<Float>()
        for (order in listOfOrders) {
            tradesmenReports.find { it.orderNumber == order}?.orderExpectedTime?.let { expectedTimes.add(it.toFloat())
            }
        }
        return expectedTimes
    }

    private fun getExpectedTimesDelivery(deliveryReports: RealmResults<Delivery_Report_DB>, listOfOrders: ArrayList<String>): ArrayList<Float> {
        val expectedTimes    = ArrayList<Float>()
        for (order in listOfOrders) {
            deliveryReports.find { it.orderNumber == order}?.orderExpectedTime?.let { expectedTimes.add(it.toFloat())
            }
        }
        return expectedTimes
    }



    private fun getActualTimesTradesmen(tradesmenReports : RealmResults<Tradesmen_Report_DB>, listOfOrders :ArrayList<String> ): ArrayList<Float> {
        val actualTimes= ArrayList<Float>()
        for (order in listOfOrders) {
            val timesThisOrder = ArrayList<Float>()
            for (report in tradesmenReports) {
                if (report.orderNumber == order){
                    timesThisOrder.add(convertMin(report.time))
                }
            }
            actualTimes.add(convertHours(timesThisOrder.sum()))
        }
        return actualTimes
    }

    private fun getActualTimesDelivery(deliveryReports: RealmResults<Delivery_Report_DB>, listOfOrders :ArrayList<String> ): ArrayList<Float> {
        val actualTimes= ArrayList<Float>()
        for (order in listOfOrders) {
            val timesThisOrder = ArrayList<Float>()
            for (report in deliveryReports) {
                if (report.orderNumber == order){
                    timesThisOrder.add(convertMin(report.time))
                }
            }
            actualTimes.add(convertHours(timesThisOrder.sum()))
        }
        return actualTimes
    }

    private fun getTimesDeviation(deviationReports: RealmResults<Deviation_Report_DB>, listOfOrders: ArrayList<String>): ArrayList<Float> {
        val actualTimes= ArrayList<Float>()
        for (order in listOfOrders) {
            val timesThisOrder = ArrayList<Float>()
            for (report in deviationReports) {
                if (report.orderNumber == order){
                    timesThisOrder.add(convertMin(report.time))
                }
            }
            actualTimes.add(convertHours(timesThisOrder.sum()))
        }
        return actualTimes
    }
    private fun getOrderNumbersTradesmen(tradesmenReports: RealmResults<Tradesmen_Report_DB>): ArrayList<String> {
        var listOfOrders = ArrayList<String>()
        for (report in tradesmenReports){
            listOfOrders.add(report.orderNumber)
        }
        if (listOfOrders.size > 0 ){
            listOfOrders = listOfOrders.distinct() as ArrayList<String>
            listOfOrders.sort()
        }
        return listOfOrders
    }
    private fun getOrderNumbersDelivery(deliveryReports: RealmResults<Delivery_Report_DB>): ArrayList<String> {
        var listOfOrders = ArrayList<String>()
        for (report in deliveryReports){
            listOfOrders.add(report.orderNumber)
        }
        if (listOfOrders.size > 0 ) {
            listOfOrders = listOfOrders.distinct() as ArrayList<String>
            listOfOrders.sort()
        }
        return listOfOrders
    }
    private fun getOrderNumbersDeviation(deviationReports: RealmResults<Deviation_Report_DB>): ArrayList<String> {
        var listOfOrders = ArrayList<String>()
        for (report in deviationReports){
            listOfOrders.add(report.orderNumber)
        }
        if (listOfOrders.size > 0 ) {
            listOfOrders = listOfOrders.distinct() as ArrayList<String>
            listOfOrders.sort()
        }
        return listOfOrders
    }
    private fun convertHours( time: Float): Float{
        return   Math.round(time / 60).toFloat()

    }
    private fun convertMin(time: String): Float {
        val hours = time.substringBefore(":").substringBefore(" tim")
        val min = time.substringAfter(":").substringBefore(" min").trim()
        return (hours.toFloat() * 60) + min.toFloat()
    }


    private fun deliveryReportChart() {
        val deliveryReports = statsViewModel.deliveryReports
        val listOfOrders = getOrderNumbersDelivery(deliveryReports)
        val actualTimes = getActualTimesDelivery(deliveryReports,listOfOrders)
        val expectedTimes = getExpectedTimesDelivery(deliveryReports,listOfOrders)
        setEntriesLabels(actualTimes,expectedTimes,listOfOrders)

    }


    private fun tradesmenReportsChart(){
        val tradesmenReports = statsViewModel.tradesmenReports
        val listOfOrders = getOrderNumbersTradesmen(tradesmenReports)
        val actualTimes= getActualTimesTradesmen(tradesmenReports,listOfOrders)
        val expectedTimes = getExpectedTimesTradesmen(tradesmenReports,listOfOrders)
        setEntriesLabels(actualTimes,expectedTimes,listOfOrders)

    }

    private fun deviationReportChart(){
        val deviationReports = statsViewModel.deviationReports
        val listOfOrders = getOrderNumbersDeviation(deviationReports)
        val times= getTimesDeviation(deviationReports,listOfOrders)
        setEntriesLabels(times,listOfOrders)
    }
    private fun setEntriesLabels(
        actualTimes: ArrayList<Float>,
        listOfOrders: ArrayList<String>
    ) {
        val line2 = ArrayList<Entry>()
        val xLabel = ArrayList<String>()

        for (i in 0 until listOfOrders.size) {
            line2.add(Entry(i.toFloat(), actualTimes[i]))
            xLabel.add(listOfOrders[i])
        }
        oneLineChart(line2,xLabel)
    }

    private fun setEntriesLabels(
        actualTimes: ArrayList<Float>,
        expectedTimes: ArrayList<Float>,
        listOfOrders: ArrayList<String>
    ) {
        val line1 = ArrayList<Entry>()
        val line2 = ArrayList<Entry>()
        val xLabel = ArrayList<String>()

        for (i in 0 until listOfOrders.size) {
            line1.add(Entry(i.toFloat(), expectedTimes[i]))
            line2.add(Entry(i.toFloat(), actualTimes[i]))
            xLabel.add(listOfOrders[i])
        }
        twoLineChart(line1,line2,xLabel)
    }
    private fun oneLineChart(line1 : ArrayList<Entry>, xLabel : ArrayList<String>) {
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        // create LineDataSet objects
        val setEstimate = LineDataSet(line1, "Avikande timmar")
        setEstimate.axisDependency = YAxis.AxisDependency.LEFT
        setEstimate.color = R.color.Soecogreen

        // IDataSets to build CharData object
        // interface ILineDataSet
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(setEstimate)

        val data = LineData(dataSets)
        lineChart.data = data
        lineChart.invalidate()

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setDrawGridLines(false);
        xAxis.granularity = 1f;
//        xAxis.isGranularityEnabled = true;
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        xAxis.labelRotationAngle = 300f
    }
    private fun twoLineChart( line1 : ArrayList<Entry>, line2 :ArrayList<Entry>, xLabel : ArrayList<String>){
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        // create LineDataSet objects
        val setEstimate = LineDataSet(line1, "Uppskattade antal timmar")
        setEstimate.axisDependency = YAxis.AxisDependency.LEFT
        setEstimate.color = R.color.Soecogreen
        val setActual = LineDataSet(line2, "Rappoterade antal timmar")
        setActual.axisDependency = YAxis.AxisDependency.LEFT

        // IDataSets to build CharData object
        // interface ILineDataSet
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(setEstimate)
        dataSets.add(setActual)

        val data = LineData(dataSets)
        lineChart.data = data
        lineChart.invalidate()

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setDrawGridLines(false);
        xAxis.granularity = 1f;
//        xAxis.isGranularityEnabled = true;
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)
        xAxis.labelRotationAngle = 300f
    }

}