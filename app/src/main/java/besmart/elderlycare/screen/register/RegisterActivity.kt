package besmart.elderlycare.screen.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityRegisterBinding
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.SelectType.Companion.HEALTH
import besmart.elderlycare.screen.SelectType.Companion.ORSOMO
import besmart.elderlycare.screen.SelectType.Companion.PERSON
import besmart.elderlycare.screen.SelectType.Companion.SELECTTYPE
import besmart.elderlycare.util.DateInputMask
import kotlinx.android.synthetic.main.activity_select_user_type.*


class RegisterActivity : AppCompatActivity(),
    SpinerAdapter.OnSpinnerItemClick {

    private lateinit var binding: ActivityRegisterBinding

    private val selectType: String by lazy {
        intent.getStringExtra(SELECTTYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getTitleByType()
        binding.employeeLayout.hint = getHintByType()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        DateInputMask(binding.editDate).listen()
        val genderList = resources.getStringArray(R.array.gender).toList()
        binding.txtGender.setAdapter(
            SpinerAdapter(
                genderList,
                binding.txtGender,
                this
            )
        )
        if (selectType == PERSON){
            binding.employeeLayout.visibility = View.GONE
        }
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
            SelectType.ORSOMO -> getString(R.string.orsomoId)
            SelectType.HEALTH -> getString(R.string.healthId)
            else -> getString(R.string.passportId)
        }
    }

    override fun onSpinnerItemClick(text: String, view: View) {
        binding.txtGender.text = text
        binding.txtGender.dialog?.dismiss()
    }
}
