package besmart.elderlycare.screen.addbodymass

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.model.bodymass.BodyMassResponce
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import com.layernet.thaidatetimepicker.date.DatePickerDialog
import com.layernet.thaidatetimepicker.time.RadialPickerLayout
import com.layernet.thaidatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_add_body_mass.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AddBodyMassActivity : BaseActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener, TextWatcher {

    companion object {
        const val PROFILE = "profile"
    }

    private val viewModel: AddBodyMassViewModel by viewModel()
    private lateinit var profile: ProfileResponce
    private lateinit var dateLocale: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_body_mass)
        profile = intent.getParcelableExtra(PROFILE)
        initInstance()
        observeViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        editDay.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dp = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dp.maxDate = calendar
            dp.show(fragmentManager, "DatePicker")
        }

        editTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dp = TimePickerDialog.newInstance(
                this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
            )
            dp.show(fragmentManager, "TimePicker")
        }

        editWeight.addTextChangedListener(this)
        editHeight.addTextChangedListener(this)

        btnSave.setOnClickListener {
            validateData()
        }
    }

    private fun observeViewModel(){
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

        viewModel.successLiveEvent.observe(this, Observer {
            val intent = Intent()
            intent.putExtra("date", it)
            setResult(Activity.RESULT_OK,intent)
            finish()
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: RadialPickerLayout?, hourOfDay: Int, minute: Int, second: Int) {
        editTime.setText("$hourOfDay:$minute")
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year + 543, monthOfYear, dayOfMonth)
        val fm = SimpleDateFormat("dd MMM yyyy", Locale("TH"))
        editDay.setText(fm.format(calendar.time))
        dateLocale = "$dayOfMonth-${monthOfYear+1}-$year"
    }

    // TextWatcher
    override fun afterTextChanged(s: Editable?) {
        calculateBMI()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    @SuppressLint("SetTextI18n")
    private fun calculateBMI() {
        val height = if (editHeight.text.isNullOrEmpty()) {
            0f
        } else {
            editHeight.text.toString().toFloat()
        }
        val weight = if (editWeight.text.isNullOrEmpty()) {
            0f
        } else {
            editWeight.text.toString().toFloat()
        }
        val bmi = BodyMassResponce.getBMI(weight, height)

        textTitleBmi.text = BodyMassResponce.getWeigthResultByBMI(bmi)
        textBMI.text = "%.2f".format(bmi)
        bmiLayout.background = ContextCompat.getDrawable(
            this, when {
                bmi >= 40 -> R.drawable.shape_oval_bmi_red
                bmi >= 30 -> R.drawable.shape_oval_bmi_orange
                bmi >= 25 -> R.drawable.shape_oval_bmi_ligthorage
                bmi >= 18.5 -> R.drawable.shape_oval_bmi_green
                else -> R.drawable.shape_oval_bmi_blue
            }
        )
    }

    private fun validateData() {
        val day = editDay.text
        val time = editTime.text
        val weight = editWeight.text
        val height = editHeight.text

        if (day.isNullOrEmpty()) {
            BaseDialog.warningDialog(this, "Please select day")
            return
        }

        if (time.isNullOrEmpty()) {
            BaseDialog.warningDialog(this, "Please select time")
            return
        }

        if (weight.isNullOrEmpty()) {
            BaseDialog.warningDialog(this, "Please enter your weight")
            return
        }

        if (height.isNullOrEmpty()) {
            BaseDialog.warningDialog(this, "Please enter your height")
            return
        }

        viewModel.addBodyMass(
            dateLocale,
            time.toString(),
            weight.toString(),
            height.toString(),
            profile
        )
    }
}
