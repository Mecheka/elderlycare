package besmart.elderlycare.screen.elderlyprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.screen.addelderly.AddElderlyActivity
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_calendar.*
import org.koin.android.viewmodel.ext.android.viewModel

class ElderlyProfileActivity : BaseActivity() {

    private val viewModel: ElderlyProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elderly_profile)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.WarringDialog(this, it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })

        viewModel.successLiveData.observe(this, Observer {

        })

        viewModel.getMyElderly()
    }

    fun onAddClick(view: View) {
        Intent().apply {
            this.setClass(this@ElderlyProfileActivity, AddElderlyActivity::class.java)
            startActivity(this)
        }
    }
}
