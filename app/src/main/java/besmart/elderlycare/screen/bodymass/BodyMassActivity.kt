package besmart.elderlycare.screen.bodymass

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityBodyMassBinding
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.CustomXAxisRenderer
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


class BodyMassActivity : BaseActivity(), OnChartValueSelectedListener {

    companion object {
        const val PROFILE = "profile"
    }

    private val viewModel: BodyMassViewModel by viewModel()
    private lateinit var binding: ActivityBodyMassBinding
    private lateinit var profile: ProfileResponce
    private lateinit var lineDataSet: LineDataSet
    private lateinit var listHistory:List<BodyMassResponce>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_body_mass)
        profile = intent.getParcelableExtra(PROFILE)
        binding.viewModel = viewModel
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
            listHistory = it
        })

        viewModel.getBodymassLastIndex(profile.cardID)
        viewModel.getBodymassHistory(profile.cardID)
    }

    private fun setData(list: List<Entry>?) {
        if (binding.chart.data != null && binding.chart.data.dataSetCount > 0) {
            lineDataSet = binding.chart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = list
        } else {
            lineDataSet = LineDataSet(list, "BMI")

            lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
            lineDataSet.color = ColorTemplate.getHoloBlue()
            lineDataSet.setCircleColor(Color.BLACK)
            lineDataSet.lineWidth = 2f
            lineDataSet.circleRadius = 3f
            lineDataSet.fillAlpha = 65
            lineDataSet.fillColor = ColorTemplate.getHoloBlue()
            lineDataSet.highLightColor = Color.rgb(244, 117, 117)
            lineDataSet.setDrawCircleHole(false)

            // create a data object with the data sets
            val data = LineData(lineDataSet)
            data.setValueTextColor(Color.BLACK)
            data.setValueTextSize(9f)

            // set data
            binding.chart.setData(data)
        }

        val xAxis = binding.chart.xAxis
//        xAxis.setTypeface(tfLight)
        xAxis.textSize = 11f
        xAxis.textColor = Color.RED
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.granularity=1f
        xAxis.valueFormatter = object : ValueFormatter(){
            override fun getFormattedValue(value: Float): String {
                return setDateTimeText(viewModel.history[value.toInt()].date)
            }


        }

        binding.chart.setXAxisRenderer(CustomXAxisRenderer(binding.chart.viewPortHandler,xAxis,binding.chart.getTransformer(YAxis.AxisDependency.LEFT)))
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
        val l = binding.chart.getLegend()

        // modify the legend ...
        l.form = Legend.LegendForm.LINE
//        l.setTypeface(R.font.robotoregular)
        l.textSize = 11f
        l.textColor = Color.BLACK
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)

        val leftAxis = binding.chart.axisLeft
//        leftAxis.setTypeface(tfLight)
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.axisMaximum = 60f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis = binding.chart.axisRight
        rightAxis.setDrawLabels(false)
        rightAxis.isEnabled = false
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setDateTimeText(createAt: String?):String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val monthFormat = SimpleDateFormat(" dd MMM", Locale("TH"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("TH"))
        val input = inputFormat.parse(createAt)
        return monthFormat.format(input)+"\n"+timeFormat.format(input)
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        binding.chart.centerViewToAnimated(
            e!!.x, e!!.y, binding.chart.data.getDataSetByIndex(h!!.dataSetIndex)
                .axisDependency, 500
        )
    }
}
