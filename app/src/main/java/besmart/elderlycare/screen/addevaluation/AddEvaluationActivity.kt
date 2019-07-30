package besmart.elderlycare.screen.addevaluation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityAddEvaluationBinding
import besmart.elderlycare.model.blood.BloodPressuresResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import com.layernet.thaidatetimepicker.date.DatePickerDialog
import com.layernet.thaidatetimepicker.time.RadialPickerLayout
import com.layernet.thaidatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEvaluationActivity : BaseActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var binding: ActivityAddEvaluationBinding
    private val viewModel: AddEvaluationViewModel by viewModel()
    private lateinit var profile: ProfileResponce
    private lateinit var dateLocale: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_evaluation)
        binding.viewModel = viewModel
        profile = intent.getParcelableExtra(PROFILE)
        initInstance()
        observeViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.editDay.setOnClickListener {
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

        binding.editTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dp = TimePickerDialog.newInstance(
                this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
            )
            dp.show(fragmentManager, "TimePicker")
        }

        binding.btnAddEvaluation.setOnClickListener {
            viewModel.onAddEvaluation(profile)
        }
        binding.editSys.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.textSys.text = if (s.isNullOrEmpty()) {
                    "0.0"
                } else {
                    s.toString()
                }
                calculateDIA()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editDia.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.textDia.text = if (s.isNullOrEmpty()) {
                    "0.0"
                } else {
                    s.toString()
                }
                calculateDIA()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeViewModel() {
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
            setResult(Activity.RESULT_OK, intent)
            finish()
        })
    }

    private fun calculateDIA() {
        val sys = if (binding.editSys.text.isNullOrEmpty()) {
            0f
        } else {
            binding.editSys.text.toString().toFloat()
        }
        val dia = if (binding.editDia.text.isNullOrEmpty()) {
            0f
        } else {
            binding.editDia.text.toString().toFloat()
        }
        binding.textTitleResult.text = BloodPressuresResponse.getResult(sys, dia)
        binding.textTitleResult.setTextColor(BloodPressuresResponse.getResultColor(sys, dia))
        binding.diaLayout.background = ContextCompat.getDrawable(
            this, if (sys < 120 && dia < 80) {
                R.drawable.shape_oval_bmi_green
            } else if ((sys in 120..129) && (dia < 80)) {
                R.drawable.shape_oval_bmi_dark_yellow
            } else if ((sys in 130..139) || (dia in 80..89)) {
                R.drawable.shape_oval_bmi_ligthorage
            } else if ((sys in 140..179) || (dia in 90..119)) {
                R.drawable.shape_oval_bmi_orange
            } else if (sys >= 180 || dia >= 90) {
                R.drawable.shape_oval_bmi_red
            } else {
                R.drawable.shape_oval_bmi_green
            }
        )
    }

    override fun onTimeSet(view: RadialPickerLayout?, hourOfDay: Int, minute: Int, second: Int) {
        viewModel.time.set("$hourOfDay:$minute")
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year + 543, monthOfYear, dayOfMonth)
        val fm = SimpleDateFormat("dd MMM yyyy", Locale("TH"))
        viewModel.date.set(fm.format(calendar.time))
        dateLocale = "$dayOfMonth-${monthOfYear+1}-$year"
    }
}
