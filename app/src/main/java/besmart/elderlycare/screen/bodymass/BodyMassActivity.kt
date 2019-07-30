package besmart.elderlycare.screen.bodymass

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
import besmart.elderlycare.databinding.ActivityBodyMassBinding
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.addbodymass.AddBodyMassActivity
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.bodymasshistory.BodyMassHistoryActivity
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


class BodyMassActivity : BaseActivity(), OnChartValueSelectedListener,
    DatePickerDialog.OnDateSetListener {

    companion object {
        const val PROFILE = "profile"
    }

    private val viewModel: BodyMassViewModel by viewModel()
    private lateinit var binding: ActivityBodyMassBinding
    private lateinit var profile: ProfileResponce
    private lateinit var lineDataSet: LineDataSet
    private val calendar = Calendar.getInstance(Locale("TH"))
    private val ADD_BODY_MASS_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_body_mass)
        profile = intent.getParcelableExtra(PROFILE)
        binding.viewModel = viewModel
        initInstance()
        observerViewModel()
        initLineChart()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val date = data?.getStringExtra("date")
            val resultCalendar = Calendar.getInstance()
            val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            resultCalendar.time = inputFormat.parse(date)
            val year = resultCalendar.get(Calendar.YEAR)
            val month = resultCalendar.get(Calendar.MONTH) + 1
            viewModel.getBodymassHistory(profile.cardID, year.toString(), month.toString())
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
        binding.btnAddBodyMass.setOnClickListener {
            Intent().apply {
                this.setClass(this@BodyMassActivity, AddBodyMassActivity::class.java)
                this.putExtra(AddBodyMassActivity.PROFILE, profile)
                startActivityForResult(this, ADD_BODY_MASS_CODE)
            }
        }
        binding.btnHistoryBodyMass.setOnClickListener {
            Intent().apply {
                this.setClass(this@BodyMassActivity, BodyMassHistoryActivity::class.java)
                this.putExtra(BodyMassHistoryActivity.PROFILE, profile)
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
            binding.textResult.setTextColor(getResultColorByBMI(it.bMI!!))
        })

        viewModel.getBodymassLastIndex(profile.cardID)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        viewModel.getBodymassHistory(profile.cardID, year.toString(), month.toString())
    }

    private fun setData(list: List<BodyMassResponce>) {
        val entryList = mutableListOf<Entry>()
        list.forEachIndexed { index, bodyMassResponce ->
            entryList.add(mapListToEntry(index, bodyMassResponce))
        }
        if (binding.chart.data != null && binding.chart.data.dataSetCount > 0) {
            lineDataSet = binding.chart.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = entryList
        } else {
            lineDataSet = LineDataSet(entryList, "BMI")

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
        binding.chart.notifyDataSetChanged()

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
        leftAxis.axisMaximum = 60f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true

        val rightAxis = binding.chart.axisRight
        rightAxis.setDrawLabels(false)
        rightAxis.isEnabled = false
    }

    private fun getResultColorByBMI(bMI: Double): Int {
        return when {
            bMI >= 40 -> Color.parseColor("#DE0101")
            bMI >= 30 -> Color.parseColor("#FF4E00")
            bMI >= 25 -> Color.parseColor("#FF9900")
            bMI >= 18.5 -> Color.parseColor("#00C857")
            else -> Color.parseColor("#00B1C8")
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun setDateTimeText(createAt: String?):String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val monthFormat = SimpleDateFormat(" dd MMM", Locale("TH"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("TH"))
        val input = inputFormat.parse(createAt)
        return monthFormat.format(input) + " " + timeFormat.format(input)
    }

    private fun mapListToEntry(
        index: Int, bodyMassResponce: BodyMassResponce
    ): Entry {
        return Entry(index.toFloat(), bodyMassResponce.bMI?.toFloat()!!)
    }

    private fun getAreaCount(list: List<BodyMassResponce>): MutableList<String> {
        var label = mutableListOf<String>()
        label = list.map { setDateTimeText(it.createAt) }.toMutableList()
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
        viewModel.getBodymassHistory(profile.cardID, (year - 543).toString(), month.toString())
    }
}
