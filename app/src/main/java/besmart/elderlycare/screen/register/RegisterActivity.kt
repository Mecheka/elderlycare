package besmart.elderlycare.screen.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityRegisterBinding
import besmart.elderlycare.screen.SelectType.Companion.HEALTH
import besmart.elderlycare.screen.SelectType.Companion.ORSOMO
import besmart.elderlycare.screen.SelectType.Companion.PERSON
import besmart.elderlycare.screen.SelectType.Companion.SELECTTYPE
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.welcome.WelcomeActivity
import besmart.elderlycare.util.BaseDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.layernet.thaidatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_select_user_type.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class RegisterActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    private val selectType: String by lazy {
        intent.getStringExtra(SELECTTYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.viewModel = viewModel
        viewModel.selectType = selectType
        binding.lifecycleOwner = this
        initInstance()
        observeViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getTitleByType()
        binding.employeeLayout.hint = getHintByType()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.editDate.setOnClickListener {
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

        binding.editGender.setOnClickListener {
            MaterialDialog(this).show {
                listItems(R.array.gender) { _, index, text ->
                    viewModel.genderId.set(index.toString())
                    binding.editGender.setText(text)
                }
                positiveButton(R.string.dialog_ok)
                negativeButton(R.string.dialog_cancel)
            }
        }
//        val genderList = resources.getStringArray(R.array.gender).toList()
//        binding.txtGender.setAdapter(
//            SpinerAdapter(
//                genderList,
//                binding.txtGender,
//                this
//            )
//        )
        if (selectType == PERSON) {
            binding.employeeLayout.visibility = View.GONE
        }
    }

    private fun observeViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.successLiveData.observe(this, Observer { success ->
            if (success) {
            Intent().apply {
                this.setClass(this@RegisterActivity, WelcomeActivity::class.java)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
                this@RegisterActivity.finish()
            }
            }
        })

        viewModel.loadingLiveData.observe(this, Observer { loading ->
            if (loading) {
                showLoadingDialog(this)
            }else{
                dismissDialog()
            }
        })
    }

//    override fun onSpinnerItemClick(
//        text: String,
//        view: View,
//        position: Int
//    ) {
//        viewModel.genderId.set(position.toString())
//        binding.txtGender.text = text
//        binding.txtGender.dialog?.dismiss()
//    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val newYear = year+543
        val date = "$dayOfMonth/$monthOfYear/$newYear"
        viewModel.birthday.set(date)
    }

    private fun getTitleByType(): String {
        return when (selectType) {
            ORSOMO -> getString(R.string.register) + " " + getString(R.string.orsomo)
            HEALTH -> getString(R.string.register) + " " + getString(R.string.health)
            else -> getString(R.string.register) + " " + getString(R.string.person)
        }
    }

    private fun getHintByType():String{
        return when (selectType){
            ORSOMO -> getString(R.string.orsomoId)
            HEALTH -> getString(R.string.healthId)
            else -> getString(R.string.passportId)
        }
    }
}
