package besmart.elderlycare.screen.sugar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivitySugarBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.sugaradd.SugarAddActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.witget.MonthYearPickerDialog
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class SugarActivity : BaseActivity(), OnChartValueSelectedListener,
    DatePickerDialog.OnDateSetListener {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var binding: ActivitySugarBinding
    private val viewModel: SugarViewModel by viewModel()
    private lateinit var profile: ProfileResponce
    private lateinit var lineDataSet: LineDataSet
    private val ADD_SUGAR = 303
    private var currentMonth: String = ""
    private var currentYear: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sugar)
        binding.viewModel = viewModel
        profile = intent.getParcelableExtra(PROFILE)
        initInstance()
        observerViewModel()
        initLineChart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            viewModel.getSugarHistory(profile.cardID, currentYear, currentMonth)
        }
    }

    private fun initInstance() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val thCalendar = Calendar.getInstance()
        thCalendar.set(Calendar.YEAR, thCalendar.get(Calendar.YEAR) + 543)
        val fm = SimpleDateFormat("MMM yyyy", Locale("TH"))
        val output = fm.format(thCalendar.time)
        binding.editDate.setText(output)
        binding.editDate.setOnClickListener {
            val dp = MonthYearPickerDialog()
            dp.setListener(this)
            dp.show(supportFragmentManager, "MonthDialog")
        }
        binding.btnAddSugar.setOnClickListener {
            Intent().apply {
                this.setClass(this@SugarActivity, SugarAddActivity::class.java)
                this.putExtra(SugarAddActivity.PROFILE, profile)
                startActivityForResult(this, ADD_SUGAR)
            }
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
            binding.chart.clear()
            if (it.isNotEmpty()) {
                setData(it)
            }
        })

        viewModel.historyLiveData.observe(this, Observer {
            binding.textFbsResult.setTextColor(it.getColor())
            binding.textResult.text = it.getResult()
            binding.textResult.setTextColor(it.getColor())
        })

        viewModel.getSugarLastIndex(profile.cardID)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        currentMonth = month.toString()
        currentYear = year.toString()
        viewModel.getSugarHistory(profile.cardID, year.toString(), month.toString())
    }

    private fun setData(list: List<SugarResponse>) {
        val entryList = mutableListOf<Entry>()
        list.forEachIndexed { index, bodyMassResponce ->
            entryList.add(mapListToEntry(index, bodyMassResponce))
        }
        if (binding.chart.data != null && binding.chart.data.dataSetCount > 0) {
            lineDataSet = binding.chart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = entryList
        } else {
            lineDataSet = LineDataSet(entryList, "ระดับน้ำตาล")

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
            binding.chart.data = data
        }
        binding.chart.invalidate()

        val xAxis = binding.chart.xAxis
        xAxis.textSize = 11f
        xAxis.textColor = Color.RED
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)
        xAxis.granularity=1f
        if (list.isNotEmpty()) {
            xAxis.valueFormatter = IndexAxisValueFormatter(getAreaCount(list))
        }
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
        leftAxis.axisMaximum = 300f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis = binding.chart.axisRight
        rightAxis.setDrawLabels(false)
        rightAxis.isEnabled = false
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setDateTimeText(createAt: String?):String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        calendar.time = inputFormat.parse(createAt)
        val monthFormat = SimpleDateFormat(" dd MMM", Locale("TH"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("TH"))
        return monthFormat.format(calendar.time) + "\n" + timeFormat.format(calendar.time)
    }

    private fun mapListToEntry(
        index: Int, sugar: SugarResponse
    ): Entry {
        return Entry(index.toFloat(), sugar.fbs!!.toFloat())
    }

    private fun getAreaCount(list: List<SugarResponse>): MutableList<String> {
        return list.map { setDateTimeText(it.date) }.toMutableList()
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        binding.chart.centerViewToAnimated(
            e!!.x, e.y, binding.chart.data.getDataSetByIndex(h!!.dataSetIndex)
                .axisDependency, 500
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val thCalendar = Calendar.getInstance()
        thCalendar.set(Calendar.YEAR, year)
        thCalendar.set(Calendar.MONTH, month - 1)
        val fm = SimpleDateFormat("MMM yyyy", Locale("TH"))
        val output = fm.format(thCalendar.time)
        currentYear = year.toString()
        currentMonth = month.toString()
        binding.editDate.setText(output)
        viewModel.getSugarHistory(profile.cardID, (year - 543).toString(), month.toString())
    }
}
