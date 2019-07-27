package besmart.elderlycare.screen.evaluation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityEvaluationBinding
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EvaluationActivity : BaseActivity(), OnChartValueSelectedListener {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var binding: ActivityEvaluationBinding
    private lateinit var profile: ProfileResponce
    private val viewModel: EvalustionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation)
        binding.viewModel = viewModel
        profile = intent.getParcelableExtra(PROFILE)
        initInstance()
        observerViewModel()
        initLineChart()
    }

    private fun initInstance() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })

        viewModel.chartLiveData.observe(this, Observer {
            setData(it)
        })

        viewModel.historyLiveData.observe(this, Observer {
        })

        viewModel.getBloodPressureLastIndex(profile.cardID!!)
        viewModel.getBloodPressureHistory(profile.cardID!!)
    }

    private fun initLineChart() {
        binding.chart.setOnChartValueSelectedListener(this)
        // enable touch gestures
        binding.chart.setTouchEnabled(true)

        binding.chart.dragDecelerationFrictionCoef = 0.9f

        // enable scaling and dragging
        binding.chart.isDragEnabled = true
        binding.chart.setScaleEnabled(true)
        binding.chart.setDrawGridBackground(false)
        binding.chart.isHighlightPerDragEnabled = true

        // if disabled, scaling can be done on x- and y-axis separately
        binding.chart.setPinchZoom(true)

        // set an alternative background color
        binding.chart.setBackgroundColor(Color.WHITE)

        binding.chart.animateX(1500)


        // get the legend (only possible after setting data)
        val l = binding.chart.legend

        // modify the legend ...
        l.form = Legend.LegendForm.LINE
        l.textSize = 11f
        l.textColor = Color.BLACK
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val leftAxis = binding.chart.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.axisMaximum = 240f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis = binding.chart.axisRight
        rightAxis.setDrawLabels(false)
        rightAxis.isEnabled = false
    }

    private fun setData(list: List<BloodPressuresResponse>) {

        val syslist = mutableListOf<Entry>()
        val dialist = mutableListOf<Entry>()

        list.forEachIndexed { index, bloodPressuresResponse ->
            syslist.add(Entry(index.toFloat(), bloodPressuresResponse.sys!!.toFloat()))
            dialist.add(Entry(index.toFloat(), bloodPressuresResponse.dia!!.toFloat()))
        }

        val lineDataSetSYS: LineDataSet
        val lineDataSetDIA: LineDataSet

        if (binding.chart.data != null && binding.chart.data.dataSetCount > 0) {
            lineDataSetSYS = binding.chart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSetDIA = binding.chart.data.getDataSetByIndex(1) as LineDataSet
            lineDataSetSYS.values = syslist
            lineDataSetDIA.values = dialist
        } else {
            // Map line sys data set
            lineDataSetSYS = LineDataSet(syslist, "SYS")

            lineDataSetSYS.axisDependency = YAxis.AxisDependency.LEFT
            lineDataSetSYS.color = ColorTemplate.getHoloBlue()
            lineDataSetSYS.setCircleColor(Color.BLACK)
            lineDataSetSYS.lineWidth = 2f
            lineDataSetSYS.circleRadius = 3f
            lineDataSetSYS.fillAlpha = 65
            lineDataSetSYS.fillColor = ColorTemplate.getHoloBlue()
            lineDataSetSYS.highLightColor = Color.rgb(244, 117, 117)
            lineDataSetSYS.setDrawCircleHole(false)

            // Map line dia data set
            lineDataSetDIA = LineDataSet(dialist, "DIA")

            lineDataSetDIA.axisDependency = YAxis.AxisDependency.LEFT
            lineDataSetDIA.color = Color.GREEN
            lineDataSetDIA.setCircleColor(Color.BLACK)
            lineDataSetDIA.lineWidth = 2f
            lineDataSetDIA.circleRadius = 3f
            lineDataSetDIA.fillAlpha = 65
            lineDataSetDIA.fillColor = Color.GREEN
            lineDataSetDIA.highLightColor = Color.rgb(244, 117, 117)
            lineDataSetDIA.setDrawCircleHole(false)

            // create a data object with the data sets
            val data = LineData(lineDataSetSYS, lineDataSetDIA)
            data.setValueTextColor(Color.BLACK)
            data.setValueTextSize(9f)

            // set data
            binding.chart.data = data
        }

        val xAxis = binding.chart.xAxis
        xAxis.textSize = 11f
        xAxis.textColor = Color.RED
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return setDateTimeText(viewModel.history[value.toInt()].date)
            }
        }
    }


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setDateTimeText(createAt: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val monthFormat = SimpleDateFormat(" dd MMM", Locale("TH"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("TH"))
        val input = inputFormat.parse(createAt)
        return monthFormat.format(input) + "\n" + timeFormat.format(input)
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        binding.chart.centerViewToAnimated(
            e!!.x, e.y, binding.chart.data.getDataSetByIndex(h!!.dataSetIndex)
                .axisDependency, 500
        )
    }
}
