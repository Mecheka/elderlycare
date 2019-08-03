package besmart.elderlycare.screen.editprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityEditProfileBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.layernet.thaidatetimepicker.date.DatePickerDialog
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    companion object{
        const val USER = "user"
    }

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModel()
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(USER) as ProfileResponce
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        binding.viewModel = viewModel
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        initDataViewModel()
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
                    viewModel.genderId.set((index + 1).toString())
                    binding.editGender.setText(text)
                }
                positiveButton(R.string.dialog_ok)
                negativeButton(R.string.dialog_cancel)
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.onSaveClick(profile.id.toString())
        }
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.successLiveData.observe(this, Observer { success ->
            setResult(Activity.RESULT_OK)
            finish()
        })

        viewModel.loadingLiveData.observe(this, Observer { loading ->
            if (loading) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDataViewModel() {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = inputFormat.parse(profile.birthday)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("TH"))

        viewModel.cardId.set(profile.cardID)
        viewModel.firstName.set(profile.firstName)
        viewModel.lastName.set(profile.lastName)
        viewModel.birthday.set(outputFormat.format(date))
        if (profile.genderID == 1) {
            viewModel.genderId.set("1")
            binding.editGender.setText("ชาย")
        } else {
            viewModel.genderId.set("2")
            binding.editGender.setText("หญิง")
        }
        viewModel.address.set(profile.address)
        viewModel.phone.set(profile.phone)
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("TH"))
        val calendar = Calendar.getInstance()
        val newYear = year + 543
        calendar.set(newYear, monthOfYear, dayOfMonth)
        val date = outputFormat.format(calendar.time)
        viewModel.birthday.set(date)
    }
}
