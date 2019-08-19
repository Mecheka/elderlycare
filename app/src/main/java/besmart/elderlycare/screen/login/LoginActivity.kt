package besmart.elderlycare.screen.login

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityLoginBinding
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.main.MainActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.Constance
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_select_user_type.*
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    private val selectType: String by lazy {
        intent.getStringExtra(SelectType.SELECTTYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        viewModel.selectType = selectType
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getTitleByType()
        binding.userLayout.hint = getHintByType()
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

        viewModel.successLiveData.observe(this, Observer {
            Intent().apply {
                this.setClass(this@LoginActivity, MainActivity::class.java)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                this.putExtra(SelectType.SELECTTYPE, selectType)
                Hawk.put(Constance.LOGIN_TYPE, selectType)
                startActivity(this)
                this@LoginActivity.finish()
            }
        })
    }

    private fun getTitleByType(): String {
        return when (selectType) {
            SelectType.ORSOMO -> getString(R.string.welcome_login) + " " + getString(R.string.orsomo)
            SelectType.HEALTH -> getString(R.string.welcome_login) + " " + getString(R.string.health)
            else -> getString(R.string.welcome_login) + " " + getString(R.string.person)
        }
    }

    private fun getHintByType():String{
        return when (selectType){
            SelectType.ORSOMO -> getString(R.string.orsomoId)
            SelectType.HEALTH -> getString(R.string.healthId)
            else -> getString(R.string.passportId)
        }
    }
}
