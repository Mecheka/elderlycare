package besmart.elderlycare.screen.sugaradd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivitySugarAddBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.model.sugar.SugarResponse
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import com.layernet.thaidatetimepicker.date.DatePickerDialog
import com.layernet.thaidatetimepicker.time.RadialPickerLayout
import com.layernet.thaidatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class SugarAddActivity : BaseActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var binding: ActivitySugarAddBinding
    private val viewModel: SugarAddViewModel by viewModel()
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sugar_add)
        binding.viewModel = viewModel
        initInstance()
        observeViewModel()
    }

    private fun initInstance(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.editBlood.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateFBS()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
        binding.btnSave.setOnClickListener {
            viewModel.addSugarBlood(profile.cardID!!)
        }
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


    private fun calculateFBS() {
        val fbs = if (binding.editBlood.text.isNullOrEmpty()) {
            0
        } else {
            binding.editBlood.text.toString().toInt()
        }
        binding.textResult.text = SugarResponse.getResult(fbs)
        binding.textFbs.text = fbs.toString()
        binding.fbsLayout.background = ContextCompat.getDrawable(
            this, when {
                fbs >= 200 -> R.drawable.shape_oval_bmi_orange
                fbs in 140..200 -> R.drawable.shape_oval_bmi_ligthorage
                else -> R.drawable.shape_oval_bmi_green
            }
        )
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year + 543, monthOfYear, dayOfMonth)
        val fm = SimpleDateFormat("dd MMM yyyy", Locale("TH"))
        viewModel.date.set(fm.format(calendar.time))
    }

    override fun onTimeSet(view: RadialPickerLayout?, hourOfDay: Int, minute: Int, second: Int) {
        viewModel.time.set("$hourOfDay:$minute")
    }
}
