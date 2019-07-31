package besmart.elderlycare.screen.evaluation

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
import besmart.elderlycare.databinding.ActivityEvaluationBinding
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.evaluationadd.AddEvaluationActivity
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.evaluationhistory.EvaluationHistoryActivity
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

class EvaluationActivity : BaseActivity(), OnChartValueSelectedListener,
    DatePickerDialog.OnDateSetListener {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var binding: ActivityEvaluationBinding
    private lateinit var profile: ProfileResponce
    private val viewModel: EvalustionViewModel by viewModel()
    private val ADD_EVALUATION = 202

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation)
        binding.viewModel = viewModel
        profile = intent.getParcelableExtra(PROFILE)
        initInstance()
        observerViewModel()
        initLineChart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val date = data?.getStringExtra("date")
            val resultCalendar = Calendar.getInstance()
            val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            resultCalendar.time = inputFormat.parse(date)
            val year = resultCalendar.get(Calendar.YEAR)
            val month = resultCalendar.get(Calendar.MONTH) + 1
            viewModel.getBloodPressureHistory(profile.cardID!!, year.toString(), month.toString())
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
        binding.btnAddEvaluation.setOnClickListener {
            Intent().apply {
                this.setClass(this@EvaluationActivity, AddEvaluationActivity::class.java)
                this.putExtra(AddEvaluationActivity.PROFILE, profile)
                startActivityForResult(this, ADD_EVALUATION)
            }
        }

        binding.btnEvaluetionHistory.setOnClickListener {
            Intent().apply {
                this.setClass(this@EvaluationActivity, EvaluationHistoryActivity::class.java)
                this.putExtra(EvaluationHistoryActivity.PROFILE, profile)
                startActivity(this)
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
            binding.textResult.setTextColor(it.getResultColor())
        })

        viewModel.getBloodPressureLastIndex(profile.cardID!!)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        viewModel.getBloodPressureHistory(profile.cardID!!, year.toString(), month.toString())
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
        if (list.isNotEmpty()) {
            xAxis.valueFormatter = IndexAxisValueFormatter(getAreaCount(list))
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

    private fun getAreaCount(list: List<BloodPressuresResponse>): MutableList<String> {
        val label = list.map { setDateTimeText(it.createAt) }.toMutableList()
        return label
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
        binding.editDate.setText(output)
        viewModel.getBloodPressureHistory(
            profile.cardID!!,
            (year - 543).toString(),
            month.toString()
        )
    }
}
